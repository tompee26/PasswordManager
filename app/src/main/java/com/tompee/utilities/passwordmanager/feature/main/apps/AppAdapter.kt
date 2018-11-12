package com.tompee.utilities.passwordmanager.feature.main.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import androidx.recyclerview.widget.SortedListAdapterCallback
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.databinding.ListPackageCredentialBinding
import com.tompee.utilities.passwordmanager.model.PackageCredential

class AppAdapter : RecyclerView.Adapter<AppAdapter.ViewHolder>() {
    private val packageList = SortedList<PackageCredential>(PackageCredential::class.java,
        object : SortedListAdapterCallback<PackageCredential>(this) {
            override fun areItemsTheSame(item1: PackageCredential, item2: PackageCredential): Boolean {
                return item1.name == item2.name && item1.packageName == item2.packageName
            }

            override fun compare(o1: PackageCredential, o2: PackageCredential): Int {
                return o1.name.toLowerCase().compareTo(o2.name.toLowerCase())
            }

            override fun areContentsTheSame(oldItem: PackageCredential, newItem: PackageCredential): Boolean {
                return oldItem.name == newItem.name && oldItem.packageName == newItem.packageName
            }
        })

    var listener: (PackageCredential) -> Unit = {}

    fun addPackages(users: List<PackageCredential>) {
        packageList.addAll(users)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListPackageCredentialBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_package_credential,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = packageList.size()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = packageList[position]
        holder.binding.packageModel = current
        holder.binding.root.setOnClickListener {
            listener(current)
        }
    }

    class ViewHolder(val binding: ListPackageCredentialBinding) : RecyclerView.ViewHolder(binding.root)
}