package ama.ripe.search.presentation

import ama.ripe.search.domain.usecase.GetInetNumsByOrgUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class InetNumViewModel @Inject constructor(
    private val getInetNumsByOrgUseCase: GetInetNumsByOrgUseCase
) : ViewModel() {

    val stateInetNum = getInetNumsByOrgUseCase.inetNumLiveDate()

    fun loadData(string: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getInetNumsByOrgUseCase(string)
        }
    }


}
