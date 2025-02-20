package com.example.hrr_android.challenge.certification.ui.text


import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hrr_android.R
import com.example.hrr_android.databinding.CustomSnackbarBinding
import com.example.hrr_android.databinding.FragmentTextCertificationBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TextCertificationFragment : Fragment() {
    private var _binding: FragmentTextCertificationBinding? = null
    private val binding get() = _binding!!

    // UI 입력값 상태 관리
    private var hasTitle = false
    private var hasContent = false
    private var hasLink = false
    private val MAX_CONTENT_LENGTH = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextCertificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTextWatchers()
        initViews()

        // 키보드 설정
        requireActivity().window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
        )

        // 임시저장 목록으로 이동
        binding.tvCertificationTempCount.setOnClickListener {
            findNavController().navigate(R.id.action_textCertificationFragment_to_draftListFragment)
        }
    }

    // 갤러리 이미지 선택 결과 처리
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedImageUri ->
            binding.ivCertificationImage.apply {
                visibility = View.VISIBLE
                setImageURI(selectedImageUri)
            }
        }
    }

    private fun initViews() {
        with(binding) {
            // 임시저장 목록으로 이동
            tvCertificationTempCount.setOnClickListener {
                findNavController().navigate(R.id.action_textCertificationFragment_to_draftListFragment)
            }

            // 완료 버튼
            tvCertificationComplete.setOnClickListener {
                if (validateInput()) {
                    showLoadingAndNavigate()
                }
            }

            // 뒤로가기
            btnCertificationBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            // 이미지 추가
            btnCertificationImage.setOnClickListener {
                openGallery()
            }

            // 링크 추가
            btnCertificationLink.setOnClickListener {
                showLinkDialog()
            }
        }
    }

    private fun validateInput(): Boolean {
        return hasTitle && hasContent && hasLink &&
                binding.etCertificationContent.text.length <= MAX_CONTENT_LENGTH
    }

    // EditText 입력값 변경 감지
    private fun setupTextWatchers() {
        // 제목 입력 감지
        binding.etCertificationTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                hasTitle = !s.isNullOrEmpty()
                updateCompleteButton()
            }
        })

        // 내용 입력 감지 및 글자수 제한
        binding.etCertificationContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentLength = s?.length ?: 0
                if (currentLength > MAX_CONTENT_LENGTH) {
                    // 200자 초과 입력 방지
                    binding.etCertificationContent.setText(s?.subSequence(0, MAX_CONTENT_LENGTH))
                    binding.etCertificationContent.setSelection(MAX_CONTENT_LENGTH)

                    // Custom Snackbar 표시
                    showCustomSnackbar(binding.root)
                }
            }
            override fun afterTextChanged(s: Editable?) {
                hasContent = !s.isNullOrEmpty()
                updateCompleteButton()
            }
        })
    }

    // 200자 초과 시 표시할 Custom Snackbar
    private fun showCustomSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)

        // Snackbar의 뷰 가져오기
        val snackbarView = snackbar.view as ViewGroup
        val context = snackbarView.context

        // 기존 Snackbar 텍스트 숨기기
        val defaultTextView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        defaultTextView.visibility = View.INVISIBLE

        // 스낵바 커스텀
        val binding = CustomSnackbarBinding.inflate(LayoutInflater.from(context), snackbarView, false)
        binding.tvSnackbarContent.text = "200자 이내로 작성해주세요!"
        snackbarView.setBackgroundColor(Color.TRANSPARENT)
        snackbarView.setPadding(0, 0, 0, 0)
        snackbarView.addView(binding.root)

        snackbar.show()
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

        // TODO: 개발용 2초 뒤 자동 화면 전환, 추후 수정 필요
        lifecycleScope.launch {
            delay(2000)
            if (isAdded) {
                loadingDialog.dismiss()
                findNavController().navigate(R.id.action_textCertificationFragment_to_postFragment)
            }
        }
    }

    private fun updateCompleteButton() {
        val isValid = hasTitle && hasContent && hasLink
        binding.tvCertificationComplete.apply {
            isEnabled = isValid
            setTextColor(if (isValid)
                ContextCompat.getColor(requireContext(), R.color.text_primary)
            else
                ContextCompat.getColor(requireContext(), R.color.grey_500)
            )
        }
    }

    private fun handleUrlInput(url: String) {
        with(binding) {
            // 링크 프리뷰 표시
            llCertificationPreview.visibility = View.VISIBLE
            tvCertificationLink.text = url
            hasLink = true
            updateCompleteButton()
        }
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    // URL 입력 다이얼로그 표시
    private fun showLinkDialog() {
        // 현재 키보드가 보이는 상태인지 확인
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val isKeyboardVisible = imm.isActive

        // 뒷배경 어둡게 처리
        val dimView = View(requireContext()).apply {
            setBackgroundColor(Color.parseColor("#80000000"))
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        (view as? ViewGroup)?.addView(dimView)

        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_link_input, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // 다이얼로그 설정
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // 키보드가 다이얼로그를 밀어올리도록 설정
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        dialog.setOnDismissListener {
            (view as? ViewGroup)?.removeView(dimView)
        }

        val urlEditText = dialogView.findViewById<EditText>(R.id.et_certification_url)

        // 다이얼로그가 표시된 후 키보드 상태 유지
        dialog.setOnShowListener {
            if (isKeyboardVisible) {
                urlEditText.requestFocus()
                imm.showSoftInput(urlEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        dialogView.findViewById<TextView>(R.id.btn_certification_confirm).setOnClickListener {
            val url = urlEditText.text.toString()
            if (url.isNotEmpty()) {
                handleUrlInput(url)
            }
            dialog.dismiss()
        }

        dialogView.findViewById<TextView>(R.id.btn_certification_cancle).setOnClickListener {
            dialog.cancel()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}