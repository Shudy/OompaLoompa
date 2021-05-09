package com.eliasortiz.oompaloomparrhh.ui.customViews

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.eliasortiz.oompaloomparrhh.databinding.CustomTitleAndTextBinding

private const val MAX_LINES_COLLAPSED = 3

class TitleAndText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding =
        CustomTitleAndTextBinding.inflate(LayoutInflater.from(context), this, true)

    private var descriptionIsCollapsed = true

    init {
        binding.description.setOnClickListener {
            if (descriptionIsCollapsed) {
                binding.description.maxLines = Int.MAX_VALUE
                binding.description.ellipsize = null
            } else {
                binding.description.maxLines = MAX_LINES_COLLAPSED
                binding.description.ellipsize = TextUtils.TruncateAt.END
            }

            descriptionIsCollapsed = !descriptionIsCollapsed
        }
    }

    fun setTitle(title: String) {
        binding.title.text = title
    }

    fun setDescription(description: String?) {
        binding.description.text = description?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(description)
            }
        } ?: run {
            ""
        }
    }

    fun setTitleAndDescription(title: String, description: String?) {
        setTitle(title)
        setDescription(description)
    }
}