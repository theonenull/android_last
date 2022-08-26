package com.example.news.fragments.loginActivity


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.utilClass.Util
import com.example.news.databinding.FragmentRegisterBinding
import com.example.news.theActivitys.LoginActivity
import com.example.news.viewModel.LoginViewModel
import com.example.news.viewModel.RecommendViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class RegisterFragment(val viewModel: LoginViewModel) : Fragment() {
    var code:String="Fail"
    private var eTime: Long = 0
    private val binding:FragmentRegisterBinding get() = _binding!!
    private var _binding:FragmentRegisterBinding?=null
    private val job= Job()
    private val scope= CoroutineScope(job)
    private lateinit var activityOnFragment:LoginActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eTime=Calendar.getInstance().time.time-60*1000
        _binding=FragmentRegisterBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        binding.buttonToRegisterValidation.setOnClickListener {
            if(Util.isEmail(binding.emailText.getTheText())){
                var returnText=""
                scope.launch {
                    withContext(Dispatchers.Main){
                        if((Calendar.getInstance().time.time-eTime)<1000*60){
                            val timeSecond=60-((Calendar.getInstance().time.time-eTime)/1000)
                            Util.easyToast("邮箱发送过于频繁，请$timeSecond"+"秒后重尝试")
                            scope.cancel()
                        }
                    }
                    withContext(Dispatchers.IO){
                        val myEmailAccount = "thenewsofapp@163.com"
                        val myEmailPassword = "DQMWOISLNDMHBEWQ"
                        // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
                        // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
                        val myEmailSMTPHost = "smtp.163.com"
                        // 收件人邮箱（替换为自己知道的有效邮箱）
                        val receiveMailAccount = binding.emailText.getTheText()
                        returnText= Util.sendEmail(myEmailSMTPHost,myEmailAccount,receiveMailAccount,myEmailPassword)
                    }
                    withContext(Dispatchers.Main){
                        if (returnText == "FAIL") {
                            Util.easyToast("邮件发送失败")
                        } else {
                            Util.easyToast("邮件发送成功")
                            code = returnText
                            Util.logDetail("bbbbb",code)
                            eTime = Calendar.getInstance().time.time
                        }
                    }
                }
            }
            else{
                Util.easyToast("邮箱格式错误")
            }
        }
        binding.buttonToRegister.setOnClickListener {
            if(binding.validationCodeText.getTheText()==code){
                if(binding.registerPasswordText.getTheText()==""||binding.emailText.getTheText()==""){
                    Util.easyToast("信息不得为空")
                    return@setOnClickListener
                }
                viewModel.refreshUserRegisterData(binding.emailText.getTheText(),binding.registerPasswordText.getTheText())
            }
            else{
                Util.easyToast("验证码错误")
            }
        }
        viewModel.registerBackData.observe(activityOnFragment){
            try{
                it.getOrThrow()
                Util.easyToast("注册成功")
                activityOnFragment.onBackPressed()
            }catch (e:Exception){
                Util.easyToast(e.message.toString())
            }
//            val result=it.getOrNull()
//            if(result!=null){
//                try{
//                    it.getOrThrow()
//
//                }catch (e:Exception){
//                    Util.easyToast(e.message.toString())
//                }
//            }else{
//                Util.easyToast("注册失败")
//            }
        }
        return binding.root
    }
//    private suspend fun <T> Call<T>.await():T{
//        return suspendCoroutine {
//            enqueue(object : Callback<T> {
//                override fun onFailure(call: Call<T>, t: Throwable) {
//                    it.resumeWithException(t)
//                }
//                override fun onResponse(call: Call<T>, response: Response<T>) {
//                    val body=response.body()
//                    if (body!=null)it.resume(body)
//                    else it.resumeWithException(RuntimeException("response is null"))
//                }
//            })
//        }
//    }
//    private suspend fun getData(username:String,password:String){
//        try {
//            val data= ServiceCreator.create<TotalRetrofit>().getRegisterData(username,password).await()
//            if(data.code=="200"){
//                withContext(Dispatchers.Main){
//                    activity?.let { it1 -> Util.easyToast("注册成功")}
//                    activity?.let { it->it.onBackPressed() }
//                }
//            }
//            else{
//                withContext(Dispatchers.Main){
//                    activity?.let { it1 -> Util.easyToast("${data.message}")}
//                }
//            }
//        }
//        catch (e: Exception){
//            Util.logDetail("获取失败",e.toString())
//        }
//    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activityOnFragment=activity as LoginActivity
    }
}