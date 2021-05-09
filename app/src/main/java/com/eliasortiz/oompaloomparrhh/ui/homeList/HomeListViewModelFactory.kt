package com.eliasortiz.oompaloomparrhh.ui.homeList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eliasortiz.oompaloomparrhh.data.repositories.OompaLoompaRepository

@Suppress("UNCHECKED_CAST")
class HomeListViewModelFactory(
    private val repository: OompaLoompaRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeListViewModel(repository) as T
    }
}