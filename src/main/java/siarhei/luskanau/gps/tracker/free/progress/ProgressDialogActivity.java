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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

public class ProgressDialogActivity extends ActionBarActivity {

    @Override
    public void onStart() {
        super.onStart();
        ProgressBinder.getInstance().bindTask(this);
    }

    @Override
    public void onStop() {
        ProgressBinder.getInstance().unbindTask(this);
        super.onStop();
    }

    public void showProgressDialog(CharSequence title, CharSequence message) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) fragmentManager.findFragmentByTag(ProgressDialogFragment.TAG);
        if (progressDialogFragment != null) {
            progressDialogFragment.updateProgressDialog(title, message);
        } else {
            ProgressDialogFragment.newInstance(title, message).show(fragmentManager, ProgressDialogFragment.TAG);
        }
    }

    public void hideProgressDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        Fragment fragment = fragmentManager.findFragmentByTag(ProgressDialogFragment.TAG);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    public void showAlertDialog(CharSequence title, CharSequence message) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        Fragment fragment = fragmentManager.findFragmentByTag(AlertDialogFragment.TAG);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
        AlertDialogFragment.newInstance(title, message).show(fragmentManager, AlertDialogFragment.TAG);
    }

    public void onProgressDialogTaskFinished() {
    }

}
