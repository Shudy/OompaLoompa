package com.eliasortiz.oompaloomparrhh.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eliasortiz.oompaloomparrhh.R
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompaModel
import com.eliasortiz.oompaloomparrhh.databinding.DetailFragmentBinding
import com.eliasortiz.oompaloomparrhh.utils.ResultResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModelObservers()
        setListener()
    }

    private fun setViewModelObservers() {
        viewModel.getOompaLoompaLiveData().observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResultResponse.Failure -> {
                    hideProgress()
                    showError(response.message)
                }

                is ResultResponse.Loading -> showProgress()

                is ResultResponse.Success -> {
                    val oompaLoompaModel = response.data as OompaLoompaModel

                    Glide.with(binding.root)
                        .load(oompaLoompaModel.image)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.image)

                    binding.nameSurname.text = String.format(
                        getString(R.string.nameAndSurname),
                        oompaLoompaModel.firstName,
                        oompaLoompaModel.lastName
                    )

                    binding.profession.text = oompaLoompaModel.profession

                    binding.age.text = String.format(
                        getString(R.string.age),
                        oompaLoompaModel.age
                    )

                    binding.gender.text = String.format(
                        getString(R.string.gender),
                        oompaLoompaModel.gender
                    )

                    binding.olHeight.text = String.format(
                        getString(R.string.height),
                        oompaLoompaModel.height
                    )

                    binding.email.setTitleAndDescription(
                        getString(R.string.email),
                        oompaLoompaModel.email
                    )

                    binding.country.setTitleAndDescription(
                        getString(R.string.country),
                        oompaLoompaModel.country
                    )

                    binding.descrip.setTitleAndDescription(
                        getString(R.string.description),
                        oompaLoompaModel.description
                    )

                    binding.color.setTitleAndDescription(
                        getString(R.string.color),
                        oompaLoompaModel.favorite.color
                    )

                    binding.food.setTitleAndDescription(
                        getString(R.string.food),
                        oompaLoompaModel.favorite.food
                    )

                    binding.song.setTitleAndDescription(
                        getString(R.string.song),
                        oompaLoompaModel.favorite.song
                    )

                    binding.randomText.setTitleAndDescription(
                        getString(R.string.randomText),
                        oompaLoompaModel.favorite.randomString
                    )

                    hideProgress()
                    hideError()
                }
            }
        }
    }

    private fun setListener() {
        binding.errorContainer.retryButton.setOnClickListener {
            viewModel.retryLoadData()
        }
    }

    private fun showError(message: String) {
        hideInfoCards()
        binding.errorContainer.errorMessage.text = message
        binding.errorContainer.root.visibility = View.VISIBLE
    }

    private fun showInfoCards() {
        binding.header.visibility = View.VISIBLE
        binding.genericData.visibility = View.VISIBLE
        binding.favorites.visibility = View.VISIBLE
    }

    private fun hideInfoCards() {
        binding.header.visibility = View.GONE
        binding.genericData.visibility = View.GONE
        binding.favorites.visibility = View.GONE
    }

    private fun hideError() {
        showInfoCards()
        binding.errorContainer.root.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showProgress() {
        binding.loading.root.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.loading.root.visibility = View.GONE
    }
}