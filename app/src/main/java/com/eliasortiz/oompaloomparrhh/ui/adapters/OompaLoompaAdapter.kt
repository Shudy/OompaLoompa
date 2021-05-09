package com.eliasortiz.oompaloomparrhh.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompa
import com.eliasortiz.oompaloomparrhh.databinding.LayoutOompaLoompaViewHolderBinding
import com.eliasortiz.oompaloomparrhh.ui.listeners.OompaLoompaListListener
import com.eliasortiz.oompaloomparrhh.ui.viewHolders.OompaLoompaViewHolder

class OompaLoompaAdapter(
    private val oompaLoompaList: MutableList<OompaLoompa>,
    var oompaLoompaListener: OompaLoompaListListener?
) : RecyclerView.Adapter<OompaLoompaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OompaLoompaViewHolder {
        val view = LayoutOompaLoompaViewHolderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return OompaLoompaViewHolder(view, oompaLoompaListener)
    }

    override fun onBindViewHolder(holder: OompaLoompaViewHolder, position: Int) {
        holder.bindView(oompaLoompaList[position])
    }

    override fun getItemCount() = oompaLoompaList.size

}