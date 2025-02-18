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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentProfileBadgeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileBadgeFragment : Fragment() {
    private var _binding: FragmentProfileBadgeBinding? = null       //뷰 바인딩
    private val binding get() = _binding!!
    private var typeBadgeList = ArrayList<Badge>()                  //유형 뱃지 리스트
    private var categoryBadgeList = ArrayList<Badge>()              //카테고리 뱃지 리스트
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBadgeBinding.inflate(inflater, container, false)

        //유형 뱃지 RecyclerView 연결
        val typeBadgeRVAdapter = ProfileBadgeRVAdapter(typeBadgeList)
        binding.rvProfileBadgeType.adapter = typeBadgeRVAdapter
        binding.rvProfileBadgeType.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //카테고리 뱃지 RecyclerView 연결
        val categoryBadgeRVAdapter = ProfileBadgeRVAdapter(categoryBadgeList)
        binding.rvProfileBadgeCategory.adapter = categoryBadgeRVAdapter
        binding.rvProfileBadgeCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        /*
        * 획득한 뱃지 로딩
        * */
        userViewModel.badges.observe(viewLifecycleOwner) { response->
            typeBadgeList.clear()
            typeBadgeList.addAll(
                response?.typeBadges?.filter { it.isObtained }
                    ?.map { badge ->
                        Badge(
                            name = badge.name,
                            icon = ValidUtils.getDrawableResId(requireContext(), badge.icon)
                        )
                    } ?: emptyList()
            )

            categoryBadgeList.clear()
            categoryBadgeList.addAll(
                response?.categoryBadges?.filter { it.isObtained }
                    ?.map { badge ->
                        Badge(
                            name = badge.name,
                            icon = ValidUtils.getDrawableResId(requireContext(), badge.icon)
                        )
                    } ?: emptyList()
            )

            binding.rvProfileBadgeType.adapter?.notifyDataSetChanged()
            binding.rvProfileBadgeCategory.adapter?.notifyDataSetChanged()


            //데이터 유무 판단하여 뷰 전환
            setBadgeVisibility()
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
                Log.e("ProfileFragmentVM", "오류 발생: $errorMsg")
            }
        }
        userViewModel.loadBadges()  // 유저 데이터 로드

        //더보기 클릭 처리 구현
        binding.llProfileBadgeTitle.setOnClickListener{
            val intent = Intent(requireContext(), ProfileMoreActivity::class.java)
            intent.putExtra("type", "badge")
            startActivity(intent)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setBadgeVisibility() {
        Log.d("mybadgeDebug", "typeBadgeList: $typeBadgeList")
        Log.d("mybadgeDebug", "categoryBadgeList: $categoryBadgeList")
//        if (typeBadgeList.size != 0 || categoryBadgeList.size != 0) {
//            //뱃지 획득 시
//            binding.clProfileBadgeContentNo.visibility = View.GONE
//            binding.clProfileBadgeMy.visibility = View.VISIBLE
//
//            if (typeBadgeList.size != 0 && categoryBadgeList.size != 0) {
//                //유형, 카테고리 뱃지 모두 획득 시
//                binding.rvProfileBadgeType.visibility = View.VISIBLE
//                binding.rvProfileBadgeCategory.visibility = View.VISIBLE
//                binding.clProfileBadgeTypeNo.visibility = View.GONE
//                binding.clProfileBadgeCategoryNo.visibility = View.GONE
//            } else if (typeBadgeList.size != 0 && categoryBadgeList.size == 0) {
//                //유형 뱃지 획득 시
//                binding.rvProfileBadgeType.visibility = View.VISIBLE
//                binding.clProfileBadgeTypeNo.visibility = View.GONE
//            } else if (typeBadgeList.size == 0 && categoryBadgeList.size != 0) {
//                //카테고리 뱃지 획득 시
//                binding.rvProfileBadgeCategory.visibility = View.VISIBLE
//                binding.clProfileBadgeCategoryNo.visibility = View.GONE
//            }
//        }
        if (typeBadgeList.isNotEmpty() || categoryBadgeList.isNotEmpty()) {
            // 뱃지 획득 시
            binding.clProfileBadgeContentNo.visibility = View.GONE
            binding.clProfileBadgeMy.visibility = View.VISIBLE

            // ✅ 유형과 카테고리 뱃지를 각각 체크하여 visibility 설정
            if (typeBadgeList.isNotEmpty()) {
                binding.rvProfileBadgeType.visibility = View.VISIBLE
                binding.clProfileBadgeTypeNo.visibility = View.GONE
            } else {
                binding.rvProfileBadgeType.visibility = View.GONE
                binding.clProfileBadgeTypeNo.visibility = View.VISIBLE
            }

            if (categoryBadgeList.isNotEmpty()) {
                binding.rvProfileBadgeCategory.visibility = View.VISIBLE
                binding.clProfileBadgeCategoryNo.visibility = View.GONE
            } else {
                binding.rvProfileBadgeCategory.visibility = View.GONE
                binding.clProfileBadgeCategoryNo.visibility = View.VISIBLE
            }
        } else {
            // ✅ 뱃지가 하나도 없는 경우
            binding.clProfileBadgeContentNo.visibility = View.VISIBLE
            binding.clProfileBadgeMy.visibility = View.GONE
        }

    }

}