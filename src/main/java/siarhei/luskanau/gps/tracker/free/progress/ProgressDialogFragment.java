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

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;

public class ProgressDialogFragment extends DialogFragment {

    private static final String TAG = ProgressDialogFragment.class.getCanonicalName();
    private static final String TITLE_ARG = "TITLE_ARG";
    private static final String MESSAGE_ARG = "MESSAGE_ARG";
    private static final String REFRESH_ACTION_ARG = "REFRESH_ACTION_ARG";
    private ProgressDialog progressDialog;
    private CharSequence title;
    private CharSequence message;
    private String refreshAction;

    public static void show(FragmentActivity activity, CharSequence title, CharSequence message, String refreshAction) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment != null) {
            if (fragment instanceof ProgressDialogFragment) {
                ((ProgressDialogFragment) fragment).updateProgressDialog(title, message, refreshAction);
            }
        } else {
            newInstance(title, message, refreshAction).show(fragmentManager, TAG);
        }
    }

    public static void remove(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    public static void refresh(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
            if (fragment instanceof ProgressDialogFragment) {
                ((ProgressDialogFragment) fragment).sendRefreshBroadcast();
            }
        }
    }

    private static ProgressDialogFragment newInstance(CharSequence title, CharSequence message, String action) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putCharSequence(TITLE_ARG, title);
        args.putCharSequence(MESSAGE_ARG, message);
        args.putString(REFRESH_ACTION_ARG, action);
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
        if (savedInstanceState != null && savedInstanceState.containsKey(REFRESH_ACTION_ARG)) {
            refreshAction = savedInstanceState.getString(REFRESH_ACTION_ARG);
        } else if (getArguments().containsKey(REFRESH_ACTION_ARG)) {
            refreshAction = getArguments().getString(REFRESH_ACTION_ARG);
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(title);
        progressDialog.setIcon(0);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgress(0);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // ProgressBinder.getInstance().cancelTask((ProgressDialogActivity) getActivity());
                    return true;
                }
                return false;
            }
        });
        return progressDialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(TITLE_ARG, title);
        outState.putCharSequence(MESSAGE_ARG, message);
        outState.putString(REFRESH_ACTION_ARG, refreshAction);
    }

    private void updateProgressDialog(CharSequence title, CharSequence message, String refreshAction) {
        this.title = title;
        this.message = message;
        this.refreshAction = refreshAction;
        if (progressDialog != null) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }
    }

    private void sendRefreshBroadcast() {
        if (refreshAction != null) {
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(refreshAction));
        }
    }

}