package ama.ripe.search.presentation.adapters

import ama.ripe.search.domain.entity.InetNumDomModel
import androidx.recyclerview.widget.DiffUtil

object InetNumDiffCallback : DiffUtil.ItemCallback<InetNumDomModel>() {

    override fun areItemsTheSame(
        oldItem: InetNumDomModel,
        newItem: InetNumDomModel
    ): Boolean {
        return oldItem.netName == newItem.netName
    }

    override fun areContentsTheSame(
        oldItem: InetNumDomModel,
        newItem: InetNumDomModel
    ): Boolean {
        return oldItem == newItem
    }
}
