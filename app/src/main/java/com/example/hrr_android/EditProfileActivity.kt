package com.example.hrr_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.ActivityEditProfileBinding
import com.example.hrr_android.databinding.DialogProfileEditBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity(), OnBadgeClickListener {
    private lateinit var binding: ActivityEditProfileBinding        // 뷰 바인딩
    private lateinit var user: User     // 유저 데이터 - 수정된 정보로 업데이트하여 서버에 전달
    private lateinit var userImg: ImageView     // 유저 프로필 사진
    private var selectedBadgeList = mutableListOf<Pair<String, Int>>()      // 대표 뱃지 리스트
    private var obtainedBadgeList = ArrayList<Badge>()      // 획득한 전체 뱃지 리스트
    private var addPossible = true          // 대표 뱃지를 추가로 설정 가능한지 = 현재 대표 뱃지가 3개 미만인지
    private lateinit var editBadgeRVAdapter: EditBadgeRVAdapter
    private var cameraPhotoUri: Uri? = null     // 이미지 uri
    private var onCalled: (() -> Unit)? = null
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 변수 초기화
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        user = User("닉네임 테스트")
        userImg = binding.ivEditUserImage

        // Intent에서 넘어온 뱃지 이름 리스트 가져오기
        val selectedBadgeNames = intent.getStringArrayListExtra("badgeNames") ?: arrayListOf()

        userViewModel.badges.observe(this) { response ->
            obtainedBadgeList = ArrayList(
                (response?.typeBadges.orEmpty() + response?.categoryBadges.orEmpty()) // 모든 뱃지를 합침
                    .filter { it.isObtained } // 획득한 배지만 필터링
                    .map { badge ->
                        Badge(
                            name = badge.name,
                            icon = ValidUtils.getDrawableResId(this, badge.icon),
                            isObtained = true,
                            isSelected = badge.name in selectedBadgeNames
                        )
                    }
            )

            selectedBadgeList = mutableListOf()
            selectedBadgeList = obtainedBadgeList
                .filter { it.isSelected }
                .map { it.name to it.icon }
                .toMutableList()

            if(selectedBadgeList.size==3){
                addPossible = false
            }

            // 대표 뱃지 바인딩
            setSelectedBadges(selectedBadgeList, binding)
        }

//        obtainedBadgeList.apply {
//            add(Badge("오늘부터 챌린저1", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저2", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저3", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저4", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저5", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저6", R.drawable.badge_type_fromtoday_challenger, isSelected = true))
//            add(Badge("오늘부터 챌린저7", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저8", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저9", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저10", R.drawable.badge_type_fromtoday_challenger, isSelected = true))
//            add(Badge("오늘부터 챌린저11", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저12", R.drawable.badge_type_fromtoday_challenger, isSelected = true))
//            add(Badge("오늘부터 챌린저13", R.drawable.badge_type_fromtoday_challenger))
//            add(Badge("오늘부터 챌린저14", R.drawable.badge_type_fromtoday_challenger))
//        }


        setContentView(binding.root)

        // Intent 데이터 확인
        val trigger = intent.getStringExtra("clicked")
        if (trigger == "badge") {
            binding.llEditBadge.post {
                binding.llEditBadge.performClick() // 뱃지 편집 모드 강제 실행
            }
        }

        // 취소 클릭 처리
        binding.tvEditCancel.setOnClickListener {
            finish()
        }

        // 완료 클릭 처리
        binding.tvEditComplete.setOnClickListener {
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
            binding.tvEditComplete.apply {
                // "완료" 버튼 비활성화
                isEnabled = false
                setTextColor(Color.GRAY)
            }
        }

        nameEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // EditText 포커스가 사라지면 저장 처리
                changeSave(nameText, nameEdit)
                binding.tvEditComplete.apply {
                    // "완료" 버튼 비활성화
                    isEnabled = true
                    setTextColor(Color.WHITE)
                }
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

        // 사진 편집 모드
        binding.flEditImg.setOnClickListener {
            val dialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
            val dialogBinding = DialogProfileEditBinding.inflate(layoutInflater) // 뷰 바인딩 객체 생성

            dialog.setContentView(dialogBinding.root)

            // 버튼 클릭 리스너 설정
            dialogBinding.tvEditCamera.setOnClickListener {
                onCalled = ::openCamera
                // 카메라 촬영
                requestPermissions{
                    openCamera()
                }
                dialog.dismiss()
            }

            dialogBinding.tvEditGallery.setOnClickListener {
                onCalled = ::openGallery
                // 갤러리에서 불러오기
                requestPermissions {
                    openGallery()
                }
                //TODO: 사진 정보 업데이트
                dialog.dismiss()
            }

            dialogBinding.tvEditDelete.setOnClickListener {
                // 기본 이미지로 변경(삭제)
                binding.ivEditUserImage.setImageResource(R.drawable.ic_profile_default)
                // Todo:유저 정보에서 프로필 사진 정보 삭제 요청
                Toast.makeText(this, "기본 이미지로 변경되었습니다", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialogBinding.tvEditCancel.setOnClickListener {
                // 취소
                Toast.makeText(this, "취소 선택됨", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            dialog.show()
        }

        // 뱃지 편집
        binding.llEditBadge.setOnClickListener {
            // 획득한 뱃지 리스트 보이게
            binding.rvEditBadge.visibility = View.VISIBLE
            // 현재 뱃지 보이게 흑백 오버레이 숨기기
            binding.viewOverlay01.visibility = View.GONE
            binding.viewOverlay02.visibility = View.GONE
            binding.viewOverlay03.visibility = View.GONE
        }
        //카테고리 뱃지 RecyclerView 연결
        editBadgeRVAdapter = EditBadgeRVAdapter(obtainedBadgeList, this, addPossible)
        binding.rvEditBadge.apply {
            adapter = editBadgeRVAdapter
            layoutManager = GridLayoutManager(this@EditProfileActivity, 3)
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

    private fun requestPermissions(onCalled: () -> Unit) {
        val permissions = mutableListOf<String>()
        // 카메라 권한 추가
        permissions.add(android.Manifest.permission.CAMERA)

        // Android 13(API 33) 이상에서는 READ_MEDIA_IMAGES 권한 추가
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(android.Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            // Android 13 미만에서는 READ_EXTERNAL_STORAGE 권한 추가
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        // 허용되지 않은 권한 필터링
        val deniedPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isEmpty()) {
            // 모든 권한이 이미 허용된 경우
            onCalled()     // 호출한 함수 실행
        } else {
            // 최초 요청이거나, 이전에 거부되었더라도 일단 권한 요청 실행
            requestPermissionsLauncher.launch(deniedPermissions.toTypedArray())
        }
    }

//    private fun createPermissionsLauncher(onCalled: () -> Unit) =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            // 모든 권한이 허용되었는지 확인
//            val deniedPermissions = permissions.filter { !it.value }.keys
//            val allGranted = deniedPermissions.isEmpty()
//
//            if (allGranted) {
//                Log.d("PermissionDebug", "모든 권한 요청 성공")
//                onCalled() // 전달된 콜백 실행
//            } else {
//                // 각 권한이 완전히 거부되었는지 확인
//                var permanentlyDenied = false
//                deniedPermissions.forEach { permission ->
//                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//                        permanentlyDenied = true
//                        Log.e("PermissionDebug", "권한 요청이 완전히 거부됨: $permission")
//                    }
//                }
//                if (permanentlyDenied) {
//                    Toast.makeText(this, "권한이 필요합니다. 설정에서 변경해주세요.", Toast.LENGTH_SHORT).show()
//                    navigateToAppSettings()
//                } else {
//                    Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
//                    // 필요 시 다시 요청 가능
//                }
//            }
//        }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // 모든 권한이 허용되었는지 확인
            val deniedPermissions = permissions.filter { !it.value }.keys
            val allGranted = deniedPermissions.isEmpty()

            if (allGranted) {
                Log.d("PermissionDebug", "모든 권한 요청 성공")
                onCalled?.invoke() // 호출된 함수 콜백 실행
            } else {
                // 각 권한이 완전히 거부되었는지 확인
                var permanentlyDenied = false
                deniedPermissions.forEach { permission ->
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        permanentlyDenied = true
                        Log.e("PermissionDebug", "권한 요청이 완전히 거부됨: $permission")
                    }
                }
                if (permanentlyDenied) {
                    Toast.makeText(this, "권한이 필요합니다. 설정에서 변경해주세요.", Toast.LENGTH_SHORT).show()
                    navigateToAppSettings()
                } else {
                    Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                    // 필요 시 다시 요청 가능
                }
            }
        }

    // 설정으로 이동해 권한 허용을 유도
    private fun navigateToAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }


    // 카메라 촬영 결과를 받는 ActivityResultLauncher (TakePicture - Boolean 반환)
    private val cameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success && cameraPhotoUri != null) {
            // 촬영에 성공하면 Glide를 사용해 저장된 이미지 URI를 로드, 원형 처리 적용
            Glide.with(this)
                .load(cameraPhotoUri)
                .circleCrop()
                .into(userImg)
            Log.d("CameraDebug", "촬영한 사진 로드 성공: $cameraPhotoUri")
        } else {
            Log.e("CameraDebug", "사진 촬영 실패 또는 취소됨")
            Toast.makeText(this, "사진 촬영에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 카메라 촬영 실행 (TakePicture 방식 사용 - URI 방식)
    private fun openCamera() {
        try {
            // 임시 파일 생성 및 URI 획득
            val photoFile = createImageFile()
            cameraPhotoUri = FileProvider.getUriForFile(
                this,
                "com.example.hrr_android.fileprovider", // Manifest에 설정한 authority
                photoFile
            )
            cameraPhotoUri?.let{uri ->
                cameraResultLauncher.launch(uri)
            }

        } catch (e: Exception) {
            Log.e("CameraDebug", "카메라 열기 중 예외 발생: ${e.message}", e)
            Toast.makeText(this, "카메라를 열 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 임시 이미지 파일 생성
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)     // 파일명에 타임스탬프 적용
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
                .into(userImg)
            Log.d("GalleryDebug", "이미지 로드 성공: $uri")
        } catch (e: Exception) {
            Log.e("GalleryDebug", "이미지 로드 중 예외 발생: ${e.message}", e)
            Toast.makeText(this, "이미지를 로드할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBadgeClick(badge: Badge) {
        if(badge.isSelected){
            // 대표 뱃지를 선택한 경우, 대표 리스트에서 제거
            selectedBadgeList = selectedBadgeList.filterNot { it.first == badge.name }.toMutableList()
            badge.isSelected = false
        }
        else{
            // 새로 선택됐을 경우, 대표 리스트에 추가
            if(selectedBadgeList.size < 3){
                // 뱃지 개수 3개로 제한
                selectedBadgeList.add(badge.name to badge.icon)
                badge.isSelected = true
            }
            else{
                Toast.makeText(this, "뱃지는 최대 3개 선택 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
        setSelectedBadges(selectedBadgeList, binding)   // 뷰 업데이트
        editBadgeRVAdapter.updateAddPossible(addPossible)
    }

    // 대표 뱃지 바인딩
    private fun setSelectedBadges(selectedBadgeList: MutableList<Pair<String, Int>>, binding: ActivityEditProfileBinding){
        //설정한 대표 뱃지 개수에 따라 visibility 조정
        when(selectedBadgeList.size){
            //first: 이름, second: 아이콘 ID
            0 -> {
                binding.llEditBadge02.visibility = View.GONE
                binding.llEditBadge03.visibility = View.GONE

                binding.ivEditBadge01.setImageResource(R.drawable.ic_profile_default)
                binding.tvEditBadge01.text = "뱃지명"

                addPossible = true
            }
            1 -> {
                binding.llEditBadge01.visibility = View.VISIBLE
                binding.llEditBadge02.visibility = View.GONE
                binding.llEditBadge03.visibility = View.GONE

                binding.ivEditBadge01.setImageResource(selectedBadgeList[0].second)
                binding.tvEditBadge01.text = selectedBadgeList[0].first

                addPossible = true
            }
            2 -> {
                binding.llEditBadge01.visibility = View.VISIBLE
                binding.llEditBadge02.visibility = View.VISIBLE
                binding.llEditBadge03.visibility = View.GONE

                binding.ivEditBadge01.setImageResource(selectedBadgeList[0].second)
                binding.tvEditBadge01.text = selectedBadgeList[0].first

                binding.ivEditBadge02.setImageResource(selectedBadgeList[1].second)
                binding.tvEditBadge02.text = selectedBadgeList[1].first

                addPossible = true
            }
            3 -> {
                binding.llEditBadge01.visibility = View.VISIBLE
                binding.llEditBadge02.visibility = View.VISIBLE
                binding.llEditBadge03.visibility = View.VISIBLE

                binding.ivEditBadge01.setImageResource(selectedBadgeList[0].second)
                binding.tvEditBadge01.text = selectedBadgeList[0].first

                binding.ivEditBadge02.setImageResource(selectedBadgeList[1].second)
                binding.tvEditBadge02.text = selectedBadgeList[1].first

                binding.ivEditBadge03.setImageResource(selectedBadgeList[2].second)
                binding.tvEditBadge03.text = selectedBadgeList[2].first

                addPossible = false
            }
        }

    }


}