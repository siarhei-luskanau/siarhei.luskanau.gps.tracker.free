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

package siarhei.luskanau.gps.tracker.free.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.ui.app.AppController;

public class ConfirmServerDialogFragment extends DialogFragment {

    public static final String TAG = "ConfirmServerDialogFragment";
    private static final String SERVER_ENTITY = "SERVER_ENTITY";

    public static ConfirmServerDialogFragment newInstance(ServerEntity serverEntity) {
        ConfirmServerDialogFragment fragment = new ConfirmServerDialogFragment();
        Bundle args = new Bundle();
        args.putString(SERVER_ENTITY, AppConstants.GSON.toJson(serverEntity));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ServerEntity serverEntity = AppConstants.GSON.fromJson(getArguments().getString(SERVER_ENTITY), ServerEntity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(serverEntity.name);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Activity activity = getActivity();
                ServerEntity serverEntity = AppConstants.GSON.fromJson(getArguments().getString(SERVER_ENTITY), ServerEntity.class);
                if (activity != null && serverEntity != null) {
                    AppSettings.setServerEntity(activity, serverEntity);
                    AppController.get(getActivity()).onShowTrackerFragment();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

}