package com.idleempire.ads

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object AdManager {

    private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3633447128777673/5048955384"
    private const val REWARDED_AD_UNIT_ID     = "ca-app-pub-3633447128777673/9118064463"
    private const val BANNER_AD_UNIT_ID       = "ca-app-pub-3633447128777673/7629617779"

    private var interstitialAd: InterstitialAd? = null
    private var rewardedAd: RewardedAd? = null
    private var interstitialClickCount = 0

    fun initialize(context: Context) {
        MobileAds.initialize(context) {}
    }

    fun getBannerAdUnitId(): String = BANNER_AD_UNIT_ID
    fun createBannerAdRequest(): AdRequest = AdRequest.Builder().build()

    fun loadBanner(container: ViewGroup, context: Context) {
        val adView = AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = BANNER_AD_UNIT_ID
        }
        container.addView(adView)
        adView.loadAd(createBannerAdRequest())
    }

    fun loadInterstitial(context: Context) {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(context, INTERSTITIAL_AD_UNIT_ID, request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                }
            })
    }

    fun showInterstitial(activity: Activity) {
        interstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitial(activity)
                }
                override fun onAdFailedToShowFullScreenContent(e: AdError) {
                    interstitialAd = null
                    loadInterstitial(activity)
                }
            }
            ad.show(activity)
        } ?: loadInterstitial(activity)
    }

    fun onUserAction(activity: Activity) {
        interstitialClickCount++
        if (interstitialClickCount >= 3) {
            showInterstitial(activity)
            interstitialClickCount = 0
        }
    }

    fun loadRewarded(context: Context) {
        val request = AdRequest.Builder().build()
        RewardedAd.load(context, REWARDED_AD_UNIT_ID, request,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }
                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            })
    }

    fun isRewardedAdReady(): Boolean = rewardedAd != null

    fun showRewarded(activity: Activity, onRewarded: () -> Unit, onNotAvailable: (() -> Unit)? = null) {
        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    loadRewarded(activity)
                }
                override fun onAdFailedToShowFullScreenContent(e: AdError) {
                    rewardedAd = null
                    loadRewarded(activity)
                    onNotAvailable?.invoke()
                }
            }
            ad.show(activity) { onRewarded() }
        } ?: run {
            onNotAvailable?.invoke()
            loadRewarded(activity)
        }
    }
}
