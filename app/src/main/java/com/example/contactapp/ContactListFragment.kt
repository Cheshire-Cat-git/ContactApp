package com.example.contactapp

import android.Manifest.permission.READ_CONTACTS
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.data.Contact
import com.example.contactapp.data.ContactAdapter
import com.example.contactapp.databinding.FragmentContactListBinding

class ContactListFragment : Fragment(R.layout.fragment_contact_list) {
    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) loadContacts()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentContactListBinding.bind(view)
        requestPermissionLauncher.launch(READ_CONTACTS)
    }

    private fun loadContacts() {
        val contactList = mutableListOf<Contact>()
        val contentResolver = requireContext().contentResolver
        
        val cursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.MIMETYPE} IN (?, ?, ?)",
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE
            ),
            ContactsContract.Data.CONTACT_ID
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.Data.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.Data.DISPLAY_NAME)
            val mimeIndex = it.getColumnIndex(ContactsContract.Data.MIMETYPE)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val emailIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
            val addressIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS)
            val photoIndex = it.getColumnIndex(ContactsContract.Data.PHOTO_THUMBNAIL_URI)

            val contactsMap = LinkedHashMap<Long, Contact>()

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val mimeType = it.getString(mimeIndex)
                
                val contact = contactsMap.getOrPut(id) {
                    Contact(
                        name = it.getString(nameIndex) ?: "Unknown",
                        phoneNumber = "",
                        photoUri = it.getString(photoIndex),
                        email = null,
                        address = null
                    )
                }

                when (mimeType) {
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                        contactsMap[id] = contact.copy(phoneNumber = it.getString(phoneIndex) ?: "")
                    }
                    ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> {
                        contactsMap[id] = contact.copy(email = it.getString(emailIndex))
                    }
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE -> {
                        contactsMap[id] = contact.copy(address = it.getString(addressIndex))
                    }
                }
            }
            contactList.addAll(contactsMap.values)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = ContactAdapter(contactList) { contact ->
            val detailFragment = ContactDetailFragment.newInstance(
                contact.name,
                contact.phoneNumber,
                contact.photoUri,
                contact.email,
                contact.address
            )
            
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
