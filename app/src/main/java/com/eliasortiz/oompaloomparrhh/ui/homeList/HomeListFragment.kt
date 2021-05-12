package com.eliasortiz.oompaloomparrhh.ui.homeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eliasortiz.oompaloomparrhh.R
import com.eliasortiz.oompaloomparrhh.data.models.FilterOptionWithStatusModel
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompaModel
import com.eliasortiz.oompaloomparrhh.databinding.HomeListFragmentBinding
import com.eliasortiz.oompaloomparrhh.ui.adapters.OompaLoompaAdapter
import com.eliasortiz.oompaloomparrhh.ui.customViews.FiltersBottomSheet
import com.eliasortiz.oompaloomparrhh.ui.listeners.OompaLoompaListListener
import com.eliasortiz.oompaloomparrhh.ui.recyclerDecorators.OompaLoompaDecorator
import com.eliasortiz.oompaloomparrhh.utils.ResultResponse
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeListFragment : Fragment(), OompaLoompaListListener {

    private var _binding: HomeListFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeListViewModel by viewModels()

    private lateinit var oompaLoompaAdapter: OompaLoompaAdapter
    private val oompaLoompaList: MutableList<OompaLoompaModel> = mutableListOf()

    private var keepHidedFab: Boolean = true
    private lateinit var snackbar: Snackbar

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
        oompaLoompaAdapter = OompaLoompaAdapter(oompaLoompaList, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        snackbar = Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE)

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

                    // Pagination control
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
                        showFab()
                    }

                    super.onScrollStateChanged(recyclerView, newState)
                }
            })
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun setViewModelObservers() {
        viewModel.getOompaLoompaListLiveData().observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResultResponse.Failure -> {
                    hideProgress()
                    isLoadingData = false
                    showError(response.message)
                }
                is ResultResponse.Loading -> {
                    if (oompaLoompaList.size == 0) {
                        showProgress()
                    }
                    isLoadingData = true
                }
                is ResultResponse.Success -> {
                    val oompaLoompaResponseList = response.data as List<OompaLoompaModel>
                    oompaLoompaList.clear()
                    oompaLoompaList.addAll(oompaLoompaResponseList)
                    oompaLoompaAdapter.notifyDataSetChanged()
                    hideProgress()
                    hideError()
                    isLoadingData = false
                }
            }
        }

        viewModel.getOptionsListLiveData().observe(viewLifecycleOwner) {
            checkFabVisibility(it.first, it.second)
            binding.filter.setGenderOptions(getString(R.string.genderTitle), it.first)
            binding.filter.setProfessionOptions(getString(R.string.professionTitle), it.second)
        }
    }

    private fun checkFabVisibility(
        genderOptions: List<FilterOptionWithStatusModel>,
        profession: List<FilterOptionWithStatusModel>
    ) {
        keepHidedFab = genderOptions.isEmpty() && profession.isEmpty()

    }

    private fun showFab() {
        if (keepHidedFab) {
            binding.fab.hide()
        } else {
            binding.fab.show()
        }
    }

    private fun showError(message: String) {
        binding.fab.hide()
        if (oompaLoompaList.size == 0) {
            binding.errorContainer.errorMessage.text = message
            binding.errorContainer.root.visibility = View.VISIBLE
        } else {
            snackbar.setText(message)
            snackbar.setAction(R.string.retry) {
                viewModel.retryLoadData()
            }

            if (!snackbar.isShown) {
                snackbar.show()
            }
        }
    }

    private fun hideError() {
        binding.errorContainer.root.visibility = View.GONE
        snackbar.dismiss()
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
                    showFab()
                }
            }
        })

        binding.errorContainer.retryButton.setOnClickListener { viewModel.retryLoadData() }
    }

    override fun onOompaLoompaClicked(id: Int) {
        findNavController().navigate(
            HomeListFragmentDirections.actionHomeListFragmentToDetailFragment(id)
        )
    }

    private fun showProgress() {
        binding.loading.root.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.loading.root.visibility = View.GONE
    }
}