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

package siarhei.luskanau.gps.tracker.free.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class AppBroadcastController extends BroadcastController<AppBroadcastController.AppBroadcastCallback, AppBroadcastController.AppBroadcastReceiver> {

    private static final String ACTION_LAST_POSITION_IS_UPDATED = "ACTION_LAST_POSITION_IS_UPDATED";

    public static void sendLastPositionIsUpdatedBroadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_LAST_POSITION_IS_UPDATED));
    }

    @Override
    public AppBroadcastReceiver createBroadcastReceiver(AppBroadcastCallback broadcastCallback) {
        return new AppBroadcastReceiver(broadcastCallback);
    }

    public static class AppBroadcastCallback implements BroadcastCallback {
        public void onLastPositionIsUpdated() {
        }
    }

    public static class AppBroadcastReceiver extends BroadcastReceiverWrapper<AppBroadcastCallback> {

        private AppBroadcastCallback broadcastCallback;

        public AppBroadcastReceiver(AppBroadcastCallback broadcastCallback) {
            this.broadcastCallback = broadcastCallback;
        }

        @Override
        public void registerReceiver(Context context) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_LAST_POSITION_IS_UPDATED);
            LocalBroadcastManager.getInstance(context).registerReceiver(this, intentFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_LAST_POSITION_IS_UPDATED.equals(intent.getAction())) {
                if (broadcastCallback != null) {
                    broadcastCallback.onLastPositionIsUpdated();
                }
            }
        }

    }

}