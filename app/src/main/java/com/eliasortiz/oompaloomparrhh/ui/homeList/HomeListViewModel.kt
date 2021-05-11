package com.eliasortiz.oompaloomparrhh.ui.homeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eliasortiz.oompaloomparrhh.data.models.FilterOptionWithStatusModel
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompaModel
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompasResponseModel
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository
import com.eliasortiz.oompaloomparrhh.utils.Coroutines
import com.eliasortiz.oompaloomparrhh.utils.ResultResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

private const val THRESHOLD = 10

@HiltViewModel
class HomeListViewModel
@Inject constructor(
    private val repository: OompaLoompaRepository
) : ViewModel() {

    private val oompaLoompaList: MutableList<OompaLoompaModel> = mutableListOf()
    private var oompaLoompaFilteredList: MutableList<OompaLoompaModel> = mutableListOf()

    private val oompaLoompaListLiveData: MutableLiveData<List<OompaLoompaModel>> =
        MutableLiveData(oompaLoompaList)

    private val isLoadingData: MutableLiveData<Boolean> = MutableLiveData(false)
    private val showErrorLiveData: MutableLiveData<Pair<Boolean, String>> =
        MutableLiveData(Pair(false, ""))

    private var lastPageRequested = 0
    private var hasMorePages = true

    private var genderList: MutableList<FilterOptionWithStatusModel> = mutableListOf()
    private var professionList: MutableList<FilterOptionWithStatusModel> = mutableListOf()

    private val filterOptionsLiveData: MutableLiveData<Pair<List<FilterOptionWithStatusModel>, List<FilterOptionWithStatusModel>>> =
        MutableLiveData(Pair(genderList, professionList))

    private var activeGenderFilter = ""
    private var activeProfessionFilter = ""
    private var filterIsActive = false

    init {
        getNewOompaLoompasPage()
    }

    fun getOompaLoompaListLiveData(): LiveData<List<OompaLoompaModel>> = oompaLoompaListLiveData

    fun getIsLoadingDataLiveData(): LiveData<Boolean> = isLoadingData

    fun getShowErrorLiveData(): LiveData<Pair<Boolean, String>> = showErrorLiveData

    fun getOptionsListLiveData(): LiveData<Pair<List<FilterOptionWithStatusModel>, List<FilterOptionWithStatusModel>>> =
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

                        val data = response.data as OompaLoompasResponseModel

                        if (data.current == data.total) {
                            hasMorePages = false
                        }

                        oompaLoompaList.addAll(data.results)
                        if (filterIsActive) {
                            applyFilters()
                        } else {
                            oompaLoompaListLiveData.postValue(oompaLoompaList)
                        }

                        refreshFiltersList(data.results)
                    }
                }
            }
        }
    }

    private fun refreshFiltersList(oompaLoompaList: List<OompaLoompaModel>) {
        oompaLoompaList.forEach { oompaLoompa ->

            var position = genderList.indexOfFirst { it.optionTag == oompaLoompa.gender }
            if (position < 0) {
                genderList.add(FilterOptionWithStatusModel(oompaLoompa.gender))
            } else {
                genderList[position].isChecked =
                    genderList[position].optionTag == activeGenderFilter
            }

            position = professionList.indexOfFirst { it.optionTag == oompaLoompa.profession }
            if (position < 0) {
                professionList.add(FilterOptionWithStatusModel(oompaLoompa.profession))
            } else {
                professionList[position].isChecked =
                    professionList[position].optionTag == activeProfessionFilter
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
                        genderMatch = oompaLoompa.gender == filter
                    }

                    activeProfessionFilter.takeIf { it.isNotEmpty() }?.let { filter ->
                        professionMatch = oompaLoompa.profession == filter
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