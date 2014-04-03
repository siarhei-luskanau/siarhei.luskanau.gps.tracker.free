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

import android.os.AsyncTask;

public abstract class ProgressAsyncTask<Result> extends AsyncTask<Void, Void, Result> {

    private static final String TAG = "ProgressAsyncTask";

    protected TaskCallback taskCallback;
    protected CharSequence title;
    protected CharSequence message;

    public ProgressAsyncTask(TaskCallback taskCallback) {
        this.taskCallback = taskCallback;
        taskCallback.setProgressAsyncTask(this);
    }

    public void registerProgressDialogActivity(ProgressDialogActivity progressDialogActivity) {
        taskCallback.bindActivity(progressDialogActivity);
    }

    public void unregisterProgressDialogActivity(ProgressDialogActivity progressDialogActivity) {
        taskCallback.unbindActivity(progressDialogActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        synchronized (this) {
            taskCallback.registerTask(this);
            taskCallback.publishTaskProgress(getTitle(), getMessage());
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        synchronized (this) {
            taskCallback.onProgressDialogTaskFinished();
            taskCallback.unregisterTask(this);
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        synchronized (this) {
            taskCallback.onProgressDialogTaskFinished();
            taskCallback.unregisterTask(this);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate();
        taskCallback.publishTaskProgress(getTitle(), getMessage());
    }

    public CharSequence getTitle() {
        return title;
    }

    public CharSequence getMessage() {
        return message;
    }

}