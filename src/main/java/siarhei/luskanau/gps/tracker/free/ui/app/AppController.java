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

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import siarhei.luskanau.gps.tracker.free.R;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.ui.ServersFragment;
import siarhei.luskanau.gps.tracker.free.ui.TrackerFragment;

public class AppController {

    private FragmentActivity activity;

    public AppController(FragmentActivity activity) {
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

    public void onShowTrackerFragment() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TrackerFragment.TAG) == null) {
            fragmentManager.beginTransaction().replace(R.id.contentFrameLayout, new TrackerFragment(), TrackerFragment.TAG).commit();
        }
    }

    public void onShowServersFragment() {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(ServersFragment.TAG) == null) {
            fragmentManager.beginTransaction().replace(R.id.contentFrameLayout, new ServersFragment(), ServersFragment.TAG).commit();
        }
    }

    public static interface AppControllerAware {
        public AppController getAppController();
    }

    public static interface ServersListBusiness {
        public static final String TAG = ServersListBusiness.class.getCanonicalName();

        public ServerEntity getServerEntity(int position);
    }

}