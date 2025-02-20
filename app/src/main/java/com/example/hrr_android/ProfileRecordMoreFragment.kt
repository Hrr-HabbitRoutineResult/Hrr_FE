package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentProfileChallengeMoreBinding
import com.example.hrr_android.databinding.FragmentProfileRecordMoreBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ProfileRecordMoreFragment : Fragment(), OnRecordClickListener {
    private var _binding: FragmentProfileRecordMoreBinding? = null  //뷰 바인딩
    private val binding get() = _binding!!
    private var certifications = ArrayList<Certification>() //기록 리스트
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileRecordMoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter 연결
        val profileRecordMoreRVAdapter = ProfileRecordMoreRVAdapter(certifications, this)
        binding.rvRecordMore.apply {
            adapter = profileRecordMoreRVAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        // LiveData 관찰 (데이터가 변경될 때 자동 업데이트 되도록 설정)
        userViewModel.history.observe(viewLifecycleOwner) { historyList ->
            historyList?.map { history ->
                Certification(
                    history.name,
                    history.title,
                    dateFormat(history.createTime),
                    R.drawable.img_running,
                    history.textUrl != null, // 링크 주소가 있다면 true
                    verificationId = history.verificationId
                    //Todo: 이미지 처리 추가 예정
                )
            }?.let {
                certifications.clear()
                certifications.addAll(it)
                binding.rvRecordMore.adapter?.notifyDataSetChanged()
            }
        }

        userViewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            errorMsg?.let {
                val errorToUser = when {
                    it.contains("IllegalStateException") -> "데이터를 불러오는 중 문제가 발생했습니다. 다시 시도해 주세요."
                    it.contains("JsonSyntaxException") -> "서버 응답이 올바르지 않습니다. 업데이트를 확인해 주세요."
                    it.contains("SocketTimeoutException") -> "서버 응답이 지연되고 있습니다. 잠시 후 다시 시도해 주세요."
                    it.contains("IOException") -> "네트워크 연결을 확인해 주세요."
                    else -> "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해 주세요."
                }

                Toast.makeText(requireContext(), errorToUser, Toast.LENGTH_LONG).show()
            }
        }

        // 유저 데이터 로드 - 프래그먼트 최초 진입 시 한번만 호출
        if (savedInstanceState == null) {
            userViewModel.getChallengeHistory()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dateFormat(original: String): String {
        val instant = Instant.parse(original)
        val new = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            .withZone(ZoneId.of("UTC"))
        return new.format(instant) // 변환된 날짜 문자열 반환
    }

    override fun onRecordClicked(certification: Certification) {
        val bundle = Bundle().apply {
            putInt("verification_id", certification.verificationId)
        }
        findNavController().navigate(R.id.action_profileRecordMoreFragment_to_postFragment, bundle)
    }


}