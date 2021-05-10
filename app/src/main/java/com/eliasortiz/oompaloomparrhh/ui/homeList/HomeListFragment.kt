package com.eliasortiz.oompaloomparrhh.ui.homeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eliasortiz.oompaloomparrhh.R
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompa
import com.eliasortiz.oompaloomparrhh.data.network.NetworkConnectionInterception
import com.eliasortiz.oompaloomparrhh.data.network.OompaLoompaApi
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository
import com.eliasortiz.oompaloomparrhh.databinding.HomeListFragmentBinding
import com.eliasortiz.oompaloomparrhh.ui.adapters.OompaLoompaAdapter
import com.eliasortiz.oompaloomparrhh.ui.customViews.FiltersBottomSheet
import com.eliasortiz.oompaloomparrhh.ui.listeners.OompaLoompaListListener
import com.eliasortiz.oompaloomparrhh.ui.recyclerDecorators.OompaLoompaDecorator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar

class HomeListFragment : Fragment(), OompaLoompaListListener {

    private var _binding: HomeListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeListViewModel
    private lateinit var oompaLoompaAdapter: OompaLoompaAdapter
    private val oompaLoompaList: MutableList<OompaLoompa> = mutableListOf()

    //Variables to handle scrolling and request more data to repository
    private var isLoadingData = false
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var visibleThreshold = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = OompaLoompaApi.getInstance(NetworkConnectionInterception(requireContext()))
        val repository = OompaLoompaRepository.getInstance(api)
        val factory = HomeListViewModelFactory(repository = repository)

        viewModel = ViewModelProvider(this, factory).get(HomeListViewModel::class.java)

        oompaLoompaAdapter = OompaLoompaAdapter(oompaLoompaList, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecylerView()

        setViewModelObservers()
        setClickListeners()
        setListeners()
    }

    private fun setRecylerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = oompaLoompaAdapter
            setHasFixedSize(true)
            addItemDecoration(OompaLoompaDecorator())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    // Fab control
                    if (dy > 0 || (dy < 0 && binding.fab.isShown)) {
                        binding.fab.hide()
                    }

                    if (dy > 0) {
                        (recyclerView.layoutManager as? LinearLayoutManager)?.let { layoutManager ->
                            visibleItemCount = layoutManager.childCount
                            totalItemCount = layoutManager.itemCount
                            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                            if (!isLoadingData) {
                                if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                                    viewModel.getNewOompaLoompasPage()
                                }
                            }
                        }
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    // Fab control
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && !binding.filter.isActive()) {
                        binding.fab.show()
                    }
                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
    }

    private fun setViewModelObservers() {
        viewModel.getOompaLoompaListLiveData()
            .observe(viewLifecycleOwner) { oompaLoompaListResponse ->
                oompaLoompaList.clear()
                oompaLoompaList.addAll(oompaLoompaListResponse)
                oompaLoompaAdapter.notifyDataSetChanged()
            }

        viewModel.getIsLoadingDataLiveData().observe(viewLifecycleOwner) { isLoading ->
            isLoadingData = isLoading
        }

        viewModel.getShowErrorLiveData().observe(viewLifecycleOwner) { error ->
            if (error.first) {
                view?.let {
                    Snackbar.make(it, error.second, Snackbar.LENGTH_LONG).show()
                }
                viewModel.cleanError()
            }
        }

        viewModel.getOptionsListLiveData().observe(viewLifecycleOwner) {
            binding.filter.setGenderOptions(getString(R.string.genderTitle), it.first)
            binding.filter.setProfessionOptions(getString(R.string.professionTitle), it.second)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setClickListeners() {
        binding.fab.setOnClickListener {
            binding.fab.hide()
            binding.filter.show()
        }
    }

    fun setListeners() {
        binding.filter.setListener(object : FiltersBottomSheet.FiltersListener {
            override fun genderFilterOptions(genderOption: String) {
                viewModel.applyGenderFilter(genderOption)
            }

            override fun professionFilterOptions(professionOption: String) {
                viewModel.applyProfessionFilter(professionOption)
            }

            override fun filtersBottomSheetStateChanged(state: Int) {
                if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.fab.hide()
                } else {
                    binding.fab.show()
                }
            }
        })
    }

    override fun onOompaLoompaClicked(id: Int) {
        findNavController().navigate(HomeListFragmentDirections.actionHomeListFragmentToDetailFragment())
    }

}