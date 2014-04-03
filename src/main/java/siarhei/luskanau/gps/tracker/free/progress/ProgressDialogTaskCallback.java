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

package siarhei.luskanau.gps.tracker.free.progress;

import android.content.Context;
import android.util.Log;

public class ProgressDialogTaskCallback implements TaskCallback {

    private static final String TAG = "ProgressDialogTaskCallback";
    private ProgressDialogActivity activity;
    private ProgressAsyncTask<?> progressAsyncTask;

    public ProgressDialogTaskCallback(ProgressDialogActivity activity) {
        this.activity = activity;
    }

    public Context getContext() {
        return activity;
    }

    @Override
    public void setProgressAsyncTask(ProgressAsyncTask<?> progressAsyncTask) {
        if (this.progressAsyncTask == null) {
            this.progressAsyncTask = progressAsyncTask;
        } else {
            throw new RuntimeException("ProgressAsyncTask already is set");
        }
    }

    @Override
    public void publishTaskProgress(CharSequence title, CharSequence message) {
        if (activity != null) {
            activity.showProgressDialog(title, message);
        }
    }

    @Override
    public void showAlertDialog(CharSequence title, CharSequence message) {
        if (activity != null) {
            activity.showAlertDialog(title, message);
        }
    }

    @Override
    public void onProgressDialogTaskFinished() {
        if (activity != null) {
            activity.hideProgressDialog();
            activity.onProgressDialogTaskFinished();
        }
    }

    @Override
    public void registerTask(ProgressAsyncTask<?> resultProgressAsyncTask) {
        ProgressBinder.getInstance().registerTask(resultProgressAsyncTask, activity);
    }

    @Override
    public void unregisterTask(ProgressAsyncTask<?> resultProgressAsyncTask) {
        ProgressBinder.getInstance().unregisterTask(resultProgressAsyncTask);
    }

    @Override
    public void bindActivity(ProgressDialogActivity progressDialogActivity) {
        try {
            if (activity != null) {
                activity.showProgressDialog(progressAsyncTask.getTitle(), progressAsyncTask.getMessage());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void unbindActivity(ProgressDialogActivity progressDialogActivity) {
        // nothing to do
    }

}
