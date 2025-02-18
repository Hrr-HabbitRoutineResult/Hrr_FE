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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.hrr_android.access.ValidUtils
import com.example.hrr_android.databinding.FragmentProfileBadgeMoreBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileBadgeMoreFragment : Fragment(), OnBadgeMoreClickListener {
    //뷰 바인딩
    private var _binding: FragmentProfileBadgeMoreBinding? = null
    private val binding get() = _binding!!
    //뱃지 리스트
    private var typeBadgeList = ArrayList<Badge>()
    private var categoryBadgeList = ArrayList<Badge>()
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBadgeMoreBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //유형 뱃지 RecyclerView 연결
        val typeBadgeMoreRVAdapter = ProfileBadgeMoreRVAdapter(typeBadgeList, this)
        binding.rvBadgeMoreType.apply {
            adapter = typeBadgeMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        //카테고리 뱃지 RecyclerView 연결
        val categoryBadgeMoreRVAdapter = ProfileBadgeMoreRVAdapter(categoryBadgeList, this)
        binding.rvBadgeMoreCategory.apply {
            adapter = categoryBadgeMoreRVAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }

        /*
        * 획득한 뱃지 로딩
        * */
        userViewModel.badges.observe(viewLifecycleOwner) { response->
            typeBadgeList.clear()
            typeBadgeList.addAll(
                response?.typeBadges
                    ?.map { badge ->
                        Badge(
                            name = badge.name,
                            icon = ValidUtils.getDrawableResId(requireContext(), badge.icon),
                            isObtained = badge.isObtained
                        )
                    } ?: emptyList()
            )

            categoryBadgeList.clear()
            categoryBadgeList.addAll(
                response?.categoryBadges
                    ?.map { badge ->
                        Badge(
                            name = badge.name,
                            icon = ValidUtils.getDrawableResId(requireContext(), badge.icon),
                            isObtained = badge.isObtained
                        )
                    } ?: emptyList()
            )

            //데이터 유무 판단하여 뷰 전환
            binding.rvBadgeMoreType.adapter?.notifyDataSetChanged()
            binding.rvBadgeMoreCategory.adapter?.notifyDataSetChanged()

        }

        userViewModel.recentBadge.observe(viewLifecycleOwner) { response->
            response?.apply {
                binding.ivRecentIcon.setImageResource(ValidUtils.getDrawableResId(requireContext(),icon))
                binding.tvRecentName.text = name
                binding.tvRecentType.text = if(type=="type") "유형" else "카테고리"
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
                Log.e("ProfileFragmentVM", "오류 발생: $errorMsg")
            }
        }

        // 유저 데이터 로드
        userViewModel.loadBadges()
        userViewModel.getRecentBadge()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBadgeClick(badge: Badge) {
        // 새로운 Activity로 데이터 전달
        val gson = Gson()
        val badgeJson = gson.toJson(badge)

        val intent = Intent(requireContext(), MyBadgeDetailActivity::class.java).apply {
            putExtra("badgeJson", badgeJson)
        }
        startActivity(intent)

    }

}