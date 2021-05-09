package com.eliasortiz.oompaloomparrhh.ui.homeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompa
import com.eliasortiz.oompaloomparrhh.data.network.reponse.OompaLoompasResponse
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository
import com.eliasortiz.oompaloomparrhh.utils.Coroutines
import com.eliasortiz.oompaloomparrhh.utils.ResultResponse
import timber.log.Timber

class HomeListViewModel(private val repository: OompaLoompaRepository) : ViewModel() {

    private val oompaLoompaList: MutableList<OompaLoompa> = mutableListOf()
    private val oompaLoompaListLiveData: MutableLiveData<List<OompaLoompa>> =
        MutableLiveData(oompaLoompaList)
    private val isLoadingData: MutableLiveData<Boolean> = MutableLiveData(false)
    private val showErrorLiveData: MutableLiveData<Pair<Boolean, String>> =
        MutableLiveData(Pair(false, ""))

    private var lastPageRequested = 0
    private var hasMorePages = true

    init {
        getNewOompaLoompasPage()
    }

    fun getOompaLoompaListLiveData(): LiveData<List<OompaLoompa>> = oompaLoompaListLiveData

    fun getIsLoadingDataLiveData(): LiveData<Boolean> = isLoadingData

    fun getShowErrorLiveData(): LiveData<Pair<Boolean, String>> = showErrorLiveData

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
                                if (currentPage == 3) {
                                    hasMorePages = false
                                }
                            }
                        }

                        data.results?.filterNotNull()?.let {
                            oompaLoompaList.addAll(it)
                            oompaLoompaListLiveData.postValue(oompaLoompaList)
                        }
                    }
                }
            }
        }
    }

    fun cleanError() {
        showErrorLiveData.postValue(Pair(false, ""))
    }
}