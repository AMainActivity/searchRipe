package ama.ripe.search.presentation

import ama.ripe.search.domain.usecase.GetAllOrganizationUseCase
import ama.ripe.search.domain.usecase.GetOrganizationUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrganizationViewModel @Inject constructor(
    private val getOrganizationUseCase: GetOrganizationUseCase,
    private val getAllOrganizationUseCase: GetAllOrganizationUseCase
) : ViewModel() {

    val stateOrganization = getOrganizationUseCase.orgLiveDate()

    private fun loadAllData() {
        viewModelScope.launch(Dispatchers.Default) {
            getAllOrganizationUseCase()
        }
    }

    init {
        loadAllData()
    }

    fun loadData(stringOrg: String) {
        viewModelScope.launch(Dispatchers.Default) {
            getOrganizationUseCase(stringOrg.trim())    /*"87.226.162.216""rostelecom"*/

        }
    }
}
