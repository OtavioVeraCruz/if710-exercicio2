package br.ufpe.cin.if710.rss

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment

class PrefsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prefs)
    }

    class PreferenceFrag:PreferenceFragment(){
        private var mListener: SharedPreferences.OnSharedPreferenceChangeListener? = null
        private var mUserNamePreference: Preference? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferencias)
        }

        companion object {
            protected val TAG = "PrefsFragment"
        }
    }
}
