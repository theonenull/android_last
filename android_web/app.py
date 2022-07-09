import calendar
import hashlib
import os
import threading
import calendar, time, os
import base64
import pymysql
from Crypto import Random
from Crypto.Cipher import PKCS1_v1_5 as Cipher_pkcs1_v1_5
from Crypto.PublicKey import RSA
from Crypto.SelfTest.Hash.test_cSHAKE import file
from flask import Flask, request, jsonify

app = Flask(__name__)
name = "  "


# md5加密方法
def encrypt(string):
    h1 = hashlib.md5()
    h1.update(string.encode('utf-8'))
    return h1.hexdigest()


class MysqlHelper:
    def __init__(self, host, port, user, password, database):
        try:
            self.conn = pymysql.connect(host=host, user=user, port=port, password=password, database=database)
            self.cursor = self.conn.cursor()
        except Exception as e:
            return

    def execute(self, sql):
        self.cursor.execute(sql)
        rowcount = self.cursor.rowcount
        return rowcount

    def delete(self, **kwargs):
        table = kwargs.get('table')
        where = kwargs.get('where')
        sql = 'DELETE FROM %s where %s' % (table, where)
        self.cursor.execute(sql)
        try:
            self.cursor.execute(sql)
            self.conn.commit()
            rowcount = self.cursor.rowcount
        except Exception as e:

            self.conn.rollback()
        return rowcount

    def insert(self, **kwargs):
        table = kwargs['table']
        del kwargs['table']
        sql = 'insert into %s(' % table
        fields = ""
        values = ""
        for k, v in kwargs.items():
            fields += "%s," % k
            values += "'%s'," % v
        fields = fields.rstrip(',')
        values = values.rstrip(',')
        sql = sql + fields + ")values(" + values + ")"

        try:
            self.cursor.execute(sql)
            self.conn.commit()
            res = self.cursor.lastrowid
            return sql
        except Exception as e:

            self.conn.rollback()
            return -1

    def update(self, **kwargs):
        table = kwargs.get('table')
        kwargs.pop('table')
        where = kwargs.get('where')
        kwargs.pop('where')
        sql = 'update %s set ' % table
        for k, v in kwargs.items():
            sql += '%s="%s",' % (k, v)
        sql = sql.rstrip(',')
        sql += ' where %s' % where

        try:
            self.cursor.execute(sql)
            self.conn.commit()
            rowcount = self.cursor.rowcount
        except Exception as e:
            self.conn.rollback()
            rowcount = 0
        return rowcount

    def selectTopone(self, **kwargs):
        table = kwargs['table']
        where = 'where' in kwargs and 'where ' + kwargs['where'] or ''
        order = 'order' in kwargs and 'order by ' + kwargs['order'] or ''
        sql = 'select * from %s %s %s ' % (table, where, order)

        try:
            self.cursor.execute(sql)
            data = self.cursor.fetchone()
            return data
        except Exception as e:
            self.conn.rollback()
            return None

    def selectAll(self, **kwargs):
        table = kwargs['table']
        field = 'field' in kwargs and kwargs['field'] or '*'
        where = 'where' in kwargs and 'where ' + kwargs['where'] or ''
        order = 'order' in kwargs and 'order by ' + kwargs['order'] or ''
        limit = 'limit' in kwargs and 'limit ' + kwargs['limit'] or ''
        sql = 'select %s from %s %s %s %s' % (field, table, where, order, limit)

        try:
            self.cursor.execute(sql)
            data = self.cursor.fetchall()
        except Exception as e:

            self.conn.rollback()
        return data

    def updateDATA(self, **kwargs):
        table = kwargs['table']
        where = 'where' in kwargs and 'where ' + kwargs['where'] or ''
        set = 'set' in kwargs and 'set ' + kwargs['set']
        # UPDATE
        # table_name
        # SET
        # field1 = new - value1, field2 = new - value2
        sql = 'UPDATE %s %s %s' % (table, set, where)
        try:
            self.cursor.execute(sql)
            return "YES"
        except Exception as e:

            self.conn.rollback()
            return "NO"


