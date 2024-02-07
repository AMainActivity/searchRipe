package ama.ripe.search.domain.usecase

import ama.ripe.search.domain.repository.RipeSearchRepository
import javax.inject.Inject

class GetInetNumsByOrgUseCase @Inject constructor(
    private val repository: RipeSearchRepository
) {

    suspend operator fun invoke(string: String) = repository.getInetNumByOrg(string)


    fun inetNumLiveDate() = repository.getInetNumLivaData()
}
