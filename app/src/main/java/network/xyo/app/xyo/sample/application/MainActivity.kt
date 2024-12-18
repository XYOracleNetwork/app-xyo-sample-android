package network.xyo.app.xyo.sample.application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import network.xyo.app.xyo.sample.application.databinding.ActivityItemDetailBinding
import network.xyo.client.settings.AccountPreferences
import network.xyo.client.settings.DefaultXyoSdkSettings
import network.xyo.client.settings.XyoSdk

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        class UpdatedAccountPreferences : AccountPreferences {
            override val fileName = "network-xyo-account-prefs"
            override val storagePath = "xyo-accounts"
        }

        class SampleAppSettings : DefaultXyoSdkSettings() {
            override val accountPreferences = UpdatedAccountPreferences()
        }

        XyoSdk.refresh(this.application, SampleAppSettings())

        val binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_item_detail) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_item_detail)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}