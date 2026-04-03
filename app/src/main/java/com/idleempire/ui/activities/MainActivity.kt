package com.idleempire.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.idleempire.R
import com.idleempire.ads.AdManager
import com.idleempire.databinding.ActivityMainBinding
import com.idleempire.ui.viewmodel.GameViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]

        // Setup bottom nav + nav graph
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        // Setup banner ad - size and ID are already set in XML
        binding.bannerAdView.loadAd(AdManager.createBannerAdRequest())

        // Preload interstitial and rewarded ads
        AdManager.loadInterstitial(this)
        AdManager.loadRewarded(this)

        // Show offline earnings dialog if any
        viewModel.offlineEarnings.observe(this) { earned ->
            if (earned > 0) {
                showOfflineEarningsDialog(earned)
            }
        }
    }

    private fun showOfflineEarningsDialog(earned: Double) {
        val formatted = viewModel.formatCoins(earned)
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Welcome Back! 🎉")
            .setMessage("Your businesses earned $formatted coins while you were away!")
            .setPositiveButton("Collect 2x (Watch Ad)") { _, _ ->
                AdManager.showRewarded(this,
                    onRewarded = { viewModel.claimOfflineEarnings(2.0) },
                    onNotAvailable = { viewModel.claimOfflineEarnings(1.0) }
                )
            }
            .setNegativeButton("Collect ($formatted)") { _, _ ->
                viewModel.claimOfflineEarnings(1.0)
            }
            .setCancelable(false)
            .show()
    }

    // Called from fragments after purchases
    fun trackUserAction() {
        AdManager.onUserAction(this)
    }
}
