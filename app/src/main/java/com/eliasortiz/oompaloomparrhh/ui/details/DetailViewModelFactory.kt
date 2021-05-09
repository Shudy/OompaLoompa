package com.eliasortiz.oompaloomparrhh.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository

class DetailViewModelFactory(
    private val repository: OompaLoompaRepository,
    private val id: Int
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(repository, id) as T
    }
}