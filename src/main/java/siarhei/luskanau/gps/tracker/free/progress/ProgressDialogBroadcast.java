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
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

public class ProgressDialogBroadcast {

    public static final String ACTION_ALERT_DIALOG_SHOW = "ACTION_ALERT_DIALOG_SHOW";
    public static final String ACTION_PROGRESS_DIALOG_SHOW = "ACTION_PROGRESS_DIALOG_SHOW";
    public static final String ACTION_PROGRESS_DIALOG_REMOVE = "ACTION_PROGRESS_DIALOG_REMOVE";
    public static final String ACTION_PROGRESS_DIALOG_REFRESH = "ACTION_PROGRESS_DIALOG_REFRESH";
    public static final String TITLE_ARG = "TITLE_ARG";
    public static final String MESSAGE_ARG = "MESSAGE_ARG";
    public static final String REFRESH_ACTION_ARG = "REFRESH_ACTION_ARG";

    public static void registerReceiver(Context context, BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_ALERT_DIALOG_SHOW);
        intentFilter.addAction(ACTION_PROGRESS_DIALOG_SHOW);
        intentFilter.addAction(ACTION_PROGRESS_DIALOG_REMOVE);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }

    public static void registerRefreshReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(ACTION_PROGRESS_DIALOG_REFRESH));
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public static void sendShowAlertDialogBroadcast(Context context, CharSequence title, CharSequence message) {
        Intent intent = new Intent(ACTION_ALERT_DIALOG_SHOW);
        intent.putExtra(TITLE_ARG, title);
        intent.putExtra(MESSAGE_ARG, message);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendShowProgressDialogBroadcast(Context context, CharSequence title, CharSequence message) {
        Intent intent = new Intent(ACTION_PROGRESS_DIALOG_SHOW);
        intent.putExtra(TITLE_ARG, title);
        intent.putExtra(MESSAGE_ARG, message);
        intent.putExtra(REFRESH_ACTION_ARG, ACTION_PROGRESS_DIALOG_REFRESH);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void sendRemoveProgressDialogBroadcast(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_PROGRESS_DIALOG_REMOVE));
    }

}
