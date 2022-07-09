import pymysql

py = pymysql.connect(host='localhost', user='root', password='351172abc2015', port=3306, charset='utf8')
cursor = py.cursor()
cursor.execute("CREATE DATABASE IF NOT EXISTS android_web DEFAULT CHARACTER SET utf8")
dy = pymysql.connect(host='localhost', user='root', password='351172abc2015', port=3306, charset='utf8',
                     database='android_web')
cursor = dy.cursor()
cursor.execute('CREATE TABLE IF NOT EXISTS access (firstname varchar(50) unique primary key,password varchar(200))')
cursor.execute(
    'CREATE TABLE IF NOT EXISTS tag (code varchar(50),tag_id int(200) PRIMARY KEY AUTO_INCREMENT,tag_name varchar(200))')
cursor.execute(
    'CREATE TABLE IF NOT EXISTS note (id char(50) PRIMARY KEY ,code varchar(50),name varchar(500),data varchar(200),num int(10),state int(10),image varchar(500),tag varchar(50),tag_id int(10),count int(10))')

cursor.execute(
    'CREATE TABLE IF NOT EXISTS note (id char(50) PRIMARY KEY ,code varchar(50),name varchar(500),data varchar(200),num int(10),state int(10),image varchar(500),tag varchar(50),tag_id int(10),count int(10))')


