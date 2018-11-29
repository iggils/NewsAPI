package com.example.android.newsapi;

import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public  static class NewsPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_main);

            Preference newstype = findPreference(getString(R.string.settings_news_type));
            bindPreferenceSummaryToValue(newstype);
        }

        public boolean onPreferenceChange(Preference pref, Object val){
            String sValue = val.toString();
            if(pref instanceof  ListPreference)
            {
                ListPreference lPref = (ListPreference) pref;
                int prefIndex = lPref.findIndexOfValue(sValue);
                if(prefIndex >0){
                    CharSequence[] labels = lPref.getEntries();
                    pref.setSummary(labels[prefIndex]);
                }
            }
            else
            {
                pref.setSummary(sValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener((Preference.OnPreferenceChangeListener) this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

    }
}
