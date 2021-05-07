package com.eliasortiz.oompaloomparrhh.homeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eliasortiz.oompaloomparrhh.databinding.HomeListFragmentBinding

class HomeListFragment : Fragment() {

    private var _binding: HomeListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO Delete : For testing purposes
        binding.buttonTest.setOnClickListener {
            findNavController().navigate(HomeListFragmentDirections.actionHomeListFragmentToDetailFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeListViewModel::class.java)
    }

}