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

    private var lastPageRequested = 0
    private var activeGenderFilter = ""
    private var activeProfessionFilter = ""
    private var hasMorePages = true
    private var filterIsActive = false

    private val oompaLoompaList: MutableList<OompaLoompaModel> = mutableListOf()
    private var oompaLoompaFilteredList: MutableList<OompaLoompaModel> = mutableListOf()
    private val oompaLoompaListLiveData: MutableLiveData<ResultResponse<Any>> = MutableLiveData()

    private var genderList: MutableList<FilterOptionWithStatusModel> = mutableListOf()
    private var professionList: MutableList<FilterOptionWithStatusModel> = mutableListOf()

    private val filterOptionsLiveData: MutableLiveData<Pair<List<FilterOptionWithStatusModel>, List<FilterOptionWithStatusModel>>> =
        MutableLiveData(Pair(genderList, professionList))

    init {
        getNewOompaLoompasPage()
    }

    fun getOompaLoompaListLiveData(): LiveData<ResultResponse<Any>> = oompaLoompaListLiveData

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
                oompaLoompaListLiveData.postValue(ResultResponse.Loading)

                when (val response = repository.getOompaLoompasList(page)) {
                    is ResultResponse.Failure -> {
                        oompaLoompaListLiveData.postValue(
                            ResultResponse.Failure(
                                response.errorCode,
                                response.message
                            )
                        )
                    }

                    ResultResponse.Loading -> oompaLoompaListLiveData.postValue(ResultResponse.Loading)

                    is ResultResponse.Success -> {
                        lastPageRequested = page
                        val data = response.data as OompaLoompasResponseModel

                        if (data.current == data.total) {
                            hasMorePages = false
                        }

                        oompaLoompaList.addAll(data.results)
                        if (filterIsActive) {
                            applyFilters()
                        } else {
                            oompaLoompaListLiveData.postValue(ResultResponse.Success(oompaLoompaList))
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
                oompaLoompaListLiveData.postValue(ResultResponse.Success(oompaLoompaList))
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
            oompaLoompaListLiveData.postValue(ResultResponse.Success(oompaLoompaFilteredList))
        }
    }

    fun retryLoadData() {
        getNewOompaLoompasPage()
    }

}