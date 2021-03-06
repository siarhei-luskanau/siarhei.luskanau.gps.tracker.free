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

package siarhei.luskanau.androidbroadcastlib;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ProgressLocalBroadcastController extends LocalBroadcastController<ProgressLocalBroadcastController.ProgressLocalBroadcastCallback, ProgressLocalBroadcastController.ProgressLocalBroadcastReceiver> {

    private static final String ACTION_SHOW_TOAST = "ACTION_SHOW_TOAST";
    private static final String ACTION_SHOW_ALERT_DIALOG = "ACTION_SHOW_ALERT_DIALOG";
    private static final String TITLE_ARG = "TITLE_ARG";
    private static final String MESSAGE_ARG = "MESSAGE_ARG";

    public static void sendShowToastBroadcast(Context context, CharSequence message) {
        sendBroadcast(context, new Intent(ACTION_SHOW_TOAST).putExtra(MESSAGE_ARG, message));
    }

    public static void sendShowAlertDialogBroadcast(Context context, CharSequence title, CharSequence message) {
        sendBroadcast(context, new Intent(ACTION_SHOW_ALERT_DIALOG).putExtra(TITLE_ARG, title).putExtra(MESSAGE_ARG, message));
    }

    @Override
    public ProgressLocalBroadcastReceiver createBroadcastReceiver(ProgressLocalBroadcastCallback broadcastCallback) {
        return new ProgressLocalBroadcastReceiver(broadcastCallback);
    }

    public static class ProgressLocalBroadcastCallback implements BroadcastCallback {
        public void onShowToast(Context context, CharSequence message) {
        }

        public void onShowAlertDialog(CharSequence title, CharSequence message) {
        }
    }

    public static class ProgressLocalBroadcastReceiver extends LocalBroadcastReceiverWrapper<ProgressLocalBroadcastCallback> {

        private static final String TAG = "BroadcastReceiver";
        private ProgressLocalBroadcastCallback broadcastCallback;

        public ProgressLocalBroadcastReceiver(ProgressLocalBroadcastCallback broadcastCallback) {
            this.broadcastCallback = broadcastCallback;
        }

        @Override
        public void registerReceiver(Context context) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_SHOW_TOAST);
            intentFilter.addAction(ACTION_SHOW_ALERT_DIALOG);
            registerReceiver(context, this, intentFilter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                switch (intent.getAction()) {
                    case ACTION_SHOW_TOAST: {
                        CharSequence message = intent.getCharSequenceExtra(MESSAGE_ARG);
                        if (broadcastCallback != null) {
                            broadcastCallback.onShowToast(context, message);
                        }
                        break;
                    }
                    case ACTION_SHOW_ALERT_DIALOG: {
                        CharSequence title = intent.getCharSequenceExtra(TITLE_ARG);
                        CharSequence message = intent.getCharSequenceExtra(MESSAGE_ARG);
                        if (broadcastCallback != null) {
                            broadcastCallback.onShowAlertDialog(title, message);
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
            }
        }

    }

}