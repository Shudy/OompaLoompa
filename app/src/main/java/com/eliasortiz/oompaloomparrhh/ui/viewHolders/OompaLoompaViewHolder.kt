package com.eliasortiz.oompaloomparrhh.ui.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eliasortiz.oompaloomparrhh.R
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompa
import com.eliasortiz.oompaloomparrhh.databinding.LayoutOompaLoompaViewHolderBinding
import com.eliasortiz.oompaloomparrhh.ui.listeners.OompaLoompaListListener

class OompaLoompaViewHolder(
    private val view: LayoutOompaLoompaViewHolderBinding,
    private val oompaLoompaListener: OompaLoompaListListener?
) :
    RecyclerView.ViewHolder(view.root) {

    fun bindView(oompaLoompa: OompaLoompa) {

        val context = view.root.context
        Glide.with(view.root)
            .load(oompaLoompa.image)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(view.image)

        view.nameSurname.text = String.format(
            context.getString(R.string.nameAndSurname),
            oompaLoompa.firstName,
            oompaLoompa.lastName
        )

        view.profession.text = oompaLoompa.profession
        view.email.text = oompaLoompa.email

        view.root.setOnClickListener {
            oompaLoompa.id?.let { id ->
                oompaLoompaListener?.onOompaLoompaClicked(id)
            }
        }
    }
}