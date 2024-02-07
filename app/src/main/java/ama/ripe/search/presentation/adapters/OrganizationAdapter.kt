package ama.ripe.search.presentation.adapters

import ama.ripe.search.R
import ama.ripe.search.databinding.ItemOrganizationBinding
import ama.ripe.search.domain.entity.OrganizationDomModel
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso

class OrganizationAdapter(
) : ListAdapter<OrganizationDomModel, OrganizationViewHolder>(OrganizationDiffCallback) {

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

    private fun getInfo(itemOrg: OrganizationDomModel): List<String> {
        val address = if (itemOrg.address.isNotEmpty()) "<b>Адрес: </b> $itemOrg.address" else ""
        val phone = if (itemOrg.phone.isNotEmpty()) "<b>Телефон: </b> $itemOrg.phone" else ""
        val faxNo = if (itemOrg.faxNo.isNotEmpty()) "<b>Факс: </b> $itemOrg.faxNo" else ""
        return listOf(address, phone, faxNo)
    }


    private fun setFlagButVisibility(v: View, string: String) {
        if (string.isNotEmpty())
            v.visibility = View.VISIBLE
        else v.visibility = View.INVISIBLE
    }

    override fun onBindViewHolder(holder: OrganizationViewHolder, position: Int) {
        val itemOrg = getItem(position)
        //Log.e("itemOrg", itemOrg.toString())
        with(holder.binding) {
            with(itemOrg) {

                tvTitle.text = orgName
                tvInfo.text = organization
                if (countryFlag != null)
                    Picasso.get().load(countryFlag)
                        .placeholder(R.drawable.splash)
                        .into(ivLogoCountry)
                ivLogoCountry.visibility = if (countryFlag != null) View.VISIBLE else View.GONE

                val s = getInfo(itemOrg)
                setFlagButVisibility(frgmntOrgInfo, s[0] + s[1] + s[2])

                frgmntOrgInfo.setOnClickListener {
                    onButtonInfoClickListener?.onButtonInfoClick(
                        "$s[0]<br>$s[1]<br>$s[2]"
                    )
                }

                root.setOnClickListener {
                    onOrganizationClickListener?.onOrganizationClick(organization)
                }
            }
        }
    }

    companion object {

        private const val IMAGE_ENDS = ".png"
        private const val SECONDS_IN_MINUTE = 60
    }

    interface OnButtonInfoClickListener {
        fun onButtonInfoClick(info: String)
    }

    interface OnOrganizationClickListener {
        fun onOrganizationClick(orgDomModel: String)
    }
}