conn = MysqlHelper('127.0.0.1', 3306, 'root', '351172abc2015', 'android_web')


def decryptMassage(str_name):
    with open('master-private.pem') as f:
        key = f.read()
    RANDOM_GENERATOR = Random.new().read
    rsakey = RSA.importKey(key)
    cipher = Cipher_pkcs1_v1_5.new(rsakey)
    try:
        password = cipher.decrypt(base64.b64decode(str_name), RANDOM_GENERATOR).decode()
        return password

    except Exception as e:
        return "错误"


def verify(userdata, cookie):
    verificationData = conn.selectTopone(table='access', where='firstname="%s"' % userdata)
    if verificationData is not None:
        if encrypt(verificationData[0] + verificationData[1]) != cookie:
            return encrypt(verificationData[0] + verificationData[1]) + "   " + verificationData[0] + "  " + \
                   verificationData[1]
        else:
            return 'PASS'
    else:
        return 'CHANGED'


@app.route('/', methods=['POST'])
def hello_world():
    cookie = request.headers.get('cookieforuser')
    userdata = request.headers.get('userdata')
    if verify(str(userdata), str(cookie)) == 'PASS':
        try:
            data = {
                "state": "SUCCESS",
                "cookieforuser": cookie,
                "userdata": userdata,
            }
            return jsonify(data)
        except Exception as e:
            return jsonify({
                "state": "FAIL"
            })
    try:
        password_rsa = request.form.get('password')
        firstname_rsa = request.form.get('firstname')
        password = decryptMassage(password_rsa)
        firstname = decryptMassage(firstname_rsa)
        firstname_mysql = encrypt(firstname)
        password_mysql = encrypt(password)
        result = conn.selectTopone(table='access', where='firstname="%s"' % firstname_mysql)
        if result is not None:
            if result[1] == password_mysql:
                cookie = encrypt(firstname_mysql + password_mysql)
                userdata = firstname_mysql
                data = {
                    "state": "SUCCESS",
                    "cookieforuser": cookie,
                    "userdata": userdata,
                }
                return jsonify(data)
            else:
                return cookie + " " + userdata
        else:
            return jsonify({
                "state": "FAIL"
            })
    except Exception as e:
        return jsonify({
            "state": "FAIL"
        })


@app.route('/register', methods=['POST'])
def register():
    try:
        password_rsa = request.form.get('password')
        firstname_rsa = request.form.get('firstname')
        password_rsa2 = password_rsa.replace(' ', '+')
        firstname_rsa2 = firstname_rsa.replace(' ', '+')
        password = decryptMassage(password_rsa2)
        firstname = decryptMassage(firstname_rsa2)
        firstname_mysql = encrypt(firstname)
        password_mysql = encrypt(password)
        result = conn.selectTopone(table='access', where='firstName="%s"' % firstname_mysql)
        if result is not None:
            return "ALREADY"
        conn.insert(table='access', firstname=firstname_mysql, password=password_mysql)
        conn.insert(table='tag', code=firstname_mysql, tag_id=0, tag_name="未分组")
        cookie = encrypt(firstname_mysql + password_mysql)
        userdata = firstname_mysql
        data = {
            "state": "SUCCESS",
            "cookieforuser": cookie,
            "userdata": userdata,
        }
        return jsonify(data)
    except Exception as e:
        return jsonify({
            "state": "FAIL"
        })


@app.route("/recording", methods=['GET'])
def getRecording():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
                "data": []
            }
            return jsonify(data_sever)
        recordings = conn.selectAll(table='list_recording', where="code='%s'" % userdata)
        data_sever = {
            "state": "SUCCESS",
            "data": recordings
        }
        return jsonify(data_sever)
    except Exception as e:
        data_sever = {
            "data": [],
            "state": 'FAIL'
        }
        return jsonify(data_sever)


@app.route('/person', methods=['GET'])
def getPersonMassage():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
                "data": []
            }
            return jsonify(data_sever)
    except Exception as e:
        data_server = {
            'state': "FAIL",
            "data": []
        }
        return jsonify(data_server)


