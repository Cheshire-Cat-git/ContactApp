package com.example.contactapp.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.R
import com.example.contactapp.databinding.ItemContactBinding

class ContactAdapter(
    private val contacts: List<Contact>,
    private val onItemClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.binding.tvContactName.text = contact.name
        holder.binding.tvContactPhone.text = contact.phoneNumber

        if (contact.photoUri != null) {
            holder.binding.ivContactPhoto.setImageURI(contact.photoUri.toUri())
        } else {
            holder.binding.ivContactPhoto.setImageResource(R.drawable.ic_profile_blank)
        }

        holder.itemView.setOnClickListener { onItemClick(contact) }
    }

    override fun getItemCount() = contacts.size
}