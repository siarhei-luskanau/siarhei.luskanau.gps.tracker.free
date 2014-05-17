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

import android.content.Context;
import android.widget.Toast;

public class ContextTaskCallback implements TaskCallback {

    private Context context;

    public ContextTaskCallback(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void setProgressAsyncTask(ProgressAsyncTask<?> progressAsyncTask) {
        // nothing to do
    }

    @Override
    public void publishTaskProgress(CharSequence title, CharSequence message) {
        Toast.makeText(context, title + "\n" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAlertDialog(CharSequence title, CharSequence message) {
        Toast.makeText(context, title + "\n" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressDialogTaskFinished() {
        // nothing to do
    }

    @Override
    public void registerTask(ProgressAsyncTask<?> resultProgressAsyncTask) {
        // nothing to do
    }

    @Override
    public void unregisterTask(ProgressAsyncTask<?> resultProgressAsyncTask) {
        // nothing to do
    }

    @Override
    public void bindActivity(ProgressDialogActivity progressDialogActivity) {
        // nothing to do
    }

    @Override
    public void unbindActivity(ProgressDialogActivity progressDialogActivity) {
        // nothing to do
    }

}