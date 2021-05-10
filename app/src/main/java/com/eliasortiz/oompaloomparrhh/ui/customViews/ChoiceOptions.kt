package com.eliasortiz.oompaloomparrhh.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.eliasortiz.oompaloomparrhh.data.models.FilterOptionWithStatus
import com.eliasortiz.oompaloomparrhh.databinding.CustomChoiceOptionsBinding
import com.eliasortiz.oompaloomparrhh.databinding.SingleChipChoiceBinding
import com.eliasortiz.oompaloomparrhh.utils.capitalizeFirstLetter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChoiceOptions @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    interface ChoiceOptionsListener {
        fun chipSelected(tag: String)
    }

    private val binding =
        CustomChoiceOptionsBinding.inflate(LayoutInflater.from(context), this, true)
    private var optionsList: MutableList<FilterOptionWithStatus> = mutableListOf()
    private var listener: ChoiceOptionsListener? = null

    private val onCheckListener: ChipGroup.OnCheckedChangeListener =
        ChipGroup.OnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip?>(checkedId)
            chip?.tag?.let { tag ->
                tag as String
                listener?.chipSelected(tag)
            } ?: run {
                listener?.chipSelected("")
            }
        }

    fun setListener(listener: ChoiceOptionsListener?) {
        this.listener = listener
    }

    fun setTitleAndOptions(title: String, options: List<FilterOptionWithStatus>) {
        binding.titleOptions.text = title
        setOptions(options)
    }

    private fun setOptions(options: List<FilterOptionWithStatus>) {
        optionsList.clear()
        optionsList.addAll(options)
        binding.choiceGroup.setOnCheckedChangeListener(null)
        binding.choiceGroup.removeAllViews()

        optionsList.forEach { option ->
            val chip = SingleChipChoiceBinding.inflate(
                LayoutInflater.from(context),
                binding.choiceGroup,
                false
            )

            chip.root.apply {
                text = option.optionTag.capitalizeFirstLetter()
                tag = option.optionTag
                isChecked = option.isChecked
            }

            binding.choiceGroup.addView(chip.root)
        }

        binding.choiceGroup.setOnCheckedChangeListener(onCheckListener)
    }
}