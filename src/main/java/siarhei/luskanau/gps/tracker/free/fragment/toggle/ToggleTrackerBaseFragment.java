/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 Siarhei Luskanau
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

package siarhei.luskanau.gps.tracker.free.fragment.toggle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.CompoundButton;

import siarhei.luskanau.gps.tracker.free.settings.AppSettings;

public abstract class ToggleTrackerBaseFragment extends Fragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getToggleButton().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppSettings.setTrackerStarted(getActivity(), isChecked);
                if (isChecked) {
                    showStartState();
                } else {
                    showStopState();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppSettings.isTrackerStarted(getActivity())) {
            showStartState();
        } else {
            showStopState();
        }
        if (AppSettings.getServerEntity(getActivity()) == null) {
            getToggleButton().setEnabled(false);
            AppSettings.setTrackerStarted(getActivity(), false);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    protected abstract CompoundButton getToggleButton();

    protected abstract void showStartState();

    protected abstract void showStopState();

}
