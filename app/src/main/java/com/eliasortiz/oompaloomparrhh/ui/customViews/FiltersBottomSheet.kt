package com.eliasortiz.oompaloomparrhh.ui.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.eliasortiz.oompaloomparrhh.data.models.FilterOptionWithStatus
import com.eliasortiz.oompaloomparrhh.databinding.LayoutFilterBottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class FiltersBottomSheet @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    interface FiltersListener {
        fun genderFilterOptions(genderOption: String)
        fun professionFilterOptions(professionOption: String)
        fun filtersBottomSheetStateChanged(state: Int) {}
    }

    private val binding =
        LayoutFilterBottomsheetBinding.inflate(LayoutInflater.from(context), this, true)
    private var listener: FiltersListener? = null

    private var bottomSheetBehavior: BottomSheetBehavior<View> =
        BottomSheetBehavior.from(binding.bottomSheetContainer)

    init {
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    listener?.filtersBottomSheetStateChanged(newState)
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                /* no-op */
            }
        })

        binding.genderFilter.setListener(object : ChoiceOptions.ChoiceOptionsListener {
            override fun chipSelected(tag: String) {
                listener?.genderFilterOptions(tag)
            }
        })
        binding.professionFilter.setListener(object : ChoiceOptions.ChoiceOptionsListener {
            override fun chipSelected(tag: String) {
                listener?.professionFilterOptions(tag)
            }
        })
    }

    fun setListener(listener: FiltersListener?) {
        this.listener = listener
    }

    fun setGenderOptions(title: String, options: List<FilterOptionWithStatus>) {
        binding.genderFilter.setTitleAndOptions(title, options)
    }

    fun setProfessionOptions(title: String, options: List<FilterOptionWithStatus>) {
        binding.professionFilter.setTitleAndOptions(title, options)
    }

    fun show() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun hide() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    fun isActive(): Boolean = bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED

}