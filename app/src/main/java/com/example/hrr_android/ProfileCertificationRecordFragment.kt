package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentProfileCertificationRecordBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class ProfileCertificationRecordFragment : Fragment() {
    private var _binding: FragmentProfileCertificationRecordBinding? = null     //뷰 바인딩
    private val binding get() = _binding!!
    private var certificationList = ArrayList<Certification>()                  //인증 기록
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileCertificationRecordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 인증 기록 더미 데이터 - 테스트 시 주석 해제 or 설정
        certificationList.apply {
            add(Certification("챌린지명", "게시글 제목", "2024.12.31", R.drawable.img_running, true))
            add(Certification("Run To You", "마지막 인증합니다~^^", "2024.12.31", R.drawable.img_running, true))
            add(Certification("사진 없을 경우", "게시글 제목", "2025.01.01", hasLink = false))
            add(Certification("링크 없을 경우", "1년 만에 인증합니다~^^", "2025.01.01", R.drawable.img_running, false))
            add(Certification("인증 제목 없을 경우", "", "2025.01.01", R.drawable.img_running, false))
        }

        //데이터 유무 판단하여 뷰 전환
        if(certificationList.size != 0){
            //인증 기록 존재
            binding.clProfileCertificationRecordContentNo.visibility = View.GONE
            binding.rvProfileCertificationRecored.visibility = View.VISIBLE
        }

        //인증 기록 RecyclerView 연결
        val profileCertificationRVAdapter = ProfileCertificationRVAdapter(certificationList)
        binding.rvProfileCertificationRecored.adapter = profileCertificationRVAdapter
        binding.rvProfileCertificationRecored.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        //더보기 버튼 클릭 처리
        binding.llProfileCertificationRecordTitle.setOnClickListener{
            val intent = Intent(requireContext(), ProfileMoreActivity::class.java)
            intent.putExtra("type", "certification")
            startActivity(intent)
        }

        // LiveData 관찰 (데이터가 변경될 때 자동 업데이트 되도록 설정)
        userViewModel.history.observe(viewLifecycleOwner) { history ->
            history?.let {
                val hasLink = (it.textUrl != null)  // 링크 주소가 있다면 true
                certificationList.add(Certification(it.name, it.title, dateFormat(it.createTime), R.drawable.img_running, hasLink))
                //Todo: 이미지 처리 추가 예정
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


        // 유저 데이터 로드
        userViewModel.getChallengeHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun dateFormat(original: String): String {
        val instant = Instant.parse(original)
        val new = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            .withZone(ZoneId.of("UTC"))
        return new.format(instant) // 변환된 날짜 문자열 반환
    }

}