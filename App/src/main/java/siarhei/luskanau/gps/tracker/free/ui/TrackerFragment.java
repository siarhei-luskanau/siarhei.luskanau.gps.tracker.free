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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;

import siarhei.luskanau.androiddatalib.SimpleAppBarWithDrawerFragment;
import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.broadcast.AppBroadcastController;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.service.TrackerService;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.ui.app.AppController;
import siarhei.luskanau.gps.tracker.free.ui.dialog.AboutServerDialogFragment;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class TrackerFragment extends SimpleAppBarWithDrawerFragment {

    public static final String TAG = "TrackerFragment";
    protected AQuery aq;
    private AppBroadcastController.AppBroadcastReceiver appBroadcastReceiver = new AppBroadcastController().createBroadcastReceiver(new InnerAppBroadcastCallback());

    public static TrackerFragment newInstance(int menuItemId) {
        TrackerFragment fragment = new TrackerFragment();
        Bundle args = new Bundle();
        setMenuItemId(args, menuItemId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_tracker, container, false);
        aq = new AQuery(getActivity(), view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        aq.id(R.id.imeiTextView).text(getString(R.string.fragment_tracker_imei, Utils.getDeviceId(getContext())));

        aq.id(R.id.editServerImageButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppController.get(getActivity()).onShowServersFragment();
            }
        });
        aq.id(R.id.aboutServerImageButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerEntity serverEntity = AppSettings.getServerEntity(getContext());
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
        appBroadcastReceiver.registerReceiver(getContext());
        ServerEntity serverEntity = AppSettings.getServerEntity(getContext());
        if (serverEntity != null) {
            aq.id(R.id.aboutServerImageButton).visible();
            aq.id(R.id.serverNameImageButton).text(getString(R.string.fragment_tracker_server, serverEntity.name));
        } else {
            aq.id(R.id.aboutServerImageButton).gone();
            aq.id(R.id.serverNameImageButton).text(getString(R.string.fragment_tracker_server, "---"));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        appBroadcastReceiver.unregisterReceiver(getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tracker, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (AppSettings.getAppSettingsEntity(getContext()).isTrackerStarted) {
            menu.findItem(R.id.menu_item_action_play).setVisible(false);
            menu.findItem(R.id.menu_item_action_stop).setVisible(true);
        } else {
            menu.findItem(R.id.menu_item_action_play).setVisible(true);
            menu.findItem(R.id.menu_item_action_stop).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_action_play:
                if (AppSettings.getServerEntity(getContext()) != null) {
                    TrackerService.startTracking(getContext());
                } else {
                    AppController.get(getActivity()).onShowServersFragment();
                }
                getActivity().supportInvalidateOptionsMenu();
                return true;
            case R.id.menu_item_action_stop:
                TrackerService.stopTracking(getContext());
                getActivity().supportInvalidateOptionsMenu();
                return true;
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private class InnerAppBroadcastCallback extends AppBroadcastController.AppBroadcastCallback {
        @Override
        public void onLastPositionIsUpdated() {
        }
    }

}