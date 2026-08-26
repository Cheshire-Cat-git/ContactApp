package com.example.contactapp

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import com.example.contactapp.data.Contact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ContactViewModel(application: Application): AndroidViewModel(application) {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    fun loadContacts() {
        val contentResolver = getApplication<Application>().contentResolver

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
            _contacts.value = contactsMap.values.toList()
        }
    }
}