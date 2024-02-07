package ama.ripe.search.presentation


sealed class StateLoading<out T> {
    object Initial : StateLoading<Nothing>()
    object Loading : StateLoading<Nothing>()
    data class Content<T>(val currencyList: List<T>) : StateLoading<T>()
    data class ContentError(val er: String) : StateLoading<Nothing>()
}
