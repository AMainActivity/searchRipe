package ama.ripe.search.presentation.adapters

import ama.ripe.search.R
import ama.ripe.search.databinding.ItemOrganizationBinding
import ama.ripe.search.domain.entity.OrganizationDomModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso

class OrganizationAdapter :
    ListAdapter<OrganizationDomModel, OrganizationViewHolder>(OrganizationDiffCallback) {

    var onOrganizationClickListener: OnOrganizationClickListener? = null
    var onButtonInfoClickListener: OnButtonInfoClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationViewHolder {
        val binding = ItemOrganizationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrganizationViewHolder(binding)
    }

    private fun getInfo(itemOrg: OrganizationDomModel, ctx: Context): List<String> {
        val address = if (itemOrg.address.isNotEmpty()) String.format(
            ctx.getString(R.string.org_info_format),
            ctx.getString(R.string.org_address),
            itemOrg.address
        ) else EMPTY_STRING
        val phone = if (itemOrg.phone.isNotEmpty()) String.format(
            ctx.getString(R.string.org_info_format),
            ctx.getString(R.string.org_phone),
            itemOrg.phone
        ) else EMPTY_STRING
        val faxNo = if (itemOrg.faxNo.isNotEmpty()) String.format(
            ctx.getString(R.string.org_info_format),
            ctx.getString(R.string.org_faxNo),
            itemOrg.faxNo
        ) else EMPTY_STRING
        return listOf(address, phone, faxNo)
    }


    private fun setFlagButVisibility(v: View, string: String) {
        if (string.isNotEmpty())
            v.visibility = View.VISIBLE
        else v.visibility = View.INVISIBLE
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val itemOrg = getItem(position)
        with(holder.binding) {
            with(itemOrg) {

                tvTitle.text = orgName
                tvInfo.text = organization
                if (countryFlag != null)
                    Picasso.get().load(countryFlag)
                        .placeholder(R.drawable.no_image)
                        .into(ivLogoCountry)
                ivLogoCountry.visibility = if (countryFlag != null) View.VISIBLE else View.GONE

                val s = getInfo(itemOrg, tvInfo.context)
                setFlagButVisibility(frgmntOrgInfo, s[0] + s[1] + s[2])

                frgmntOrgInfo.setOnClickListener {
                    onButtonInfoClickListener?.onButtonInfoClick(
                        String.format(ORG_INFO, s[0], s[1], s[2])
                    )
                }

                root.setOnClickListener {
                    onOrganizationClickListener?.onOrganizationClick(organization)
                }
            }
        }
    }

    companion object {

        private const val ORG_INFO = "%s<br>%s<br>%s"
        private const val EMPTY_STRING = ""
    }

    interface OnButtonInfoClickListener {
        fun onButtonInfoClick(info: String)
    }

    interface OnOrganizationClickListener {
        fun onOrganizationClick(tInfo: String)
    }
}
