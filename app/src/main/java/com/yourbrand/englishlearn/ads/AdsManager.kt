package com.yourbrand.englishlearn.ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.widget.FrameLayout
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.yourbrand.englishlearn.BuildConfig
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

object AdConfig {
    private const val TEST_BANNER = "ca-app-pub-3940256099942544/9214589741"
    private const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
    private const val TEST_REWARDED = "ca-app-pub-3940256099942544/5224354917"
    private val test get() = BuildConfig.FORCE_TEST_ADS
    val banner: String get() = if (test) TEST_BANNER else BuildConfig.AD_BANNER
    val interstitial: String get() = if (test) TEST_INTERSTITIAL else BuildConfig.AD_INTERSTITIAL
    val rewarded: String get() = if (test) TEST_REWARDED else BuildConfig.AD_REWARDED
}

/**
 * Consent (UMP every launch), SDK init after consent and off the main thread, adaptive banner,
 * interstitial between sessions and user-initiated rewarded ads. Failures simply leave no ad.
 */
class AdsManager(private val app: Application) {
    private val prefs = app.getSharedPreferences("ads_v1", Context.MODE_PRIVATE)
    val policy = AdPolicy(readState())
    private val consentInfo: ConsentInformation by lazy { UserMessagingPlatform.getConsentInformation(app) }
    private val main = Handler(Looper.getMainLooper())
    private val initStarted = AtomicBoolean(false)
    private val readyListeners = mutableListOf<() -> Unit>()

    var isInitialized = false
        private set
    var isShowingFullScreen = false
        private set

    /** Debug screenshots hide all ads. */
    var demoNoAds = false

    val showsAds: Boolean get() = BuildConfig.ADS_ENABLED && !demoNoAds

    fun gatherConsent(activity: Activity, done: () -> Unit = {}) {
        if (!showsAds) { done(); return }
        val params = ConsentRequestParameters.Builder().build()
        consentInfo.requestConsentInfoUpdate(activity, params, {
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { initializeIfAllowed(); done() }
        }, { initializeIfAllowed(); done() })
        initializeIfAllowed()
    }

    val privacyOptionsRequired: Boolean
        get() = runCatching { consentInfo.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED }.getOrDefault(false)

    fun showPrivacyOptions(activity: Activity) { UserMessagingPlatform.showPrivacyOptionsForm(activity) { } }

    private fun initializeIfAllowed() {
        if (!consentInfo.canRequestAds() || !initStarted.compareAndSet(false, true)) return
        thread(name = "ads-init", priority = Thread.MIN_PRIORITY) {
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder()
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
                    .build()
            )
            MobileAds.initialize(app) {
                main.post {
                    isInitialized = true
                    readyListeners.toList().forEach { it() }
                    readyListeners.clear()
                    preloadInterstitial()
                }
            }
        }
    }

    private fun whenReady(block: () -> Unit) { if (isInitialized) block() else readyListeners += block }

    // ---- banner -------------------------------------------------------------------------------

    fun loadBanner(activity: Activity, container: FrameLayout, onLoaded: () -> Unit) {
        whenReady {
            if (activity.isFinishing || activity.isDestroyed || !showsAds || container.childCount > 0) return@whenReady
            val metrics = activity.resources.displayMetrics
            val widthPx = if (container.width > 0) container.width else metrics.widthPixels
            val adWidthDp = (widthPx / metrics.density).toInt().coerceAtMost(728)
            val view = AdView(activity).apply {
                adUnitId = AdConfig.banner
                setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidthDp))
                adListener = object : AdListener() { override fun onAdLoaded() { onLoaded() } }
            }
            container.addView(view, FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER))
            view.loadAd(AdRequest.Builder().build())
        }
    }

    fun pauseBanner(c: FrameLayout) { (c.getChildAt(0) as? AdView)?.pause() }
    fun resumeBanner(c: FrameLayout) { (c.getChildAt(0) as? AdView)?.resume() }
    fun destroyBanner(c: FrameLayout) { (c.getChildAt(0) as? AdView)?.destroy(); c.removeAllViews() }

    // ---- interstitial -------------------------------------------------------------------------

    private var interstitial: InterstitialAd? = null
    private var interstitialLoading = false
    private var interstitialFailures = 0

    fun preloadInterstitial() {
        if (!isInitialized || interstitial != null || interstitialLoading || interstitialFailures >= 3 || !showsAds) return
        interstitialLoading = true
        InterstitialAd.load(app, AdConfig.interstitial, AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) { interstitial = ad; interstitialLoading = false; interstitialFailures = 0 }
            override fun onAdFailedToLoad(error: LoadAdError) {
                interstitialLoading = false; interstitialFailures++
                main.postDelayed({ preloadInterstitial() }, 4_000L shl interstitialFailures)
            }
        })
    }

    /** Called when a session ends, before its result screen. [then] always runs exactly once. */
    fun onSessionFinished(activity: Activity, lastAnswerWrong: Boolean, fromMock: Boolean, fromPetHome: Boolean, then: () -> Unit) {
        val allowed = showsAds && policy.onSessionFinished(lastAnswerWrong, fromMock, fromPetHome)
        save()
        val ad = interstitial
        if (!allowed || ad == null || !isInitialized) { then(); preloadInterstitial(); return }
        interstitial = null
        var done = false
        val finish = { if (!done) { done = true; isShowingFullScreen = false; then(); preloadInterstitial() } }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() { isShowingFullScreen = true; policy.onInterstitialShown(); save() }
            override fun onAdDismissedFullScreenContent() { finish() }
            override fun onAdFailedToShowFullScreenContent(error: AdError) { finish() }
        }
        ad.show(activity)
    }

    // ---- rewarded -----------------------------------------------------------------------------

    private var rewarded: RewardedAd? = null
    private var rewardedLoading = false
    private var rewardedFailures = 0
    val rewardedReady: Boolean get() = rewarded != null

    fun preloadRewarded(onReady: (() -> Unit)? = null) {
        if (!showsAds) return
        whenReady {
            if (rewarded != null) { onReady?.invoke(); return@whenReady }
            if (rewardedLoading || rewardedFailures >= 3) return@whenReady
            rewardedLoading = true
            RewardedAd.load(app, AdConfig.rewarded, AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) { rewarded = ad; rewardedLoading = false; rewardedFailures = 0; onReady?.invoke() }
                override fun onAdFailedToLoad(error: LoadAdError) { rewardedLoading = false; rewardedFailures++ }
            })
        }
    }

    fun showRewarded(activity: Activity, onRewarded: () -> Unit, onClosed: () -> Unit = {}) {
        val ad = rewarded ?: run { onClosed(); return }
        rewarded = null
        var earned = false
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() { isShowingFullScreen = true }
            override fun onAdDismissedFullScreenContent() {
                isShowingFullScreen = false
                if (earned) onRewarded()
                onClosed()
                preloadRewarded()
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) { isShowingFullScreen = false; onClosed() }
        }
        ad.show(activity) { earned = true }
    }

    // ---- persistence --------------------------------------------------------------------------

    private fun readState(): AdState = runCatching {
        val o = JSONObject(prefs.getString("s", "{}").orEmpty())
        AdState(o.optInt("sessions"), o.optLong("lastAt"), o.optLong("day", -1), o.optInt("dayCount"))
    }.getOrElse { AdState() }

    private fun save() {
        val s = policy.state
        prefs.edit().putString("s", JSONObject().put("sessions", s.sessionsFinished).put("lastAt", s.lastInterstitialAt)
            .put("day", s.dayKey).put("dayCount", s.dayCount).toString()).apply()
    }
}
