package ama.ripe.search.domain.usecase

import ama.ripe.search.domain.repository.RipeSearchRepository
import javax.inject.Inject

class GetAllOrganizationUseCase @Inject constructor(
    private val repository: RipeSearchRepository
) {
    suspend operator fun invoke() = repository.getOrgList()
}
