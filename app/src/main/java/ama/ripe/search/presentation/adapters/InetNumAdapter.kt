package ama.ripe.search.presentation.adapters

import ama.ripe.search.R
import ama.ripe.search.databinding.ItemInetNumBinding
import ama.ripe.search.domain.entity.InetNumDomModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.squareup.picasso.Picasso

class InetNumAdapter : ListAdapter<InetNumDomModel, InetNumViewHolder>(InetNumDiffCallback) {

    var onInetNumClickListener: OnInetNumClickListener? = null
    private var unfilteredList = listOf<InetNumDomModel>()
    fun modifyList(list: List<InetNumDomModel>) {
        unfilteredList = list
        submitList(list)
    }

    fun filter(query: CharSequence?) {
        val list = mutableListOf<InetNumDomModel>()

        if (!query.isNullOrEmpty()) {
            list.addAll(unfilteredList.filter {
                it.inetNum.contains(
                    query.toString().lowercase()
                )
            })
        } else {
            list.addAll(unfilteredList)
        }

        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InetNumViewHolder {
        val binding = ItemInetNumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InetNumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InetNumViewHolder, position: Int) {
        val test = getItem(position)
        with(holder.binding) {
            with(test) {

                tvTitle.text = inetNum
                tvInfo.text = netName
                if (countryFlag != null)
                    Picasso.get().load(countryFlag).placeholder(R.drawable.no_image)
                        .into(ivLogoCountry)
                ivLogoCountry.visibility = if (countryFlag != null) View.VISIBLE else View.GONE



                root.setOnClickListener {
                    onInetNumClickListener?.onInetNumClick(netName + "\n" + inetNum)
                }
            }
        }
    }


    interface OnInetNumClickListener {
        fun onInetNumClick(tInfo: String)
    }
}
