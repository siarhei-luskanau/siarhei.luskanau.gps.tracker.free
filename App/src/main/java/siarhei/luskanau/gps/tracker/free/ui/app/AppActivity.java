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

import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.service.sync.SyncService;
import siarhei.luskanau.gps.tracker.free.service.sync.task.GcmTask;
import siarhei.luskanau.gps.tracker.free.ui.TrackerFragment;
import siarhei.luskanau.gps.tracker.free.ui.progress.BaseProgressActivity;

public class AppActivity extends BaseProgressActivity implements AppController.AppControllerAware {

    private static final String TAG = "BaseDrawerActivity";
    protected AppController appController = new AppController(this);

    @Override
    protected void onResume() {
        super.onResume();
        FrameLayout appContentFrameLayout = getAppContentFrameLayout();
        if (appContentFrameLayout == null || appContentFrameLayout.getChildCount() == 0) {
            appController.onShowTrackerFragment();
            openDrawer();
        }
    }

    @Override
    protected void setupNavigationView(NavigationView navigationView) {
        navigationView.inflateHeaderView(R.layout.layout_navigation_header);
        navigationView.inflateMenu(R.menu.menu_navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener());
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawers();
        } else if (getSupportFragmentManager().findFragmentByTag(TrackerFragment.TAG) == null) {
            appController.onShowTrackerFragment();
        } else {
            openDrawer();
        }
    }

    @Override
    public AppController getAppController() {
        return appController;
    }

    private class NavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_drawer_item_gcm: {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SyncService.startTask(AppActivity.this, new GcmTask());
                                GcmTask.sendEchoMessage(AppActivity.this);
                            } catch (Exception e) {
                                Log.d(TAG, e.toString(), e);
                            }
                        }
                    }).start();
                    break;
                }
                case R.id.menu_drawer_item_home: {
                    appController.onShowTrackerFragment();
                    break;
                }
                case R.id.menu_drawer_item_settings: {
                    appController.onShowSettingsFragment();
                    break;
                }
                case R.id.menu_drawer_item_about: {
                    appController.onShowAboutFragment();
                    break;
                }
            }
            closeDrawers();

            return true;
        }
    }

}