package ama.ripe.search.presentation

import ama.ripe.search.domain.usecase.GetCountOfOrgUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val getCountOfOrg: GetCountOfOrgUseCase
) : ViewModel() {

    private val _canStart = MutableLiveData<Unit>()
    val canStart: LiveData<Unit>
        get() = _canStart

    init {
        val d1 = viewModelScope.async(Dispatchers.IO) {
            getCountOfOrg()
        }

        viewModelScope.launch {
            d1.await()
            _canStart.value = Unit
        }
    }
}
