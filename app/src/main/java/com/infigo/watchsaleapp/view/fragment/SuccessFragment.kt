package com.infigo.watchsaleapp.view.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.infigo.watchsaleapp.databinding.FragmentSuccessBinding


class SuccessFragment : Fragment() {

    lateinit var binding: FragmentSuccessBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccessBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.gotoHome.setOnClickListener {
            findNavController().navigate(SuccessFragmentDirections.actionSuccessFragmentToHome())
        }
    }
}