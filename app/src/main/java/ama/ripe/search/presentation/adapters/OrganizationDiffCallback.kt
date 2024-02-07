package ama.ripe.search.presentation.adapters

import ama.ripe.search.domain.entity.OrganizationDomModel
import androidx.recyclerview.widget.DiffUtil

object OrganizationDiffCallback : DiffUtil.ItemCallback<OrganizationDomModel>() {

    override fun areItemsTheSame(
        oldItem: OrganizationDomModel,
        newItem: OrganizationDomModel
    ): Boolean {
        return oldItem.organization == newItem.organization
    }

    override fun areContentsTheSame(
        oldItem: OrganizationDomModel,
        newItem: OrganizationDomModel
    ): Boolean {
        return oldItem == newItem
    }
}
