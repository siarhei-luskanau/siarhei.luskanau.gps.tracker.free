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

package siarhei.luskanau.gps.tracker.free.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.List;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.dao.ServerEntityDAO;
import siarhei.luskanau.gps.tracker.free.fragment.ServerEntityItemFragment;
import siarhei.luskanau.gps.tracker.free.settings.ServerEntity;

public class ServersActivity extends BaseActivity {

    private List<ServerEntity> serverEntities;

    public static void startServersActivity(Context context) {
        context.startActivity(new Intent(context, ServersActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateServerEntities();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_servers, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_action_new: {
                ServerEditActivity.startServerEditActivity(this, null);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void updateServerEntities() {
        serverEntities = ServerEntityDAO.getServerEntities(this);
        FragmentManager fragmentManager = getSupportFragmentManager();

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