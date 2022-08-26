package com.example.news.utilClass

import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.BaseInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.news.application.MyApplication
import com.example.news.keyValue.TOTALKEYLIST
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.crypto.Cipher
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import android.content.DialogInterface
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.adapter.personActivity.PersonMainPageButtonAdapter
import com.example.news.classes.network.getUserDataByIdNetwork.User


object  Util {
    class PersonMassage(val user: User){
        val listOfIcon= listOf(
            R.drawable.age,
            R.drawable.personal_identity,
            R.drawable.username
        )
        val listOfMassage=listOf(user.age,user.role,user.username)
        val count=3
    }
    val personMainPageButtonList= listOf(
        PersonMainPageButtonAdapter.ButtonInPersonMainPage(R.drawable.change_massage,"修改信息"),
        PersonMainPageButtonAdapter.ButtonInPersonMainPage(R.drawable.history,"历史"),
        PersonMainPageButtonAdapter.ButtonInPersonMainPage(R.drawable.like,"收藏"),
        PersonMainPageButtonAdapter.ButtonInPersonMainPage(R.drawable.massage,"私信"),
    )
    private const val level="Debug"
    fun repLaceFragment(fragment: Fragment, boolean: Boolean,activity: FragmentActivity,int: Int){
        val fragmentManager = activity.supportFragmentManager
        val transaction=fragmentManager.beginTransaction()
        transaction.replace(int,fragment)
        if(boolean){
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
    fun logDetail(preString: String="----------------",text:String=""){
        if(level =="Debug"){
            Log.d(preString,text)
        }
    }
    fun getScreenWidth(activity: Activity):Float{
        val outMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getRealMetrics(outMetrics)
        val widthPixel = outMetrics.widthPixels
        val heightPixel = outMetrics.heightPixels
        Log.w(TAG, "widthPixel = $widthPixel,heightPixel = $heightPixel")
        //widthPixel = 1440,heightPixel = 2960
        return widthPixel.toFloat()
    }
    fun action(view: View, string: String, start:Float, end:Float, time:Long,baseInterpolator: BaseInterpolator){
        val animator: ObjectAnimator = ObjectAnimator.ofFloat(view,string,start,end )
        animator.duration = time
        animator.interpolator = baseInterpolator
        animator.start()
    }
    fun <T> startIntent(context: Context, cls:Class<T>,intentData:Map<String,String>?){
        val intent= Intent(context, cls)
        if (intentData != null) {
            for(i in intentData.keys){
                intent.putExtra(i,intentData[i])
            }
        }
        context.startActivity(intent)
    }
    fun easyToast(text: String, int: Int=Toast.LENGTH_SHORT){
        Toast.makeText(MyApplication.context,text,int).show()
    }
    fun isEmail(email: String?): Boolean {
        if (null == email || "" == email) return false
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        val p: Pattern =
            Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*") //复杂匹配
        val m: Matcher = p.matcher(email)
        return m.matches()
    }
    fun sendEmail(myEmailSMTPHost:String,myEmailAccount:String,receiveMailAccount:String,myEmailPassword:String):String{
        try {
            val verificationCode=(100000..999999).random().toString()
            // 1. 创建参数配置, 用于连接邮件服务器的参数配置
            val props = Properties() // 参数配置
            props.setProperty("mail.transport.protocol", "smtp") // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.smtp.host", myEmailSMTPHost) // 发件人的邮箱的 SMTP 服务器地址
            props.setProperty("mail.smtp.auth", "true") // 需要请求认证

            // PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
            //     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
            //     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
            /*
            // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
            //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
            //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
            final String smtpPort = "465";
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", smtpPort);
            */

            // 2. 根据配置创建会话对象, 用于和邮件服务器交互
            val session: Session = Session.getInstance(props)
            session.debug = true // 设置为debug模式, 可以查看详细的发送 log

            // 3. 创建一封邮件
            val message: MimeMessage = createMimeMessage(
                session,
                myEmailAccount,
                receiveMailAccount,
                verificationCode
            )

            // 4. 根据 Session 获取邮件传输对象
            val transport: Transport = session.transport

            // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
            //
            //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
            //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
            //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
            //
            //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
            //           (1) 邮箱没有开启 SMTP 服务;
            //           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
            //           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
            //           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
            //           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
            //
            //    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
            transport.connect(myEmailAccount,myEmailPassword)

            // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.allRecipients)

            // 7. 关闭连接
            transport.close()
            return verificationCode.toString()
        }catch (e:Exception){
            return "FAIL"
        }


    }
    private fun createMimeMessage(session: Session?, sendMail: String?, receiveMail: String?, text: String): MimeMessage {
        // 1. 创建一封邮件
        val message = MimeMessage(session)
        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(InternetAddress(sendMail, "热点集讯官方", "UTF-8"))

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(
            MimeMessage.RecipientType.TO,
            InternetAddress(receiveMail, "热点集讯官方", "UTF-8")
        )
        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject("注册验证码", "UTF-8")

        // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent("用户你好,欢迎使用热点集讯,您的验证码为$text", "text/html;charset=UTF-8")

        // 6. 设置发件时间
        message.sentDate = Date()

        // 7. 保存设置
        message.saveChanges()
        return message
    }

    fun setSharePreferences(
        activity: FragmentActivity?,
        place:String,
        mapBool:Map<String,Boolean>?,
        mapString: Map<String, String>?){
        val editor = MyApplication.context.getSharedPreferences(place,Context.MODE_PRIVATE).edit()
        if (mapString != null) {
            for(key in mapString.keys){
                editor.putString(key,mapString[key])
            }
        }
        if (mapBool != null) {
            for(key in mapBool.keys){
                mapBool[key]?.let { editor.putBoolean(key, it) }
            }
        }
        editor.apply()
    }
    fun getSharePreferencesString(activity: FragmentActivity?, place: String, key: String): String{
        val editor = MyApplication.context.getSharedPreferences(place, Context.MODE_PRIVATE)
        return editor.getString(key, "").toString()
    }
    fun getSharePreferencesBool(activity: FragmentActivity, place: String, key: String): Boolean {
        val editor = activity.getSharedPreferences(place, Context.MODE_PRIVATE)
        return editor.getBoolean(key, false)
    }
    fun clearSharePreference(activity: FragmentActivity,place: String){
        val editor = activity.getSharedPreferences(place, Context.MODE_PRIVATE).edit()
        for(key in TOTALKEYLIST.KEY_MAP[place]!!){
            editor.putString(key,"")
        }
        editor.apply()
    }

    suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    Log.d("response",response.body().toString())
                    if (body!=null)it.resume(body)
                    else it.resumeWithException(RuntimeException("response is null"))
                }
            })
        }
    }

    fun encodeToRsa(text:String, key: String): String {
        val keyFactory = KeyFactory.getInstance("RSA")
        val publicKey =
            keyFactory.generatePublic(X509EncodedKeySpec(Base64.getDecoder().decode(key)))
        val transformation = "RSA/ECB/PKCS1Padding"
        val cipherPassword = Cipher.getInstance(transformation)
        cipherPassword.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptPassword = cipherPassword.doFinal(text.toByteArray())
        return Base64.getEncoder().encode(encryptPassword).decodeToString()
    }

    fun showInformation(msg: String,activity: FragmentActivity,func:()->Unit) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(msg)
        builder.setTitle("提示")
        builder.setPositiveButton("确定", DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
            func()
        })
        builder.setNegativeButton("取消",
            DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
        builder.create().show()
    }

    fun<T : RecyclerView.ViewHolder> RecyclerView.initRecyclerView(context: Context, direction: Int, adapter: RecyclerView.Adapter<T>){
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = direction
        this.layoutManager = layoutManager
        this.adapter = adapter
    }

}