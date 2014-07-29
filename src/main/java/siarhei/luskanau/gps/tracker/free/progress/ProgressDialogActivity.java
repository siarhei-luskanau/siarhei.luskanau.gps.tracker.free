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

package siarhei.luskanau.gps.tracker.free.progress;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class ProgressDialogActivity extends ActionBarActivity {

    private static final String TAG = "ProgressDialogActivity";

    private BroadcastReceiver broadcastReceiver = new ProgressDialogBroadcastReceiver();

    @Override
    protected void onStart() {
        super.onStart();
        ProgressDialogBroadcast.registerReceiver(this, broadcastReceiver);
        ProgressDialogFragment.refresh(ProgressDialogActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ProgressDialogBroadcast.unregisterReceiver(this, broadcastReceiver);
    }

    private class ProgressDialogBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (ProgressDialogBroadcast.ACTION_ALERT_DIALOG_SHOW.equals(intent.getAction())) {
                    CharSequence title = intent.getCharSequenceExtra(ProgressDialogBroadcast.TITLE_ARG);
                    CharSequence message = intent.getCharSequenceExtra(ProgressDialogBroadcast.MESSAGE_ARG);
                    AlertDialogFragment.show(ProgressDialogActivity.this, title, message);
                } else if (ProgressDialogBroadcast.ACTION_PROGRESS_DIALOG_SHOW.equals(intent.getAction())) {
                    CharSequence title = intent.getCharSequenceExtra(ProgressDialogBroadcast.TITLE_ARG);
                    CharSequence message = intent.getCharSequenceExtra(ProgressDialogBroadcast.MESSAGE_ARG);
                    String refreshAction = intent.getStringExtra(ProgressDialogBroadcast.REFRESH_ACTION_ARG);
                    ProgressDialogFragment.show(ProgressDialogActivity.this, title, message, refreshAction);
                } else if (ProgressDialogBroadcast.ACTION_PROGRESS_DIALOG_REMOVE.equals(intent.getAction())) {
                    ProgressDialogFragment.remove(ProgressDialogActivity.this);
                }
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
            }
        }
    }

}
