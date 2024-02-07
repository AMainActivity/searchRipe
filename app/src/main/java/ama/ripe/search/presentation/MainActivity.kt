package ama.ripe.search.presentation

import ama.ripe.search.R
import ama.ripe.search.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val component by lazy {
        (application as MyApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
       setBottomNavigation()
    }

private fun setBottomNavigation()
{
     val orgFragment = OrganizationFragment()
        val aboutFragment = AboutFragment()
        setCurrentFragment(orgFragment, true)
        binding.contentMain.bottomNavigationView.setOnItemSelectedListener {
            setCurrentFragment(
                when (it.itemId) {
                    R.id.page_1 -> orgFragment
                    R.id.page_2 -> aboutFragment
                    else -> orgFragment
                },
                false
            )
            true
        }
}

    private fun setCurrentFragment(fragment: Fragment, firstRun: Boolean) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_content_main, fragment)
            if (! firstRun) addToBackStack(null)
            commit()
        }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
