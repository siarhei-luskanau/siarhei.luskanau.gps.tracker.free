/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Siarhei Luskanau
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

package siarhei.luskanau.gps.tracker.free.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.sync.task.GcmTask;
import siarhei.luskanau.gps.tracker.free.ui.dialog.AboutFragment;

public class LeftDrawerFragment extends Fragment {

    public static final String TAG = "LeftDrawerFragment";

    private AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_drawer, container, false);
        aq = new AQuery(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aq.id(R.id.gcmMessageTextView).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new GcmTask(getActivity()).doTask();
                            GcmTask.sendEchoMessage(getActivity());
                        } catch (Exception e) {
                            Log.d(TAG, e.toString(), e);
                        }
                    }
                }).start();
            }
        });
        aq.id(R.id.internetLeftDrawerTextView).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getFragmentManager().findFragmentByTag(InternetSettingsFragment.TAG);
                if (fragment == null) {
                    getFragmentManager().beginTransaction().add(R.id.internetSettingsFrameLayout, new InternetSettingsFragment(), InternetSettingsFragment.TAG).commit();
                } else {
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        });
        aq.id(R.id.aboutTextView).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().findFragmentByTag(AboutFragment.TAG) == null) {
                    new AboutFragment().show(getFragmentManager(), AboutFragment.TAG);
                }
                BaseDrawerActivity baseDrawerActivity = (BaseDrawerActivity) getActivity();
                baseDrawerActivity.closeDrawers();
            }
        });
    }

}
