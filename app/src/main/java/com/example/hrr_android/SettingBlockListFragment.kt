package com.example.hrr_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hrr_android.databinding.FragmentSettingBlockListBinding

class SettingBlockListFragment : Fragment() {
    private var _binding: FragmentSettingBlockListBinding? = null
    private val binding get() = _binding!!
    private var tempList = ArrayList<TempUser>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBlockListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (parentFragmentManager.backStackEntryCount >= 1) {
                        parentFragmentManager.popBackStack() // 추가 Fragment가 있을 때느 이전 Fragment로 돌아감

                        // 가장 최근에 추가된 Fragment를 다시 보이도록 설정
                        val currentFragment = parentFragmentManager.fragments.lastOrNull()
                        currentFragment?.let {
                            parentFragmentManager.beginTransaction().show(it).commit()
                        }
                    }

                    (activity as? ProfileMoreActivity)?.setTitle("설정")
                }
            })

        //팔로워 더미 데이터
        tempList.apply {
            add(TempUser("김흐르", "실버", R.drawable.ic_profile_default, true))
            add(TempUser("김흐르", "실버", R.drawable.ic_profile_default, true))
            add(TempUser("김흐르", "실버", R.drawable.ic_profile_default, true))
            add(TempUser("김흐르", "실버", R.drawable.ic_profile_default, true))
            add(TempUser("조흐르", "실버", R.drawable.ic_profile_default, true))
            add(TempUser("이흐르", "실버", R.drawable.ic_profile_default, true))
            add(TempUser("김흐르", "실버", R.drawable.ic_profile_default, true))
            add(TempUser("김흐르", "실버", R.drawable.ic_profile_default, true))
            add(TempUser("김흐르", "실버", R.drawable.ic_profile_default, true))
            add(TempUser("김흐르", "실버", R.drawable.ic_profile_default, true))
        }

        val blockRVAdapter = BlockRVAdapter(tempList)
        binding.rvBlocklist.apply {
            adapter = blockRVAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? ProfileMoreActivity)?.setTitle("설정")
        _binding = null
    }
}