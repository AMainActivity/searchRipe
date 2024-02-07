package ama.ripe.search.di

import ama.ripe.search.data.database.AppDatabase
import ama.ripe.search.data.database.InetNumObjectDao
import ama.ripe.search.data.database.IpDao
import ama.ripe.search.data.database.OrgObjectDao
import ama.ripe.search.data.network.RipeSearchApiFactory
import ama.ripe.search.data.network.RipeSearchApiService
import ama.ripe.search.data.repository.RipeSearchRepositoryImpl
import ama.ripe.search.domain.repository.RipeSearchRepository
import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @Binds
    @ApplicationScope
    fun bindRepository(impl: RipeSearchRepositoryImpl): RipeSearchRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideOrgObjectDao(
            application: Application
        ): OrgObjectDao {
            return AppDatabase.getInstance(application).orgObjectDao()
        }

        @Provides
        @ApplicationScope
        fun provideInetNumObjectDao(
            application: Application
        ): InetNumObjectDao {
            return AppDatabase.getInstance(application).inetNumObjectDao()
        }

        @Provides
        @ApplicationScope
        fun provideIpDao(
            application: Application
        ): IpDao {
            return AppDatabase.getInstance(application).ipDao()
        }

        @Provides
        @ApplicationScope
        fun provideApiService(): RipeSearchApiService {
            return RipeSearchApiFactory.apiService
        }

    }
}
