package com.example.hrr_android.challenge.certification.ui.photo

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrr_android.R
import com.example.hrr_android.databinding.CustomSnackbarBinding
import com.example.hrr_android.databinding.FragmentPhotoCertificationBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class PhotoCertificationFragment : Fragment() {
    private var _binding: FragmentPhotoCertificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PhotoCertificationViewModel by viewModels()
    private var uploadedPhotoUrl: String? = null

    private val CAMERA_PERMISSION = Manifest.permission.CAMERA
    private var imageUri: Uri? = null
    private var isPreviewMode = false

    // UI 입력값 상태 관리
    private var hasTitle = false
    private var hasContent = false
    private var hasPhoto = false
    private val MAX_CONTENT_LENGTH = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoCertificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInitialState()
        initClickListeners()
        setupTextWatchers()
        checkCameraPermission()
        setupObservers()
    }

    // 화면 초기 상태 설정
    private fun setupInitialState() {
        with(binding) {
            tvCertificationComplete.visibility = View.GONE // 완료 버튼 숨기기
            viewFlipper.displayedChild = 0 // ViewFlipper 첫 번째 화면(사진 촬영/프리뷰)으로 설정
        }
    }

    // 버튼 클릭 이벤트 설정
    private fun initClickListeners() {
        with(binding) {
            // 뒤로가기
            btnCertificationBack.setOnClickListener {
                if (isPreviewMode) {
                    setupInitialState()
                    openCamera()
                    isPreviewMode = false
                } else {
                    requireActivity().onBackPressed()
                }
            }
            // 다시 찍기
            btnPhotoRetake.setOnClickListener {
                setupInitialState()
                openCamera()
                isPreviewMode = false
            }
            // 인증하기
            btnPhotoConfirm.setOnClickListener {
                showEditMode()
            }
            // 완료 버튼
            tvCertificationComplete.setOnClickListener {
                if (!tvCertificationComplete.isEnabled) return@setOnClickListener
                showLoadingAndNavigate()
            }
        }
    }

    // EditText 입력값 변경 감지
    private fun setupTextWatchers() {
        // 제목 입력 감지
        binding.viewFlipper.findViewById<EditText>(R.id.et_certification_title)
            .addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    hasTitle = !s.isNullOrEmpty()
                    updateCompleteButton()
                }
            })

        // 내용 입력 감지 및 글자수 제한
        binding.viewFlipper.findViewById<EditText>(R.id.et_certification_content)
            .addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val currentLength = s?.length ?: 0
                    if (currentLength > MAX_CONTENT_LENGTH) {
                        // 200자 초과 입력 방지
                        val editText = binding.viewFlipper.findViewById<EditText>(R.id.et_certification_content)
                        editText.setText(s?.subSequence(0, MAX_CONTENT_LENGTH))
                        editText.setSelection(MAX_CONTENT_LENGTH)

                        // Custom Snackbar 표시
                        showCustomSnackbar(binding.root, "200자 이내로 작성해주세요!")
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    hasContent = !s.isNullOrEmpty()
                    updateCompleteButton()
                }
            })
    }

    private fun updateCompleteButton() {
        val isValid = hasTitle && hasContent && hasPhoto
        binding.tvCertificationComplete.apply {
            isEnabled = isValid
            setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isValid) R.color.text_primary else R.color.grey_500
                )
            )
        }
    }

    // Custom Snackbar
    private fun showCustomSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view as ViewGroup
        val context = snackbarView.context

        // 기존 Snackbar 텍스트 숨기기
        val defaultTextView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        defaultTextView.visibility = View.INVISIBLE

        // 스낵바 커스텀
        val binding = CustomSnackbarBinding.inflate(LayoutInflater.from(context), snackbarView, false)
        binding.tvSnackbarContent.text = message
        snackbarView.setBackgroundColor(Color.TRANSPARENT)
        snackbarView.setPadding(0, 0, 0, 0)
        snackbarView.addView(binding.root)

        snackbar.show()
    }

    // 카메라 권한 요청
    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                CAMERA_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                requestPermissionLauncher.launch(CAMERA_PERMISSION)
            }
        }
    }

    // 권한 요청 처리
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                showCustomSnackbar(
                    binding.root,
                    "카메라 권한이 필요합니다. 설정에서 권한을 허용해주세요."
                )
                requireActivity().onBackPressed()
            }
        }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    // 비트맵 이미지에 타임스탬프 추가
    private fun addTimestampToBitmap(originalBitmap: Bitmap): Bitmap {
        val resultBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(resultBitmap)

        // sp를 px로 변환
        val scale = resources.displayMetrics.scaledDensity
        val textSizeInPx = 7 * scale

        // 날짜, 시간 포맷팅
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val meridiem = if (SimpleDateFormat("HH", Locale.getDefault()).format(Date()).toInt() < 12) "AM" else "PM"
        val timeWithMeridiem = "$meridiem $time"

        // 폰트 로드
        val customTypeface = ResourcesCompat.getFont(requireContext(), R.font.noto_sans_kr_medium)

        // Paint 객체 설정
        val paint = Paint().apply {
            color = Color.WHITE
            textSize = textSizeInPx
            typeface = customTypeface
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        // 위치 계산
        val leftPadding = 17f
        val bottomPadding = 16f
        val lineSpacing = 3f // 줄 간격

        // 텍스트 위치 계산
        val xPos = leftPadding
        val dateYPos = canvas.height - bottomPadding - paint.textSize - lineSpacing
        val timeYPos = canvas.height - bottomPadding

        // 텍스트 그리기
        canvas.drawText(date, xPos, dateYPos, paint)
        canvas.drawText(timeWithMeridiem, xPos, timeYPos, paint)

        return resultBitmap
    }

    // 촬영한 사진 결과 처리
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val originalBitmap = result.data?.extras?.get("data") as Bitmap
            val timestampedBitmap = addTimestampToBitmap(originalBitmap)
            binding.ivPhotoPreview.setImageBitmap(timestampedBitmap)
            hasPhoto = true
            showPreviewMode()
            updateCompleteButton()

            // 촬영한 사진 서버에 업로드
            viewModel.uploadPhoto(timestampedBitmap, requireContext())
        }
    }

    // 사진 프리뷰 모드
    private fun showPreviewMode() {
        isPreviewMode = true
        with(binding) {
            tvCertificationComplete.visibility = View.GONE
            viewFlipper.displayedChild = 0
        }
    }

    // 글 작성 모드
    private fun showEditMode() {
        binding.tvCertificationComplete.visibility = View.VISIBLE

        // 체크박스 상태 동기화
        binding.viewFlipper.findViewById<CheckBox>(R.id.cb_certification_question).isChecked =
            binding.cbPhotoConfirmQuestion.isChecked

        // 타임스탬프가 포함된 이미지를 프리뷰로 설정
        val bitmap = (binding.ivPhotoPreview.drawable as BitmapDrawable).bitmap
        binding.viewFlipper.findViewById<ImageView>(R.id.iv_certification_image).setImageBitmap(bitmap)

        // ViewFlipper 두 번째 화면으로 전환
        binding.viewFlipper.displayedChild = 1
    }

    // 로딩 화면 표시
    private fun showLoadingAndNavigate() {
        val loadingDialog = AlertDialog.Builder(requireContext())
            .setView(R.layout.activity_loading)
            .setCancelable(false)
            .create()

        loadingDialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }

        loadingDialog.show()

        // 인증 정보 업로드
        val challengeId = arguments?.getInt("challenge_id") ?: -1
        uploadedPhotoUrl?.let { photoUrl ->
            viewModel.uploadVerification(
                challengeId = challengeId,
                photoUrl = photoUrl,
                title = binding.viewFlipper.findViewById<EditText>(R.id.et_certification_title).text.toString(),
                content = binding.viewFlipper.findViewById<EditText>(R.id.et_certification_content).text.toString(),
                isQuestion = binding.viewFlipper.findViewById<CheckBox>(R.id.cb_certification_question).isChecked
            )
        } ?: run {
            loadingDialog.dismiss()
            showCustomSnackbar(binding.root, "이미지 업로드를 먼저 완료해주세요.")
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.photoUploadState.collect { result ->
                result?.onSuccess { url ->
                    uploadedPhotoUrl = url  // 업로드된 URL 저장
                }?.onFailure { e ->
                    showCustomSnackbar(binding.root, e.message ?: "이미지 업로드에 실패했습니다.")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.verificationState.collect { result ->
                result?.onSuccess { response ->
                    findNavController().navigate(
                        R.id.action_photoCertificationFragment_to_postFragment,
                        Bundle().apply {
                            putInt("verification_id", response.verification.verificationId)
                        }
                    )
                }?.onFailure { e ->
                    showCustomSnackbar(binding.root, e.message ?: "인증 업로드에 실패했습니다.")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}