package com.eliasortiz.oompaloomparrhh.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eliasortiz.oompaloomparrhh.data.network.NetworkConnectionInterception
import com.eliasortiz.oompaloomparrhh.data.network.OompaLoompaApi
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository
import com.eliasortiz.oompaloomparrhh.databinding.DetailFragmentBinding

class DetailFragment : Fragment() {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = OompaLoompaApi.getInstance(NetworkConnectionInterception(requireContext()))
        val repository = OompaLoompaRepository.getInstance(api)
        val factory = DetailViewModelFactory(repository = repository)

        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)
    }

}