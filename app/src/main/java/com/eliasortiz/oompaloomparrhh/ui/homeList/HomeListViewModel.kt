package com.eliasortiz.oompaloomparrhh.ui.homeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eliasortiz.oompaloomparrhh.data.models.FilterOptionWithStatus
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompa
import com.eliasortiz.oompaloomparrhh.data.network.reponse.OompaLoompasResponse
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository
import com.eliasortiz.oompaloomparrhh.utils.Coroutines
import com.eliasortiz.oompaloomparrhh.utils.ResultResponse
import timber.log.Timber

private const val THRESHOLD = 10

class HomeListViewModel(private val repository: OompaLoompaRepository) : ViewModel() {

    private val oompaLoompaList: MutableList<OompaLoompa> = mutableListOf()
    private var oompaLoompaFilteredList: MutableList<OompaLoompa> = mutableListOf()

    private val oompaLoompaListLiveData: MutableLiveData<List<OompaLoompa>> =
        MutableLiveData(oompaLoompaList)

    private val isLoadingData: MutableLiveData<Boolean> = MutableLiveData(false)
    private val showErrorLiveData: MutableLiveData<Pair<Boolean, String>> =
        MutableLiveData(Pair(false, ""))

    private var lastPageRequested = 0
    private var hasMorePages = true

    private var genderList: MutableList<FilterOptionWithStatus> = mutableListOf()
    private var professionList: MutableList<FilterOptionWithStatus> = mutableListOf()

    private val filterOptionsLiveData: MutableLiveData<Pair<List<FilterOptionWithStatus>, List<FilterOptionWithStatus>>> =
        MutableLiveData(
            Pair(genderList, professionList)
        )

    private var activeGenderFilter = ""
    private var activeProfessionFilter = ""
    private var filterIsActive = false

    init {
        getNewOompaLoompasPage()
    }

    fun getOompaLoompaListLiveData(): LiveData<List<OompaLoompa>> = oompaLoompaListLiveData

    fun getIsLoadingDataLiveData(): LiveData<Boolean> = isLoadingData

    fun getShowErrorLiveData(): LiveData<Pair<Boolean, String>> = showErrorLiveData

    fun getOptionsListLiveData(): LiveData<Pair<List<FilterOptionWithStatus>, List<FilterOptionWithStatus>>> =
        filterOptionsLiveData

    fun getNewOompaLoompasPage() {
        if (hasMorePages) {
            loadOompaLoompasData(lastPageRequested + 1)
        }
    }

    private fun loadOompaLoompasData(page: Int) {
        Timber.d("Requested page: $page")

        if (hasMorePages) {
            Coroutines.main {

                isLoadingData.postValue(true)
                lastPageRequested = page

                val response = repository.getOompaLoompasList(page)
                when (response) {
                    is ResultResponse.Failure -> {
                        isLoadingData.postValue(false)
                        showErrorLiveData.postValue(Pair(true, response.message))
                    }

                    ResultResponse.Loading -> {
                    }

                    is ResultResponse.Success -> {
                        isLoadingData.postValue(false)

                        val data = response.data as OompaLoompasResponse

                        data.current?.let { currentPage ->
                            data.total?.let { maxPages ->
                                if (currentPage == maxPages) {
                                    hasMorePages = false
                                }
                            }
                        }

                        data.results?.filterNotNull()?.let {
                            oompaLoompaList.addAll(it)

                            if (filterIsActive) {
                                applyFilters()
                            } else {
                                oompaLoompaListLiveData.postValue(oompaLoompaList)
                            }

                            refreshFiltersList(it)
                        }
                    }
                }
            }
        }
    }

    private fun refreshFiltersList(oompaLoompaList: List<OompaLoompa>) {
        oompaLoompaList.forEach { oompaLoompa ->
            oompaLoompa.gender?.let { gender ->
                val position = genderList.indexOfFirst { it.optionTag == gender }
                if (position < 0) {
                    genderList.add(FilterOptionWithStatus(gender))
                } else {
                    genderList[position].isChecked =
                        genderList[position].optionTag == activeGenderFilter
                }
            }

            oompaLoompa.profession?.let { profession ->
                val position = professionList.indexOfFirst { it.optionTag == profession }
                if (position < 0) {
                    professionList.add(FilterOptionWithStatus(profession))
                } else {
                    professionList[position].isChecked =
                        professionList[position].optionTag == activeProfessionFilter
                }

            }
        }

        filterOptionsLiveData.postValue(Pair(genderList, professionList))
    }

    fun cleanError() {
        showErrorLiveData.postValue(Pair(false, ""))
    }

    fun applyGenderFilter(genderOption: String) {
        activeGenderFilter = genderOption
        applyFilters()
    }

    fun applyProfessionFilter(professionOption: String) {
        activeProfessionFilter = professionOption
        applyFilters()
    }

    private fun applyFilters() {
        if (activeGenderFilter.isEmpty() && activeProfessionFilter.isEmpty()) {
            if (filterIsActive) {
                oompaLoompaListLiveData.postValue(oompaLoompaList)
            }
            filterIsActive = false
        } else {
            oompaLoompaFilteredList.clear()
            oompaLoompaFilteredList.addAll(
                oompaLoompaList.filter { oompaLoompa ->
                    var genderMatch = true
                    var professionMatch = true
                    activeGenderFilter.takeIf { it.isNotEmpty() }?.let { filter ->
                        oompaLoompa.gender?.let { gndr ->
                            genderMatch = gndr == filter
                        }
                    }

                    activeProfessionFilter.takeIf { it.isNotEmpty() }?.let { filter ->
                        oompaLoompa.profession?.let { prof ->
                            professionMatch = prof == filter
                        }
                    }

                    genderMatch && professionMatch
                }
            )

            filterIsActive = true
            if (oompaLoompaFilteredList.size < THRESHOLD) {
                getNewOompaLoompasPage()
            }
            oompaLoompaListLiveData.postValue(oompaLoompaFilteredList)
        }
    }
}