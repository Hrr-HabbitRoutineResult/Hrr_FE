package com.example.hrr_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
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
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileCertificationRecordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        userViewModel.history.observe(viewLifecycleOwner) { historyList ->
            historyList?.map { history ->
                Certification(
                    history.name,
                    history.title,
                    dateFormat(history.createTime),
                    R.drawable.img_running,
                    history.textUrl != null // 링크 주소가 있다면 true
                    //Todo: 이미지 처리 추가 예정
                )
            }?.let {
                certificationList.clear()
                certificationList.addAll(it)
                binding.rvProfileCertificationRecored.adapter?.notifyDataSetChanged()

                if (certificationList.isNotEmpty()) {
                    binding.clProfileCertificationRecordContentNo.visibility = View.GONE
                    binding.rvProfileCertificationRecored.visibility = View.VISIBLE
                } else {
                    binding.clProfileCertificationRecordContentNo.visibility = View.VISIBLE
                    binding.rvProfileCertificationRecored.visibility = View.GONE
                }
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
        Log.d("ProfileCertDebug", "onDestroyView() called")

        _binding = null
    }

    private fun dateFormat(original: String): String {
        val instant = Instant.parse(original)
        val new = DateTimeFormatter.ofPattern("yyyy.MM.dd")
            .withZone(ZoneId.of("UTC"))
        return new.format(instant) // 변환된 날짜 문자열 반환
    }

}