@app.route('/tag', methods=['POST'])
def insertTag():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
            }
            return jsonify(data_sever)
        tag_name = request.form.get('tag_name')
        tag_mysql = conn.selectTopone(table="tag", where='code="%s" and tag_name="%s"' % (userdata, tag_name))
        if tag_mysql is not None:
            data_sever = {
                "state": "ALREADY",
            }
            return data_sever
        conn.insert(table='tag', code=userdata, tag_name=tag_name)
        data_sever = {
            "state": "SUCCESS"
        }
        return jsonify(data_sever)
    except Exception as e:
        data_server = {
            'state': "FAIL",
        }
        return jsonify(data_server)


@app.route('/tag', methods=['PUT'])
def reSetTag():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
            }
            return jsonify(data_sever)
        tag_name = request.args.get('tag_name')
        tag_id = request.args.get('tag_id')
        conn.update(table="tag", where="tag_id=%s" % tag_id, tag_name=tag_name)
        return jsonify({
            "state": "SUCCESS"
        })
    except Exception as e:
        return jsonify({
            "state": "FAIL",
            "massage": str(e)
        })


@app.route('/tag', methods=['DELETE'])
def deleteTag():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
            }
            return jsonify(data_sever)
        tag_id = request.args.get('tag_id')
        conn.delete(table="tag", where="tag_id=%s" % tag_id)
        return jsonify({
            "state": "SUCCESS"
        })
    except Exception as e:
        return jsonify({
            "state": "FAIL",
            "massage": str(e)
        })


@app.route('/taglist', methods=['GET'])
def getTagList():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
            }
            return jsonify(data_sever)
        list_sever = []
        data = conn.selectAll(table='tag', where='code="%s"' % userdata)
        for res in data:
            json_mysql = {"tag_id": res[1], "tag_name": res[2]}
            list_sever.append(json_mysql)
        return jsonify({
            "state": "SUCCESS",
            "list": list_sever
        })
    except Exception as e:
        return jsonify({
            "state": "FAIL",
            "massage": str(e)
        })


@app.route('/tag/note', methods=['GET'])
def getPersonTagList():
    a = ''
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
            }
            return jsonify(data_sever)
        list_server = []
        tag_list = conn.selectAll(table='tag', field='tag_name,tag_id', where='code="%s"' % userdata)
        for tag_name, tag_id in tag_list:
            if conn.selectAll(table='note',
                              where="tag_id=%s and code='%s'" % (
                                      str(tag_id), userdata)) != ():
                result = conn.selectAll(table='note',
                                        where="tag_id=%s and code='%s'" % (
                                            str(tag_id), userdata))
                list_ww = []
                for res in result:
                    json_mysql = {"id": res[0], "code": res[1], "name": res[2], "data": res[3], "num": res[4],
                                  "state": res[5],
                                  "image": res[6], "tag_name": res[7], "tag_id": res[8], "count": res[9]}
                    list_ww.append(json_mysql)
                json = {
                    "id": tag_id,
                    "tag": tag_name,
                    "list": list_ww
                }
                list_server.append(json)
            else:
                json = {
                    "id": tag_id,
                    "tag": tag_name,
                    "list": []
                }
                list_server.append(json)
        return jsonify(list_server)
    except Exception as e:
        return jsonify({
            "state": "FAIL"
        })


@app.route('/note', methods=['GET'])
def get_note():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
            }
            return jsonify(data_sever)
        id = request.args.get('id')
        res = conn.selectTopone(table="note", where='id="%s"' % id)
        return jsonify({
            "id": res[0], "code": res[1], "name": res[2], "date": res[3], "num": res[4],
            "state": res[5],
            "image": res[6], "tag_name": res[7], "tag_id": res[8], "count": res[9]
        })
    except Exception as e:
        return jsonify({
            "state": "FAIL"
        })


