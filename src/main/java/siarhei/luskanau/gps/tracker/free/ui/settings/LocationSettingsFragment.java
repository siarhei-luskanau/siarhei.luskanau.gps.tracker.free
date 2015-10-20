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
import android.support.v7.preference.SwitchPreferenceCompat;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;

public class LocationSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_location);

        onGpsFilterPreferenceCreate();
        onWirelessFilterPreferenceCreate();
        onGsmFilterPreferenceCreate();
        onTimeIntervalPreferenceCreate();
    }

    private void onGpsFilterPreferenceCreate() {
        AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_location_filter_gps));
        CharSequence[] entries = listPreference.getEntries();
        switch (state.locationSettings.filterGpsLocations) {
            default:
            case AppSettings.FilterGpsLocations.DONT_USE: {
                listPreference.setValueIndex(0);
                listPreference.setSummary(entries[0]);
                listPreference.setIcon(R.drawable.ic_location_off_24dp);
                break;
            }
            case AppSettings.FilterGpsLocations.USE: {
                listPreference.setValueIndex(1);
                listPreference.setSummary(entries[1]);
                listPreference.setIcon(R.drawable.ic_location_on_24dp);
                break;
            }
            case AppSettings.FilterGpsLocations.FILTER_10_M: {
                listPreference.setValueIndex(2);
                listPreference.setSummary(entries[2]);
                listPreference.setIcon(R.drawable.ic_location_on_24dp);
                break;
            }
            case AppSettings.FilterGpsLocations.FILTER_100_M: {
                listPreference.setValueIndex(3);
                listPreference.setSummary(entries[3]);
                listPreference.setIcon(R.drawable.ic_location_on_24dp);
                break;
            }
            case AppSettings.FilterGpsLocations.FILTER_1000_M: {
                listPreference.setValueIndex(4);
                listPreference.setSummary(entries[4]);
                listPreference.setIcon(R.drawable.ic_location_on_24dp);
                break;
            }
        }
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_location_filter_gps));
                listPreference.setSummary(newValue.toString());
                AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
                switch (listPreference.findIndexOfValue(newValue.toString())) {
                    case 0: {
                        state.locationSettings.filterGpsLocations = AppSettings.FilterGpsLocations.DONT_USE;
                        listPreference.setIcon(R.drawable.ic_location_off_24dp);
                        break;
                    }
                    case 1: {
                        state.locationSettings.filterGpsLocations = AppSettings.FilterGpsLocations.USE;
                        listPreference.setIcon(R.drawable.ic_location_on_24dp);
                        break;
                    }
                    case 2: {
                        state.locationSettings.filterGpsLocations = AppSettings.FilterGpsLocations.FILTER_10_M;
                        listPreference.setIcon(R.drawable.ic_location_on_24dp);
                        break;
                    }
                    case 3: {
                        state.locationSettings.filterGpsLocations = AppSettings.FilterGpsLocations.FILTER_100_M;
                        listPreference.setIcon(R.drawable.ic_location_on_24dp);
                        break;
                    }
                    case 4: {
                        state.locationSettings.filterGpsLocations = AppSettings.FilterGpsLocations.FILTER_1000_M;
                        listPreference.setIcon(R.drawable.ic_location_on_24dp);
                        break;
                    }
                }
                AppSettings.setAppSettingsEntity(getContext(), state);
                return true;
            }
        });
    }

    private void onWirelessFilterPreferenceCreate() {
        AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_location_filter_network));
        CharSequence[] entries = listPreference.getEntries();
        switch (state.locationSettings.filterNetworkLocations) {
            default:
            case AppSettings.FilterNetworkLocations.DONT_USE: {
                listPreference.setValueIndex(0);
                listPreference.setSummary(entries[0]);
                listPreference.setIcon(R.drawable.ic_signal_wifi_off_24dp);
                break;
            }
            case AppSettings.FilterNetworkLocations.USE: {
                listPreference.setValueIndex(1);
                listPreference.setSummary(entries[1]);
                listPreference.setIcon(R.drawable.ic_signal_wifi_4_bar_24dp);
                break;
            }
            case AppSettings.FilterNetworkLocations.FILTER_100_M: {
                listPreference.setValueIndex(2);
                listPreference.setSummary(entries[2]);
                listPreference.setIcon(R.drawable.ic_signal_wifi_4_bar_24dp);
                break;
            }
            case AppSettings.FilterNetworkLocations.FILTER_500_M: {
                listPreference.setValueIndex(3);
                listPreference.setSummary(entries[3]);
                listPreference.setIcon(R.drawable.ic_signal_wifi_4_bar_24dp);
                break;
            }
            case AppSettings.FilterNetworkLocations.FILTER_5000_M: {
                listPreference.setValueIndex(4);
                listPreference.setSummary(entries[4]);
                listPreference.setIcon(R.drawable.ic_signal_wifi_4_bar_24dp);
                break;
            }
        }
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_location_filter_network));
                listPreference.setSummary(newValue.toString());
                AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
                switch (listPreference.findIndexOfValue(newValue.toString())) {
                    case 0: {
                        state.locationSettings.filterNetworkLocations = AppSettings.FilterNetworkLocations.DONT_USE;
                        listPreference.setIcon(R.drawable.ic_signal_wifi_off_24dp);
                        break;
                    }
                    case 1: {
                        state.locationSettings.filterNetworkLocations = AppSettings.FilterNetworkLocations.USE;
                        listPreference.setIcon(R.drawable.ic_signal_wifi_4_bar_24dp);
                        break;
                    }
                    case 2: {
                        state.locationSettings.filterNetworkLocations = AppSettings.FilterNetworkLocations.FILTER_100_M;
                        listPreference.setIcon(R.drawable.ic_signal_wifi_4_bar_24dp);
                        break;
                    }
                    case 3: {
                        state.locationSettings.filterNetworkLocations = AppSettings.FilterNetworkLocations.FILTER_500_M;
                        listPreference.setIcon(R.drawable.ic_signal_wifi_4_bar_24dp);
                        break;
                    }
                    case 4: {
                        state.locationSettings.filterNetworkLocations = AppSettings.FilterNetworkLocations.FILTER_5000_M;
                        listPreference.setIcon(R.drawable.ic_signal_wifi_4_bar_24dp);
                        break;
                    }
                }
                AppSettings.setAppSettingsEntity(getContext(), state);
                return true;
            }
        });
    }

    private void onGsmFilterPreferenceCreate() {
        AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
        SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) findPreference(getString(R.string.preference_key_location_filter_gsm));
        switchPreferenceCompat.setChecked(state.locationSettings.isUseGsmCellInfo);
        if (state.locationSettings.isUseGsmCellInfo) {
            switchPreferenceCompat.setIcon(R.drawable.ic_signal_cellular_4_bar_24dp);
        } else {
            switchPreferenceCompat.setIcon(R.drawable.ic_signal_cellular_off_24dp);
        }
        switchPreferenceCompat.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
                state.locationSettings.isUseGsmCellInfo = Boolean.parseBoolean(newValue.toString());
                AppSettings.setAppSettingsEntity(getContext(), state);
                SwitchPreferenceCompat switchPreferenceCompat = (SwitchPreferenceCompat) findPreference(getString(R.string.preference_key_location_filter_gsm));
                if (state.locationSettings.isUseGsmCellInfo) {
                    switchPreferenceCompat.setIcon(R.drawable.ic_signal_cellular_4_bar_24dp);
                } else {
                    switchPreferenceCompat.setIcon(R.drawable.ic_signal_cellular_off_24dp);
                }
                return true;
            }
        });
    }

    private void onTimeIntervalPreferenceCreate() {
        AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_location_filter_time));
        CharSequence[] entries = listPreference.getEntries();
        switch (state.locationSettings.timeFilter) {
            default:
            case AppSettings.FilterTimeInterval.IMMEDIATELY: {
                listPreference.setValueIndex(0);
                listPreference.setSummary(entries[0]);
                break;
            }
            case AppSettings.FilterTimeInterval.MINUTES_1: {
                listPreference.setValueIndex(1);
                listPreference.setSummary(entries[1]);
                break;
            }
            case AppSettings.FilterTimeInterval.MINUTES_10: {
                listPreference.setValueIndex(2);
                listPreference.setSummary(entries[2]);
                break;
            }
            case AppSettings.FilterTimeInterval.MINUTES_60: {
                listPreference.setValueIndex(3);
                listPreference.setSummary(entries[3]);
                break;
            }
        }
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_location_filter_time));
                listPreference.setSummary(newValue.toString());
                AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
                switch (listPreference.findIndexOfValue(newValue.toString())) {
                    case 0: {
                        state.locationSettings.timeFilter = AppSettings.FilterTimeInterval.IMMEDIATELY;
                        break;
                    }
                    case 1: {
                        state.locationSettings.timeFilter = AppSettings.FilterTimeInterval.MINUTES_1;
                        break;
                    }
                    case 2: {
                        state.locationSettings.timeFilter = AppSettings.FilterTimeInterval.MINUTES_10;
                        break;
                    }
                    case 3: {
                        state.locationSettings.timeFilter = AppSettings.FilterTimeInterval.MINUTES_60;
                        break;
                    }
                }
                AppSettings.setAppSettingsEntity(getContext(), state);
                return true;
            }
        });
    }

}