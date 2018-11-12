package com.tompee.utilities.passwordmanager.feature.packages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tompee.utilities.passwordmanager.R
import com.tompee.utilities.passwordmanager.databinding.ListPackageBinding
import com.tompee.utilities.passwordmanager.model.Package

class PackageAdapter : RecyclerView.Adapter<PackageAdapter.ViewHolder>() {
    var packageList: List<Package> = listOf()
        set(value) {
            val result = DiffUtil.calculateDiff(DiffUtilCallback(value))
            field = value
            result.dispatchUpdatesTo(this)
        }

    var listener: (Package) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListPackageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_package,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = packageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = packageList[position]
        holder.binding.packageModel = current
        holder.binding.root.setOnClickListener{
            listener(current)
        }
    }

    private inner class DiffUtilCallback(private val list: List<Package>) : DiffUtil.Callback() {
        val oldList = this@PackageAdapter.packageList

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].packageName == list[newItemPosition].packageName

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = list.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItemPosition, newItemPosition)
    }

    class ViewHolder(val binding: ListPackageBinding) : RecyclerView.ViewHolder(binding.root)
}