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

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import siarhei.luskanau.gps.tracker.free.R;

public class ToggleTrackerFragment extends ToggleTrackerBaseFragment {

    private ToggleButton toggleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        toggleButton = new ToggleButton(getActivity());
        toggleButton.setTypeface(Typeface.DEFAULT_BOLD);
        float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (32 * scale + 0.5f);
        toggleButton.setTextSize(pixels);
        return toggleButton;
    }

    @Override
    protected CompoundButton getToggleButton() {
        return toggleButton;
    }

    @Override
    protected void showStartState() {
        toggleButton.setTextColor(getResources().getColor(R.color.toggle_green));
        toggleButton.setChecked(true);
    }

    @Override
    protected void showStopState() {
        toggleButton.setTextColor(getResources().getColor(android.R.color.black));
        toggleButton.setChecked(false);
    }

}
