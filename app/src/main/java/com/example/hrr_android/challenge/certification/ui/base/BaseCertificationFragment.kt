package com.example.hrr_android.challenge.certification.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


abstract class BaseCertificationFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCommonViews()
        initCertificationView()
    }

    protected abstract fun initCommonViews()
    protected abstract fun initCertificationView()
    protected abstract fun handleSubmit()
    protected abstract fun validateInput(): Boolean

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

