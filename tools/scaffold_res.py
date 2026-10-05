"""Writes small static resource files (shapes, launcher icon, backup rules, manifest). Run once."""
import os

RES = os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "app", "src", "main", "res")

FILES = {
    "values/bools.xml": '<resources><bool name="light_bars">true</bool></resources>\n',
    "values-night/bools.xml": '<resources><bool name="light_bars">false</bool></resources>\n',
    "values/dimens.xml": """<resources>
    <dimen name="screen_pad">16dp</dimen>
    <dimen name="card_radius">16dp</dimen>
    <dimen name="button_height">52dp</dimen>
    <dimen name="content_max_width">640dp</dimen>
</resources>
""",
    "color/btn_primary_text.xml": """<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_enabled="false" android:color="@color/muted" />
    <item android:color="@color/on_primary" />
</selector>
""",
    "drawable/bg_card.xml": """<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="@dimen/card_radius" />
    <solid android:color="@color/surface" />
    <stroke android:width="1dp" android:color="@color/outline" />
</shape>
""",
    "drawable/bg_dialog.xml": """<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="28dp" />
    <solid android:color="@color/surface" />
</shape>
""",
    "drawable/bg_sheet.xml": """<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:topLeftRadius="24dp" android:topRightRadius="24dp" />
    <solid android:color="@color/surface" />
</shape>
""",
    "drawable/bg_bottom_nav.xml": """<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item><shape><solid android:color="@color/outline" /></shape></item>
    <item android:top="1dp"><shape><solid android:color="@color/surface" /></shape></item>
</layer-list>
""",
    "drawable/bg_nav_indicator.xml": """<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="100dp" />
    <solid android:color="@color/primary_container" />
</shape>
""",
    "drawable/bg_hero.xml": """<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="24dp" />
    <gradient android:angle="315" android:startColor="@color/primary_deep" android:endColor="@color/primary" />
</shape>
""",
    "drawable/btn_primary.xml": """<ripple xmlns:android="http://schemas.android.com/apk/res/android" android:color="@color/ripple_on_primary">
    <item>
        <selector>
            <item android:state_enabled="false"><shape><corners android:radius="14dp" /><solid android:color="@color/surface_variant" /></shape></item>
            <item><shape><corners android:radius="14dp" /><solid android:color="@color/primary" /></shape></item>
        </selector>
    </item>
</ripple>
""",
    "drawable/btn_secondary.xml": """<ripple xmlns:android="http://schemas.android.com/apk/res/android" android:color="@color/ripple">
    <item><shape><corners android:radius="14dp" /><solid android:color="@color/surface" /><stroke android:width="1.5dp" android:color="@color/outline" /></shape></item>
</ripple>
""",
    "drawable/btn_on_hero.xml": """<ripple xmlns:android="http://schemas.android.com/apk/res/android" android:color="@color/ripple">
    <item><shape><corners android:radius="14dp" /><solid android:color="#FFFFFF" /></shape></item>
</ripple>
""",
    "drawable/ripple_rect.xml": """<ripple xmlns:android="http://schemas.android.com/apk/res/android" android:color="@color/ripple">
    <item android:id="@android:id/mask"><shape><corners android:radius="16dp" /><solid android:color="#FFFFFF" /></shape></item>
</ripple>
""",
    "drawable/bg_pill.xml": """<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="100dp" />
    <solid android:color="@color/surface_variant" />
</shape>
""",
    "drawable/ic_launcher_background.xml": """<vector xmlns:android="http://schemas.android.com/apk/res/android" android:width="108dp" android:height="108dp" android:viewportWidth="108" android:viewportHeight="108">
    <path android:fillColor="#12857A" android:pathData="M0,0h108v108h-108z" />
    <path android:fillColor="#0E7369" android:pathData="M0,72 C30,60 70,84 108,70 L108,108 L0,108 Z" />
</vector>
""",
    "drawable/ic_launcher_foreground.xml": """<vector xmlns:android="http://schemas.android.com/apk/res/android" android:width="108dp" android:height="108dp" android:viewportWidth="108" android:viewportHeight="108">
    <path android:fillColor="#FFB238" android:pathData="M33,46 L36,27 L50,38 Z" />
    <path android:fillColor="#FFB238" android:pathData="M75,46 L72,27 L58,38 Z" />
    <path android:fillColor="#FFC85C" android:pathData="M54,34 C70,34 80,46 80,60 C80,74 68,81 54,81 C40,81 28,74 28,60 C28,46 38,34 54,34 Z" />
    <path android:fillColor="#FFE9C2" android:pathData="M54,60 C62,60 68,65 68,70 C68,76 62,79 54,79 C46,79 40,76 40,70 C40,65 46,60 54,60 Z" />
    <path android:fillColor="#2B2118" android:pathData="M44,56 m-4.2,0 a4.2,4.2 0,1 1,8.4 0 a4.2,4.2 0,1 1,-8.4 0" />
    <path android:fillColor="#2B2118" android:pathData="M64,56 m-4.2,0 a4.2,4.2 0,1 1,8.4 0 a4.2,4.2 0,1 1,-8.4 0" />
    <path android:fillColor="#FFFFFF" android:pathData="M45.6,54.4 m-1.5,0 a1.5,1.5 0,1 1,3 0 a1.5,1.5 0,1 1,-3 0" />
    <path android:fillColor="#FFFFFF" android:pathData="M65.6,54.4 m-1.5,0 a1.5,1.5 0,1 1,3 0 a1.5,1.5 0,1 1,-3 0" />
    <path android:fillColor="#F59A9A" android:pathData="M38,64 m-3.6,0 a3.6,2.4 0,1 1,7.2 0 a3.6,2.4 0,1 1,-7.2 0" />
    <path android:fillColor="#F59A9A" android:pathData="M70,64 m-3.6,0 a3.6,2.4 0,1 1,7.2 0 a3.6,2.4 0,1 1,-7.2 0" />
    <path android:strokeColor="#2B2118" android:strokeWidth="2.2" android:strokeLineCap="round" android:fillColor="#00000000" android:pathData="M49,65 Q54,70 59,65" />
</vector>
""",
    "mipmap-anydpi-v26/ic_launcher.xml": """<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
""",
    "mipmap/ic_launcher.xml": """<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/ic_launcher_background" />
    <item android:drawable="@drawable/ic_launcher_foreground" />
</layer-list>
""",
    "xml/backup_rules.xml": """<full-backup-content>
    <include domain="sharedpref" path="." />
    <include domain="database" path="learning.db" />
</full-backup-content>
""",
    "xml/data_extraction_rules.xml": """<data-extraction-rules>
    <cloud-backup><include domain="sharedpref" path="." /><include domain="database" path="learning.db" /></cloud-backup>
    <device-transfer><include domain="sharedpref" path="." /><include domain="database" path="learning.db" /></device-transfer>
</data-extraction-rules>
""",
    "../AndroidManifest.xml": """<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="com.google.android.gms.permission.AD_ID" />
    <!-- Opt-in daily reminder only; requested after the first completed session. -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <queries>
        <intent><action android:name="android.intent.action.SENDTO" /><data android:scheme="mailto" /></intent>
        <intent><action android:name="android.intent.action.VIEW" /><data android:scheme="https" /></intent>
        <intent><action android:name="android.intent.action.TTS_SERVICE" /></intent>
    </queries>

    <application
        android:name=".EnglishApp"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:roundIcon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.App"
        tools:targetApi="34">

        <activity
            android:name=".MainActivity"
            android:configChanges="orientation|screenSize|screenLayout|smallestScreenSize|keyboardHidden"
            android:exported="true"
            android:label="@string/app_short_name"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <receiver android:name=".notify.ReminderReceiver" android:exported="false" />

        <meta-data android:name="com.google.android.gms.ads.APPLICATION_ID" android:value="${admobAppId}" />
        <meta-data android:name="com.google.android.gms.ads.flag.OPTIMIZE_INITIALIZATION" android:value="true" />
        <meta-data android:name="com.google.android.gms.ads.flag.OPTIMIZE_AD_LOADING" android:value="true" />
    </application>
</manifest>
""",
}

for rel, text in FILES.items():
    path = os.path.normpath(os.path.join(RES, rel))
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        f.write(text)
print(f"wrote {len(FILES)} files")
