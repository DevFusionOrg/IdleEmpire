package com.idleempire

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.idleempire.data.db.GameDatabase

class IdleEmpireApp : Application() {

    val database: GameDatabase by lazy { GameDatabase.getDatabase(this) }

    override fun onCreate() {
        super.onCreate()
        // Initialize AdMob
        MobileAds.initialize(this) {}
    }
}
