package com.eliasortiz.oompaloomparrhh.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eliasortiz.oompaloomparrhh.data.models.OompaLoompa
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository
import com.eliasortiz.oompaloomparrhh.utils.Coroutines
import com.eliasortiz.oompaloomparrhh.utils.ResultResponse

class DetailViewModel(
    private val repository: OompaLoompaRepository,
    private val id: Int
) : ViewModel() {

    private var oompaLoompa: OompaLoompa? = null
    private val oompaLoompaLiveData: MutableLiveData<OompaLoompa?> = MutableLiveData(oompaLoompa)

    init {
        Coroutines.main {
            val response = repository.getOompaLoompa(id)
            when (response) {
                is ResultResponse.Loading -> {
                }
                is ResultResponse.Failure -> {
                }
                is ResultResponse.Success -> {
                    val data = response.data as? OompaLoompa
                    data?.let {
                        oompaLoompa = it
                        oompaLoompaLiveData.postValue(oompaLoompa)
                    }
                }
            }
        }
    }

    fun getOompaLoompaLiveData(): LiveData<OompaLoompa?> = oompaLoompaLiveData
}