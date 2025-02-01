package com.example.hrr_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var user: User
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 변수 초기화
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        user = User("닉네임 테스트")
        imageView = binding.ivEditUserImage

        setContentView(binding.root)

        // 취소 클릭 처리
        binding.ivEditCancel.setOnClickListener {
            finish()
        }

        // 완료 클릭 처리
        binding.ivEditComplete.setOnClickListener {
            // TODO: 사용자 정보 서버에 업데이트
            finish()
        }

        //닉네임 바인딩
        val nameText = binding.tvEditUsername
        val nameEdit = binding.etEditUsername
        val nickname = binding.llEditName

        nameText.text = user.nickname
        nameEdit.hint = user.nickname

        nameEdit.visibility = View.GONE // 초기 상태로 EditText 숨김

        nickname.setOnClickListener {
            // 닉네임 부분 클릭 시 편집 모드 진입
            editMode(nameText, nameEdit)
        }

        nameEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // EditText 포커스가 사라지면 저장 처리
                changeSave(nameText, nameEdit)
            }
        }

        // 키보드의 "Done" 감지하여 저장 처리
        nameEdit.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                changeSave(nameText, nameEdit)
                true
            } else {
                false
            }
        }

        // 사진 편집 - 갤러리에서 불러오기
        binding.flEditImg.setOnClickListener {
            openGallery()
            //TODO: 사진 정보 업데이트
        }
    }

    private fun editMode(text: TextView, edit: EditText) {
        // TextView 숨기고 EditText 보이기
        text.visibility = View.GONE
        edit.visibility = View.VISIBLE
        edit.requestFocus()

        // 키보드 보이기
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT)
    }

    // 편집모드 종료 및 저장 처리
    private fun changeSave(text: TextView, edit: EditText) {
        val newName = edit.text.toString().trim()

        val validUtils = ValidUtils
        if(validUtils.isValidNickname(newName)){
            //유효한 경우
            text.text = newName
            edit.visibility = View.GONE
            text.visibility = View.VISIBLE

            // 키보드 숨기기
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(edit.windowToken, 0)
        }
        else{
            edit.error = "한글, 영어, 숫자의 조합으로 최대 10자까지 입력 가능합니다"
            edit.requestFocus()
            return
        }
    }

    // 갤러리 열기
    private fun openGallery() {
        try {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            galleryResultLauncher.launch(intent)
        } catch (e: Exception) {
            Log.e("GalleryDebug", "갤러리 열기 중 예외 발생: ${e.message}", e)
            Toast.makeText(this, "갤러리를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 갤러리 결과 처리
    private val galleryResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            if (selectedImageUri != null) {
                Log.d("GalleryDebug", "갤러리에서 선택된 이미지 URI: $selectedImageUri")
                loadImageFromGallery(selectedImageUri)
            } else {
                Log.e("GalleryDebug", "갤러리에서 선택된 이미지가 없습니다.")
                Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.d("GalleryDebug", "갤러리 열기 취소됨")
            Toast.makeText(this, "갤러리 선택이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // Glide를 이용해 이미지 로드 및 원형 처리
    private fun loadImageFromGallery(uri: Uri) {
        try {
            Glide.with(this)
                .load(uri)
                .circleCrop()  // 원형 변환 적용
                .into(imageView)
            Log.d("GalleryDebug", "이미지 로드 성공: $uri")
        } catch (e: Exception) {
            Log.e("GalleryDebug", "이미지 로드 중 예외 발생: ${e.message}", e)
            Toast.makeText(this, "이미지를 로드할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}