@app.route('/note', methods=['POST'])
def insertNote():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
            }
            return jsonify(data_sever)
        form = request.form.to_dict()
        tag_id = request.form.get('tag_id')
        form['code'] = userdata
        form['id'] = encrypt(userdata + str(time.time()))
        if tag_id is None:
            sql = conn.insert(table="note", **form, tag_id=0)
        else:
            sql = conn.insert(table="note", **form)
        tag_name = request.form.get('tag')
        # if conn.selectTopone(table='tag', where='tag="%s"' % tag_name) == ():
        #     sql=conn.insert(table='tag', code=userdata, tag_name=tag_name)
        return {
            "state": "SUCCESS",
            "sql": sql
        }
    except Exception as e:
        return jsonify({
            "state": "FAIL"
            ,
            "massage": str(e)
            ,
            "form": str(form)
        })


@app.route('/note', methods=['PUT'])
def updateNote():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
            }
            return jsonify(data_sever)
        id = request.form.get('id')
        form = request.form.to_dict()
        form.pop('id')
        conn.update(table='note', **form, where='id="%s" and code="%s"' % (id, userdata))
        return {
            "state": "SUCCESS"
        }
    except Exception as e:
        return jsonify({
            "state": "FAIL"
        })


@app.route('/note', methods=['DELETE'])
def deleteNote():
    try:
        userdata = request.headers.get('userdata')
        cookie_for_user = request.headers.get('cookieforuser')
        if verify(userdata, cookie_for_user) != "PASS":
            data_sever = {
                "state": "FAIL",
            }
            return jsonify(data_sever)
        id = request.args.get('id')
        conn.delete(table='note', where='id="%s" AND code="%s"' % (id, userdata))
        return {
            "state": "SUCCESS",
            "id": str(id)
        }
    except Exception as e:
        return jsonify({
            "state": "FAIL"
        })


@app.route('/file', methods=['POST'])
def send_file():
    try:
        id = request.form.get('id')
        file = request.files.get('file')
        if file is None:
            # 表示没有发送文件
            return str(request.form)

        file_name = file.filename  # print(file.filename)
        # 获取前缀（文件名称）print(os.path.splitext(file_name)[0])
        # 获取后缀（文件类型）print(os.path.splitext(file_name)[-1])\
        suffix = os.path.splitext(file_name)[-1]  # 获取文件后缀（扩展名）
        basePath = os.path.dirname(__file__)  # 当前文件所在路径print(basePath)
        nowTime = calendar.timegm(time.gmtime())  # 获取当前时间戳改文件名print(nowTime)
        upload_path = os.path.join(basePath, 'static',
                                   str(nowTime))  # 改到upload目录下# 注意：没有的文件夹一定要先创建，不然会提示没有该路径print(upload_path)
        upload_path = os.path.abspath(upload_path)  # 将路径转换为绝对路径print("绝对路径：",upload_path)
        file.save(upload_path + str(nowTime) + suffix)  # 保存文件
        # http 路径
        url = 'http://121.37.86.25:8000/static' + str(nowTime) + str(nowTime) + suffix

        return {
            'code': 200,
            'messsge': "文件上传成功",
            'fileNameOld': file_name,
            'fileNameSave': str(nowTime) + str(nowTime) + suffix,
            'url': url
        }
    except Exception as e:
        return jsonify({
            "state": "FAIL"
        })


from flask import Response, Flask


@app.route("/image/<imageid>")
def index(imageid):
    try:
        sr = 1
        image_ = file("static/16548512751654851275.png")
        sr += 1
        resp = Response(image_, mimetype="image/png")
        return resp
    except Exception as e:
        return jsonify({
            "state": "FAIL"
        })


@app.route('/test', methods=['POST', 'GET'])
def test():
    try:
        if request.method == 'GET':

            data = [
                {
                    'id': '1',
                    'name': "biu",
                    'version': "1"

                },
                {
                    'id': '1',
                    'name': "biu",
                    'version': "1"

                },
                {
                    'id': '1',
                    'name': "biu",
                    'version': "1"

                }
            ]
            bu = {
                "data": data,
                "state": "success",
                "name": "username"
            }
            return jsonify(bu)
        else:
            try:
                form = request.form
                return str(conn.insert(table='tag', **form))
            except Exception as e:
                return jsonify({
                    "state": "FAIL"
                })
    except Exception as e:
        return jsonify({
            "state": "FAIL"
        })


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)
