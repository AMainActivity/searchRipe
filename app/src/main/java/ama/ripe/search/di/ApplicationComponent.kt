package ama.ripe.search.di


import ama.ripe.search.presentation.AboutFragment
import ama.ripe.search.presentation.InetNumFragment
import ama.ripe.search.presentation.MainActivity
import ama.ripe.search.presentation.MyApp
import ama.ripe.search.presentation.OrganizationFragment
import ama.ripe.search.presentation.SplashActivity
import android.app.Application
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: SplashActivity)
    fun inject(organizationFragment: OrganizationFragment)
    fun inject(aboutFragment: AboutFragment)
    fun inject(inetNumFragment: InetNumFragment)
    fun inject(application: MyApp)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}