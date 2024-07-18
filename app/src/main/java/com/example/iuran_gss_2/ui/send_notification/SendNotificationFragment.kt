package com.example.iuran_gss_2.ui.send_notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iuran_gss_2.R
import com.example.iuran_gss_2.databinding.FragmentSendNotificationBinding


class SendNotificationFragment : Fragment() {
    private lateinit var binding : FragmentSendNotificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

}