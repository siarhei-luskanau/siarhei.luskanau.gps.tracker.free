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

package siarhei.luskanau.gps.tracker.free.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.activity.ServersActivity;
import siarhei.luskanau.gps.tracker.free.entity.ServerEntity;
import siarhei.luskanau.gps.tracker.free.fragment.dialog.AboutServerDialogFragment;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class TrackerFragment extends Fragment {

    public static final String TAG = "TrackerFragment";

    private AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        aq = new AQuery(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aq.id(R.id.imeiTextView).text(getString(R.string.fragment_tracker_imei, Utils.getDeviceId(getActivity())));

        aq.id(R.id.editServerImageButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServersActivity.startServersActivity(getActivity());
            }
        });
        aq.id(R.id.aboutServerImageButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerEntity serverEntity = AppSettings.getServerEntity(getActivity());
                if (serverEntity != null) {
                    if (getFragmentManager().findFragmentByTag(AboutServerDialogFragment.TAG) == null) {
                        AboutServerDialogFragment.newInstance(serverEntity).show(getFragmentManager(), AboutServerDialogFragment.TAG);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ServerEntity serverEntity = AppSettings.getServerEntity(getActivity());
        if (serverEntity != null) {
            aq.id(R.id.aboutServerImageButton).visible();
            aq.id(R.id.serverNameImageButton).text(getString(R.string.fragment_tracker_server, serverEntity.name));
        } else {
            aq.id(R.id.aboutServerImageButton).gone();
            aq.id(R.id.serverNameImageButton).text(getString(R.string.fragment_tracker_server, "---"));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_tracker, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

}
