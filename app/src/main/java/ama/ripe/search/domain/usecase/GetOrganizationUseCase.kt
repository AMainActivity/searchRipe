package ama.ripe.search.domain.usecase

import ama.ripe.search.domain.repository.RipeSearchRepository
import javax.inject.Inject

class GetOrganizationUseCase @Inject constructor(
    private val repository: RipeSearchRepository
) {

    suspend operator fun invoke(string: String) = repository.getOrganization(string)
    fun orgLiveDate() = repository.getOrganizationLivaData()
}
