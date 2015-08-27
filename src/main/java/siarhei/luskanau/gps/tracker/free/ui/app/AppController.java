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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.List;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.ui.AboutFragment;
import siarhei.luskanau.gps.tracker.free.ui.ServersFragment;
import siarhei.luskanau.gps.tracker.free.ui.TrackerFragment;
import siarhei.luskanau.gps.tracker.free.ui.settings.SettingsTabsFragment;

public class AppController {

    private static final String TAG = "AppController";

    private BaseDrawerActivity activity;

    public AppController(BaseDrawerActivity activity) {
        this.activity = activity;
    }

    public static AppController get(FragmentActivity activity) {
        if (activity != null && activity instanceof AppControllerAware) {
            return ((AppControllerAware) activity).getAppController();
        }
        return null;
    }

    public static <T> T getBusiness(FragmentActivity activity, Class<T> type) {
        return (T) activity.getSupportFragmentManager().findFragmentByTag(type.getCanonicalName());
    }

    public boolean isDrawerOpen() {
        return activity.isDrawerOpen();
    }

    public void onShowHomeFragment() {
        onShowTrackerFragment();
    }

    public void onShowTrackerFragment() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TrackerFragment.TAG) == null) {
            fragmentManager.beginTransaction().replace(R.id.appContentFrameLayout, new TrackerFragment(), TrackerFragment.TAG).commit();
        }
    }

    public void onShowSettingsFragment() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(SettingsTabsFragment.TAG) == null) {
            fragmentManager.beginTransaction().replace(R.id.appContentFrameLayout, new SettingsTabsFragment(), SettingsTabsFragment.TAG).commit();
        }
    }

    public void onShowAboutFragment() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(AboutFragment.TAG) == null) {
            fragmentManager.beginTransaction().replace(R.id.appContentFrameLayout, new AboutFragment(), AboutFragment.TAG).commit();
        }
    }

    public void onShowServersFragment() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(ServersFragment.TAG) == null) {
            fragmentManager.beginTransaction().replace(R.id.appContentFrameLayout, new ServersFragment(), ServersFragment.TAG).commit();
        }
    }

    public void popBackStack() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    private void logDebug() {
        Log.d(TAG, "###########################");
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    Log.d(TAG, "FragmentManager.getFragments: " + fragment.getTag() + ": " + fragment.getClass().getSimpleName());
                } else {
                    Log.d(TAG, "FragmentManager.getFragments: null");
                }
            }
        }
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(i);
            Log.d(TAG, "FragmentManager.getBackStackEntryAt: " + i + ": " + backStackEntry.getName());
        }
    }

    public static interface AppControllerAware {
        public AppController getAppController();
    }

    public static interface ServersListBusiness {
        public static final String TAG = ServersListBusiness.class.getCanonicalName();

        public ServerEntity getServerEntity(int position);

        public void onServerEntityConfirmed(int position);
    }

}