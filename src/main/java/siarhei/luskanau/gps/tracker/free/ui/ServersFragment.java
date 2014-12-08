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
import siarhei.luskanau.gps.tracker.free.ui.app.AppController;

public class ServersFragment extends Fragment implements AppController.ServersListBusiness {

    private List<ServerEntity> serverEntities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.activity_servers, container, false);
        return view;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_new: {
                ServerEditActivity.startServerEditActivity(getActivity(), null);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void updateServerEntities() {
        serverEntities = ServerDAO.getServers(getActivity());
        if (serverEntities == null || serverEntities.size() == 0) {
            serverEntities = ServerDAO.getAssetsServers(getActivity());
            if (serverEntities != null) {
                for (ServerEntity serverEntity : serverEntities) {
                    ServerDAO.insertOrUpdate(getActivity(), serverEntity);
                }
            }
            serverEntities = ServerDAO.getServers(getActivity());
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

}