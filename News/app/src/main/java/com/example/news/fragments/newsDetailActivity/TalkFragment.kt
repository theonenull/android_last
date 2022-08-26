package com.example.news.fragments.newsDetailActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.news.adapter.recommendDetailActivity.TalkMassageListAdapter
import com.example.news.application.MyApplication
import com.example.news.utilClass.Util
import com.example.news.utilClass.Util.initRecyclerView
import com.example.news.databinding.FragmentTalkBinding
import com.example.news.keyValue.LOGIXDATA
import com.example.news.keyValue.USERDATA
import com.example.news.theActivitys.NewsDetailActivity
import com.example.news.viewModel.RecommendDetailViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.*

class TalkFragment(viewModel: RecommendDetailViewModel,val spotId: String) : Fragment() {
    private var _binding:FragmentTalkBinding? = null
    private val binding:FragmentTalkBinding get() = _binding!!
    private val viewModel:RecommendDetailViewModel
    private lateinit var activityOnFragment: NewsDetailActivity
    private val job=Job()
    private val scope= CoroutineScope(job)
    init {
        this.viewModel=viewModel
    }
    private val startActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.data != null && it.resultCode == Activity.RESULT_OK) {
                it.data?.data.let { uri->
                    val bitmap= uri?.let { it1 -> getBitmapFromUri(it1) }
                    val file = bitmap?.let { it1 -> getFile(it1) }
                    scope.launch {
                        val compressedImageFile = Compressor.compress(activityOnFragment, file!!){
                            resolution(1280, 720)
                            quality(50)
                            format(Bitmap.CompressFormat.WEBP)
                            size(2_097_152)
                        }
                        viewModel.refreshImageForSend(compressedImageFile)
                    }
                }
            }

        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentTalkBinding.inflate(layoutInflater)

        binding.massageList.initRecyclerView(activityOnFragment,LinearLayoutManager.VERTICAL,TalkMassageListAdapter(viewModel.massageList,activityOnFragment,
            Util.getSharePreferencesString(activityOnFragment,USERDATA.PLACE,USERDATA.USER_ID),scope))

        viewModel.sendMassageBackData.observe(activityOnFragment){
            try {
                it.getOrThrow()
            }catch (e:Exception){
                Util.easyToast(e.message.toString())
            }
        }
        viewModel.massageOnTime.observe(activityOnFragment){
            binding.massageList.adapter?.notifyItemInserted(viewModel.massageList.size-1)
            binding.massageList.scrollToPosition(viewModel.massageList.size-1)
        }
        viewModel.sendImageBackData.observe(activityOnFragment){
            try {
                it.getOrThrow()
                Util.easyToast("图片发送成功")
            }catch (e:Exception){
                Util.easyToast(e.message.toString())
                Util.logDetail("--------------",e.message.toString())
            }
        }


        val x=binding.emoList.x
        binding.emoList.x=x+1000
        binding.closeEmo.setOnClickListener{
            it.isEnabled=false
            binding.emoList.x+=1000
            it.isEnabled=true
            binding.buttonToSendEmo.isEnabled=true
        }
        binding.buttonToSendEmo.setOnClickListener{
            it.isEnabled=false
            binding.emoList.x-=1000
        }
        binding.buttonToSendImage.setOnClickListener{
            openGallery()
        }
        binding.buttonToSend.setOnClickListener {
            if(Util.getSharePreferencesString(null,LOGIXDATA.PLACE,LOGIXDATA.ISLOGIN)=="true"){
                if(binding.textToSend.text.toString()==""){
                    Util.easyToast("内容为空")
                }
                else{
                    viewModel.refreshMassageForSend(RecommendDetailViewModel.MassageWithUserIdWithSpotId(binding.textToSend.text.toString(),
                        Util.getSharePreferencesString(null,USERDATA.PLACE,USERDATA.USER_ID),spotId))
                    binding.textToSend.setText("")
                }
            }
            else{
                Util.easyToast("未登录")
            }
        }
        bandingClickListenToEmo()


        return binding.root
    }

    /**
     * 打开相册
     *
     * @param type 打开类型区分码（type是我用来区分回调的）
     */
    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        gallery.addCategory(Intent.CATEGORY_OPENABLE)
        gallery.type="image/*"
        startActivity.launch(gallery)
    }
    @SuppressLint("SetTextI18n")
    fun bandingClickListenToEmo(){
        for (i in 0 until binding.totalEmoList.childCount) {
            val linearLayout=binding.totalEmoList.getChildAt(i) as LinearLayout
            for (j in 0 until linearLayout.childCount){
                linearLayout.getChildAt(j).setOnClickListener {
                    val tempTextView=it as TextView
                    val text=binding.textToSend.text
                    binding.textToSend.setText(text.toString()+tempTextView.text)
                    binding.textToSend.setSelection(binding.textToSend.text.length)
                    binding.textToSend.isFocusable = true;
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityOnFragment=activity as NewsDetailActivity

    }
    private fun getBitmapFromUri(uri: Uri)=activityOnFragment.contentResolver.openFileDescriptor(uri,"r").use{
        BitmapFactory.decodeFileDescriptor(it?.fileDescriptor)
    }
    private fun getFile(bmp: Bitmap): File {
        val defaultPath: String = MyApplication.context.filesDir.absolutePath + "/defaultGoodInfo"
        var file = File(defaultPath)
        if (!file.exists()) {
            file.mkdirs()
        }
        val defaultImgPath = "$defaultPath/messageImg.jpg"
        file = File(defaultImgPath)
        try {
            file.createNewFile()
            val fOut = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 20, fOut)
            fOut.flush()
            fOut.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return file
    }
}
