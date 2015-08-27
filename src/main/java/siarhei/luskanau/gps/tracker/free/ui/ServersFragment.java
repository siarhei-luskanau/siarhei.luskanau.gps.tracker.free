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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.dao.ServerDAO;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.ui.app.AppController;
import siarhei.luskanau.gps.tracker.free.ui.app.BaseDrawerActivity;

public class ServersFragment extends SimpleAppBarFragment implements AppController.ServersListBusiness {

    private List<ServerEntity> serverEntities;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_servers, container, false);
        return view;
    }

    @Override
    protected void updateAppBar() {
        super.updateAppBar();
        BaseDrawerActivity activity = (BaseDrawerActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.updateDrawerToggle(false);
        activity.getSupportActionBar().setSubtitle(R.string.fragment_settings_general_server);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateServerEntities();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_servers, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean isDrawerOpen = AppController.get(getActivity()).isDrawerOpen();
        menu.findItem(R.id.menu_action_new).setVisible(!isDrawerOpen);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_new: {
                ServerEditActivity.startServerEditActivity(getContext(), null);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void updateServerEntities() {
        serverEntities = ServerDAO.getServers(getContext());
        if (serverEntities == null || serverEntities.size() == 0) {
            serverEntities = ServerDAO.getAssetsServers(getContext());
            if (serverEntities != null) {
                for (ServerEntity serverEntity : serverEntities) {
                    ServerDAO.insertOrUpdate(getContext(), serverEntity);
                }
            }
            serverEntities = ServerDAO.getServers(getContext());
        }
        FragmentManager fragmentManager = getChildFragmentManager();

        for (int i = serverEntities.size(); ; i++) {
            String fragmentTag = ServerEntityItemFragment.TAG + i;
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
            if (fragment != null) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            } else {
                break;
            }
        }

        for (int i = 0; i < serverEntities.size(); i++) {
            ServerEntity serverEntity = serverEntities.get(i);
            String fragmentTag = ServerEntityItemFragment.TAG + i;
            ServerEntityItemFragment consignmentItemFragment = (ServerEntityItemFragment) fragmentManager.findFragmentByTag(fragmentTag);
            if (consignmentItemFragment != null) {
                consignmentItemFragment.updateServerEntity(serverEntity);
            } else {
                fragmentManager.beginTransaction().add(R.id.serverEntitiesLinearLayout, ServerEntityItemFragment.newInstance(i), fragmentTag).commit();
            }
        }

    }

    public ServerEntity getServerEntity(int position) {
        if (serverEntities != null && position < serverEntities.size()) {
            return serverEntities.get(position);
        }
        return null;
    }

    @Override
    public void onServerEntityConfirmed(int position) {
        ServerEntity serverEntity = getServerEntity(position);
        if (serverEntity != null) {
            AppSettings.setServerEntity(getContext(), serverEntity);
            AppController.get(getActivity()).popBackStack();
        }
    }

}