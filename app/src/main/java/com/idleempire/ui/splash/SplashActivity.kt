package com.idleempire.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.appopen.AppOpenAd
import com.idleempire.R
import com.idleempire.ads.AdManager
import com.idleempire.ui.activities.MainActivity

/**
 * Splash screen that:
 * 1. Initializes AdMob
 * 2. Shows an App Open Ad (high CPM!)
 * 3. Navigates to MainActivity
 */
class SplashActivity : AppCompatActivity() {

    private var appOpenAd: AppOpenAd? = null
    private var adShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Initialize AdMob
        AdManager.initialize(this)

        // Load App Open Ad
        loadAppOpenAd()

        // Fallback: go to main after 3 seconds even if ad fails
        Handler(Looper.getMainLooper()).postDelayed({
            if (!adShown) goToMain()
        }, 3000)
    }

    private fun loadAppOpenAd() {
        // Test App Open Ad Unit ID - replace with real one!
        val adUnitId = "ca-app-pub-3940256099942544/9257395921"

        AppOpenAd.load(
            this,
            adUnitId,
            AdRequest.Builder().build(),
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    showAppOpenAd()
                }
                override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                    goToMain()
                }
            }
        )
    }

    private fun showAppOpenAd() {
        val ad = appOpenAd ?: run { goToMain(); return }
        ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                goToMain()
            }
            override fun onAdFailedToShowFullScreenContent(error: com.google.android.gms.ads.AdError) {
                goToMain()
            }
        }
        adShown = true
        ad.show(this)
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
