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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.ui.app.AppActivity;

public class GeneralSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference_general);

        onLanguagePreferenceCreate();
    }

    private void onLanguagePreferenceCreate() {
        AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_language));
        CharSequence[] entries = listPreference.getEntries();
        switch (state.language) {
            case EN: {
                listPreference.setValueIndex(0);
                listPreference.setSummary(entries[0]);
                break;
            }
            case RU: {
                listPreference.setValueIndex(1);
                listPreference.setSummary(entries[1]);
                break;
            }
        }
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                ListPreference listPreference = (ListPreference) findPreference(getString(R.string.preference_key_language));
                listPreference.setSummary(newValue.toString());
                AppSettings.State state = AppSettings.getAppSettingsEntity(getContext());
                switch (listPreference.findIndexOfValue(newValue.toString())) {
                    case 0: {
                        if (state.language == AppSettings.Language.EN) {
                            return true;
                        }
                        state.language = AppSettings.Language.EN;
                        break;
                    }
                    case 1: {
                        if (state.language == AppSettings.Language.RU) {
                            return true;
                        }
                        state.language = AppSettings.Language.RU;
                        break;
                    }
                }
                AppSettings.setAppSettingsEntity(getContext(), state);
                startActivity(new Intent(getContext(), AppActivity.class));
                System.exit(0);
                return true;
            }
        });
    }

}