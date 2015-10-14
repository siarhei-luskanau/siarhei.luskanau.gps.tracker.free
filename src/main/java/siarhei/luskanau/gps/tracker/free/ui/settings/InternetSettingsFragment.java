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
        onSendToServerIntervalPreferenceCreate();
    }

    private void onInternetTypePreferenceCreate() {
        AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_internet_type));
        CharSequence[] entries = listPreference.getEntries();
        switch (state.internetSettingsEntity.internetType) {
            default:
            case ANY_TYPE: {
                listPreference.setValueIndex(0);
                listPreference.setSummary(entries[0]);
                break;
            }
            case WIFI_TYPE: {
                listPreference.setValueIndex(1);
                listPreference.setSummary(entries[1]);
                break;
            }
            case OFFLINE_TYPE: {
                listPreference.setValueIndex(2);
                listPreference.setSummary(entries[2]);
                break;
            }
        }
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_internet_type));
                listPreference.setSummary(newValue.toString());
                AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
                switch (listPreference.findIndexOfValue(newValue.toString())) {
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

    private void onSendToServerIntervalPreferenceCreate() {
        AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_send_to_server_interval));
        CharSequence[] entries = listPreference.getEntries();
        switch (state.internetSettingsEntity.sendToServerInterval) {
            default:
            case AppSettings.SendToServerInterval.IMMEDIATELY: {
                listPreference.setValueIndex(0);
                listPreference.setSummary(entries[0]);
                break;
            }
            case AppSettings.SendToServerInterval.MINUTES_1: {
                listPreference.setValueIndex(1);
                listPreference.setSummary(entries[1]);
                break;
            }
            case AppSettings.SendToServerInterval.MINUTES_10: {
                listPreference.setValueIndex(2);
                listPreference.setSummary(entries[2]);
                break;
            }
            case AppSettings.SendToServerInterval.MINUTES_60: {
                listPreference.setValueIndex(3);
                listPreference.setSummary(entries[3]);
                break;
            }
            case AppSettings.SendToServerInterval.MANUAL: {
                listPreference.setValueIndex(4);
                listPreference.setSummary(entries[4]);
                break;
            }
        }
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_send_to_server_interval));
                listPreference.setSummary(newValue.toString());
                AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
                switch (listPreference.findIndexOfValue(newValue.toString())) {
                    case 0: {
                        state.internetSettingsEntity.sendToServerInterval = AppSettings.SendToServerInterval.IMMEDIATELY;
                        break;
                    }
                    case 1: {
                        state.internetSettingsEntity.sendToServerInterval = AppSettings.SendToServerInterval.MINUTES_1;
                        break;
                    }
                    case 2: {
                        state.internetSettingsEntity.sendToServerInterval = AppSettings.SendToServerInterval.MINUTES_10;
                        break;
                    }
                    case 3: {
                        state.internetSettingsEntity.sendToServerInterval = AppSettings.SendToServerInterval.MINUTES_60;
                        break;
                    }
                    case 4: {
                        state.internetSettingsEntity.sendToServerInterval = AppSettings.SendToServerInterval.MANUAL;
                        break;
                    }
                }
                AppSettings.setAppSettingsEntity(getContext(), state);
                return true;
            }
        });
    }

}