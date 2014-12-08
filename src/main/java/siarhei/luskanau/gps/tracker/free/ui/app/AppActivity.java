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

package siarhei.luskanau.gps.tracker.free.ui.app;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.androidquery.AQuery;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.service.TrackerService;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.ui.drawer.BaseDrawerActivity;

public class AppActivity extends BaseDrawerActivity implements AppController.AppControllerAware {

    private AppController appController = new AppController(this);
    private AQuery aq = new AQuery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appController.onShowTrackerFragment();
        openDrawer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tracker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DrawerLayout drawerLayout = (DrawerLayout) aq.id(R.id.baseDrawerLayout).getView();
        FrameLayout leftDrawerFrameLayout = (FrameLayout) aq.id(R.id.leftDrawerFrameLayout).getView();
        boolean drawerOpen = drawerLayout.isDrawerOpen(leftDrawerFrameLayout);
        if (drawerOpen) {
            menu.findItem(R.id.menu_item_action_play).setVisible(false);
            menu.findItem(R.id.menu_item_action_stop).setVisible(false);
        } else {
            if (AppSettings.getAppSettingsEntity(this).isTrackerStarted) {
                menu.findItem(R.id.menu_item_action_play).setVisible(false);
                menu.findItem(R.id.menu_item_action_stop).setVisible(true);
            } else {
                menu.findItem(R.id.menu_item_action_play).setVisible(true);
                menu.findItem(R.id.menu_item_action_stop).setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_action_play:
                if (AppSettings.getServerEntity(this) != null) {
                    TrackerService.startTracking(this);
                } else {
                    appController.onShowServersFragment();
                }
                supportInvalidateOptionsMenu();
                return true;
            case R.id.menu_item_action_stop:
                TrackerService.stopTracking(this);
                supportInvalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public AppController getAppController() {
        return appController;
    }

}