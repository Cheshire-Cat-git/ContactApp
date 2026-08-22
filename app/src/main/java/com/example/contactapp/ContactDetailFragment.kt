package com.example.contactapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.contactapp.databinding.FragmentContactDetailBinding

class ContactDetailFragment : Fragment(R.layout.fragment_contact_detail) {
    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_NAME = "arg_name"
        private const val ARG_PHONE = "arg_phone"
        private const val ARG_PHOTO = "arg_photo"
        private const val ARG_EMAIL = "arg_email"
        private const val ARG_ADDRESS = "arg_address"

        fun newInstance(name: String, phone: String, photoUri: String?, email: String?, address: String?) =
            ContactDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                    putString(ARG_PHONE, phone)
                    putString(ARG_PHOTO, photoUri)
                    putString(ARG_EMAIL, email)
                    putString(ARG_ADDRESS, address)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentContactDetailBinding.bind(view)

        val name = arguments?.getString(ARG_NAME) ?: ""
        val phone = arguments?.getString(ARG_PHONE) ?: ""
        val photoUri = arguments?.getString(ARG_PHOTO)

        binding.tvDetailName.text = name
        binding.tvDetailPhone.text = phone
        
        if (photoUri != null) {
            binding.ivDetailPhoto.setImageURI(photoUri.toUri())
        } else {
            binding.ivDetailPhoto.setImageResource(R.drawable.ic_profile_blank)
        }
        listOf(binding.tvDetailPhone, binding.tvPhoneHome, binding.tvPhoneWork).forEach { textView ->
            textView.setOnClickListener { 
                val number = textView.text.toString()
                if (number.isNotEmpty()) dialPhoneNumber(number)
            }
        }

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dialPhoneNumber(phoneNumber: String?) {
        if (phoneNumber.isNullOrBlank()) return
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
        }
        startActivity(intent)
    }
}
