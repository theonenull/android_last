package com.example.news.fragments.personActivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.news.application.MyApplication
import com.example.news.utilClass.Util

import com.example.news.databinding.FragmentPersonChangeDataBinding
import com.example.news.theActivitys.PersonActivity
import com.example.news.viewModel.PersonViewModel
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

class PersonChangeDataFragment(val viewModel: PersonViewModel) : Fragment() {
    private lateinit var activityOnFragment: PersonActivity
    private val binding:FragmentPersonChangeDataBinding get()=_binding
    private lateinit var _binding:FragmentPersonChangeDataBinding
    private val job= Job()
    var file: File? =null
    private val scope=CoroutineScope(job)
    private val startActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //此处是跳转的result回调方法
            if (it.data != null && it.resultCode == Activity.RESULT_OK) {
                it.data?.data.let { uri->
                    val bitmap= uri?.let { it1 -> getBitmapFromUri(it1) }
                    binding.imageForUserToChange.setImageBitmap(bitmap)
                    file = bitmap?.let { it1 -> getFile(it1) }
                    scope.launch {
                        val compressedImageFile = Compressor.compress(activityOnFragment, file!!){
                            resolution(1280, 720)
                            quality(50)
                            format(Bitmap.CompressFormat.WEBP)
                            size(2_097_152)
                        }
                        file=compressedImageFile
                    }
                }
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentPersonChangeDataBinding.inflate(layoutInflater)
        binding.buttonToChangeUserPicture.setOnClickListener {
            openGallery()
        }
        binding.buttonToCommitChange.setOnClickListener {
//            if(
//                binding.phoneForUserToChange.text.toString()==""||
//                binding.ageForUserToChange.text.toString()==""||
//                binding.qqNumberForUserToChange.text.toString()==""||
//                binding.nickNameForUserToChange.text.toString()==""
//                    ){
//                Util.easyToast("部分信息为空，无法修改")
//            }
//            else{
                file?.let { it1 -> this.viewModel.refreshFile(it1) }
                viewModel.refreshMassageForChange(PersonViewModel.MassageToChange(
                    binding.phoneForUserToChange.text.toString(),
                    binding.ageForUserToChange.text.toString(),
                    binding.nickNameForUserToChange.text.toString(),
                    binding.qqNumberForUserToChange.text.toString()
                ))
//            }
        }
//        binding.buttonToRealizeTheText.setOnClickListener {
//            binding.phoneForUserToChange.setText()
//            binding.ageForUserToChange.text.toString(),
//            binding.nickNameForUserToChange.text.toString(),
//            binding.qqNumberForUserToChange.text.toString()
//        }
        viewModel.refreshFileBackData.observe(activityOnFragment){
            try{
                it.getOrThrow()
                Util.easyToast("头像修改成功")
            }catch (e:Exception){
                Util.easyToast(e.message.toString())
            }
        }
        viewModel.refreshMassageForChangeBackData.observe(activityOnFragment){
            try{
                it.getOrThrow()
                Util.easyToast("信息修改成功")
                activityOnFragment.onBackPressed()
            }catch (e:Exception){
                Util.easyToast(e.message.toString())
            }
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.activityOnFragment=activity as PersonActivity
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
    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_OPEN_DOCUMENT)
        gallery.addCategory(Intent.CATEGORY_OPENABLE)
        gallery.type="image/*"
        startActivity.launch(gallery)
    }

}