package com.example.news.fragments.loginActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.news.utilClass.Util
import com.example.news.databinding.FragmentLoginBinding
import com.example.news.keyValue.LOGIXDATA
import com.example.news.keyValue.USERDATA
import com.example.news.theActivitys.LoginActivity
import com.example.news.viewModel.LoginViewModel
import kotlinx.coroutines.*


class LoginFragment(userName:String?,passWord:String?,viewModel: LoginViewModel) : Fragment() {

    interface MyListener{
        fun sendMassage()
    }
    private lateinit var activityOnFragment:LoginActivity
    private var userName:String?=null
    var passWord:String?=null
    private val viewModel:LoginViewModel
    var boolean=true
    private val job= Job()
    private lateinit var myListener: MyListener
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    init {
        this.userName=userName
        this.passWord=passWord
        this.viewModel=viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val scope= CoroutineScope(job)
        scope.launch {
            withContext(Dispatchers.IO){
                while (boolean){
                    if(binding.loginEditUserName.getTheText()!=""){
                        withContext(Dispatchers.Main){
                            binding.imageFilterView
                        }
                    }
                    delay(500)
                }
            }
        }
        binding.loginToRegisterTestView.setOnClickListener {
            myListener.sendMassage()
        }
        binding.button.setOnClickListener {
            passWord=binding.loginEditPassword.getTheText()
            userName=binding.loginEditUserName.getTheText()
            if(userName!=""&&passWord!=""){
                viewModel.refreshUserLoginData(userName!!, passWord!!)
            }
            else{
                Util.easyToast("不得为空")
            }
        }

        viewModel.loginBackData.observe(activityOnFragment){
            val result=it.getOrNull()
            if (result != null) {
                Util.easyToast("登录成功")
                if(binding.checkBox.isChecked){
                    val dataForLoginData= mapOf(
                        LOGIXDATA.PASSWORD to passWord.toString(),
                        LOGIXDATA.USERNAME to userName.toString(),
                        LOGIXDATA.ISLOGIN to "true",

                    )
                    Util.setSharePreferences(activityOnFragment, LOGIXDATA.PLACE,null,
                        dataForLoginData)
                    val dataForUserData= mapOf(
                        USERDATA.USER_ID to result.data?.user?.userId.toString(),
                        USERDATA.IMAGE to result.data?.user?.image.toString(),
                        USERDATA.AGE to result.data?.user?.age.toString(),
                        USERDATA.SALT to result.data?.user?.salt.toString(),
                        USERDATA.NICKNAME to result.data?.user?.nickname.toString(),
                        USERDATA.ROLE to result.data?.user?.role.toString()
                    )
                    Util.easyToast(result.data?.user?.userId.toString())
                    Util.setSharePreferences(activityOnFragment, USERDATA.PLACE,null,
                        dataForUserData)
                }
                else{
                    val data= mapOf(
                        LOGIXDATA.USERNAME to "",
                        LOGIXDATA.PASSWORD to "",
                        LOGIXDATA.ISLOGIN to "false"
                    )
                    Util.setSharePreferences(activityOnFragment, LOGIXDATA.PLACE,null,
                        data)
                }
                result.data?.user?.let { it1 -> activityOnFragment.setResultForUserData(it1) }
                activityOnFragment.onBackPressed()
            }
            else{
                try{
                    it.getOrThrow()
                }catch (e:Exception){
                    Util.easyToast(e.message.toString())
                }
            }
        }
        userName?.let {it -> binding.loginEditUserName.setText(it) }
        passWord?.let {it -> binding.loginEditPassword.setText(it) }
        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("fragment_onAttach",context.toString())
        myListener = activity as MyListener
        activityOnFragment= activity as LoginActivity
    }
    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
        boolean=false
        _binding = null
    }
}