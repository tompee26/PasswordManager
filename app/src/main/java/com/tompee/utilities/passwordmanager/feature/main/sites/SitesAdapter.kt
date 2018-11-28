package com.tompee.utilities.passwordmanager.feature.main.sites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.databinding.ListSiteCredentialBinding
import com.tompee.utilities.passwordmanager.model.SiteCredential

class SitesAdapter : RecyclerView.Adapter<SitesAdapter.ViewHolder>() {
    private val siteList = SortedList<SiteCredential>(SiteCredential::class.java,
        object : SortedListAdapterCallback<SiteCredential>(this) {
            override fun areItemsTheSame(item1: SiteCredential, item2: SiteCredential): Boolean {
                return item1.name == item2.name && item1.url == item2.url
            }

            override fun compare(o1: SiteCredential, o2: SiteCredential): Int {
                return o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            }

            override fun areContentsTheSame(oldItem: SiteCredential, newItem: SiteCredential): Boolean {
                return oldItem.name == newItem.name && oldItem.url == newItem.url &&
                        oldItem.username == newItem.username && oldItem.password == newItem.password
            }
        })

    var listener: (SiteCredential) -> Unit = {}

    fun addSites(users: List<SiteCredential>) {
        siteList.replaceAll(users)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListSiteCredentialBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_site_credential,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = siteList.size()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = siteList[position]
        holder.binding.packageModel = current
        holder.binding.root.setOnClickListener {
            listener(current)
        }
    }

    class ViewHolder(val binding: ListSiteCredentialBinding) : RecyclerView.ViewHolder(binding.root)
}