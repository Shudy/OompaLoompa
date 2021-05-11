package com.eliasortiz.oompaloomparrhh.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.eliasortiz.oompaloomparrhh.R
import com.eliasortiz.oompaloomparrhh.data.network.NetworkConnectionInterception
import com.eliasortiz.oompaloomparrhh.data.network.OompaLoompaApi
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository
import com.eliasortiz.oompaloomparrhh.databinding.DetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailViewModel
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = OompaLoompaApi.getInstance(NetworkConnectionInterception(requireContext()))
        val repository = OompaLoompaRepository.getInstance(api)
        val factory = DetailViewModelFactory(repository = repository, id = args.id)

        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

    }

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
    }

    fun setViewModelObservers() {
        viewModel.getOompaLoompaLiveData().observe(viewLifecycleOwner) { oompaLoompa ->
            oompaLoompa?.let { oL ->

                Glide.with(binding.root)
                    .load(oL.image)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.image)

                binding.nameSurname.text = String.format(
                    getString(R.string.nameAndSurname),
                    oL.firstName,
                    oL.lastName
                )

                binding.profession.text = oL.profession

                binding.age.text = String.format(
                    getString(R.string.age),
                    oL.age
                )

                binding.gender.text = String.format(
                    getString(R.string.gender),
                    oL.gender
                )

                binding.olHeight.text = String.format(
                    getString(R.string.height),
                    oL.height
                )

                binding.email.setTitleAndDescription(getString(R.string.email), oL.email)
                binding.country.setTitleAndDescription(getString(R.string.country), oL.country)
                binding.descrip.setTitleAndDescription(
                    getString(R.string.description),
                    oL.description
                )

                binding.color.setTitleAndDescription(getString(R.string.color), oL.favorite?.color)
                binding.food.setTitleAndDescription(getString(R.string.food), oL.favorite?.food)
                binding.song.setTitleAndDescription(getString(R.string.song), oL.favorite?.song)
                binding.randomText.setTitleAndDescription(
                    getString(R.string.randomText),
                    oL.favorite?.randomString
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}