package com.eliasortiz.oompaloomparrhh.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompaModel
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository
import com.eliasortiz.oompaloomparrhh.utils.Coroutines
import com.eliasortiz.oompaloomparrhh.utils.ResultResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel
@Inject constructor(
    private val repository: OompaLoompaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var oompaLoompa: OompaLoompaModel? = null
    private val oompaLoompaLiveData: MutableLiveData<OompaLoompaModel?> =
        MutableLiveData(oompaLoompa)

    init {
        Coroutines.main {
            savedStateHandle.get<Int>("id")?.let { id ->
                val response = repository.getOompaLoompa(id)
                when (response) {
                    is ResultResponse.Loading -> {
                    }
                    is ResultResponse.Failure -> {
                    }
                    is ResultResponse.Success -> {
                        val data = response.data as OompaLoompaModel
                        oompaLoompa = data
                        oompaLoompaLiveData.postValue(oompaLoompa)
                    }
                }
            }
        }
    }

    fun getOompaLoompaLiveData(): LiveData<OompaLoompaModel?> = oompaLoompaLiveData
}