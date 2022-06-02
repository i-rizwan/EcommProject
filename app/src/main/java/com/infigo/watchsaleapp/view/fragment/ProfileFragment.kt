package com.infigo.watchsaleapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.infigo.watchsaleapp.databinding.FragmentProfileBinding
import com.infigo.watchsaleapp.utils.SharedPreferenceManager

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferenceManager: SharedPreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferenceManager = SharedPreferenceManager(requireActivity())
        val userData: Array<String> = sharedPreferenceManager.getUserData()
        binding.apply {
            name.text = userData[0]
            email.text = userData[1]
            mobile.text = userData[2]
        }

    }

}