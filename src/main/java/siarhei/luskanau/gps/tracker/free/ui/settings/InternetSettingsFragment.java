/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Siarhei Luskanau
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package siarhei.luskanau.gps.tracker.free.ui.settings;

import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;

public class InternetSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_internet);

        onInternetTypePreferenceCreate();
    }

    private void onInternetTypePreferenceCreate() {
        AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
        ListPreference internetTypePreference = (ListPreference) findPreference(getString(R.string.preference_key_internet_type));
        switch (state.internetSettingsEntity.internetType) {
            case ANY_TYPE: {
                internetTypePreference.setValueIndex(0);
                internetTypePreference.setSummary(R.string.preference_internet_any);
                break;
            }
            case WIFI_TYPE: {
                internetTypePreference.setValueIndex(1);
                internetTypePreference.setSummary(R.string.preference_internet_wifi);
                break;
            }
            case OFFLINE_TYPE: {
                internetTypePreference.setValueIndex(2);
                internetTypePreference.setSummary(R.string.preference_internet_offline);
                break;
            }
            default: {
                internetTypePreference.setSummary(state.internetSettingsEntity.internetType.toString());
                break;
            }
        }
        internetTypePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference internetTypePreference = (ListPreference) findPreference(getString(R.string.preference_key_internet_type));
                internetTypePreference.setSummary(newValue.toString());
                AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
                switch (internetTypePreference.findIndexOfValue(newValue.toString())) {
                    case 0: {
                        state.internetSettingsEntity.internetType = AppSettings.InternetType.ANY_TYPE;
                        break;
                    }
                    case 1: {
                        state.internetSettingsEntity.internetType = AppSettings.InternetType.WIFI_TYPE;
                        break;
                    }
                    case 2: {
                        state.internetSettingsEntity.internetType = AppSettings.InternetType.OFFLINE_TYPE;
                        break;
                    }
                }
                AppSettings.setAppSettingsEntity(getContext(), state);
                return true;
            }
        });
    }

}