package ama.ripe.search.domain.usecase

import ama.ripe.search.domain.repository.RipeSearchRepository
import javax.inject.Inject

class GetCountOfOrgUseCase @Inject constructor(
    private val repository: RipeSearchRepository
) {
    operator fun invoke() = repository.getCountOfOrg()
}
