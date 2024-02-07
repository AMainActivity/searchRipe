package ama.ripe.search.di

import ama.ripe.search.presentation.InetNumViewModel
import ama.ripe.search.presentation.OrganizationViewModel
import ama.ripe.search.presentation.SplashViewModel
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(OrganizationViewModel::class)
    fun bindOrganizationViewModel(viewModel: OrganizationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(InetNumViewModel::class)
    fun bindInetNumViewModel(viewModel: InetNumViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel
}