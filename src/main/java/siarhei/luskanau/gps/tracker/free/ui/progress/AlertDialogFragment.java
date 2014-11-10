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

package siarhei.luskanau.gps.tracker.free.ui.progress;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class AlertDialogFragment extends DialogFragment {

    private static final String TAG = AlertDialogFragment.class.getCanonicalName();

    private static final String TITLE_ARG = "TITLE_ARG";
    private static final String MESSAGE_ARG = "MESSAGE_ARG";

    private AlertDialog alertDialog;
    private CharSequence title;
    private CharSequence message;

    public static void show(FragmentActivity activity, CharSequence title, CharSequence message) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment != null) {
            if (fragment instanceof AlertDialogFragment) {
                ((AlertDialogFragment) fragment).updateAlertDialog(title, message);
            }
        } else {
            newInstance(title, message).show(fragmentManager, TAG);
        }
    }

    private static AlertDialogFragment newInstance(CharSequence title, CharSequence message) {
        AlertDialogFragment fragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putCharSequence(TITLE_ARG, title);
        args.putCharSequence(MESSAGE_ARG, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(TITLE_ARG)) {
            title = savedInstanceState.getCharSequence(TITLE_ARG);
        } else if (getArguments().containsKey(TITLE_ARG)) {
            title = getArguments().getCharSequence(TITLE_ARG);
        }
        if (savedInstanceState != null && savedInstanceState.containsKey(MESSAGE_ARG)) {
            message = savedInstanceState.getCharSequence(MESSAGE_ARG);
        } else if (getArguments().containsKey(MESSAGE_ARG)) {
            message = getArguments().getCharSequence(MESSAGE_ARG);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setCancelable(true);
        alertDialog = builder.create();
        return alertDialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(TITLE_ARG, title);
        outState.putCharSequence(MESSAGE_ARG, message);
    }

    private void updateAlertDialog(CharSequence title, CharSequence message) {
        this.title = title;
        this.message = message;
        if (alertDialog != null) {
            alertDialog.setTitle(title);
            alertDialog.setMessage(message);
        }
    }

}