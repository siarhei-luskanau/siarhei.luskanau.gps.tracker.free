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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.ui.app.AppController;

public class ConfirmServerDialogFragment extends DialogFragment {

    public static final String TAG = "ConfirmServerDialogFragment";
    private static final String POSITION_ARG = "POSITION_ARG";

    public static ConfirmServerDialogFragment newInstance(int position) {
        ConfirmServerDialogFragment fragment = new ConfirmServerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_ARG, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AppController.ServersListBusiness serversListBusiness = AppController.getBusiness(getActivity(), AppController.ServersListBusiness.class);
        if (serversListBusiness != null) {
            ServerEntity serverEntity = serversListBusiness.getServerEntity(getArguments().getInt(POSITION_ARG));
            builder.setMessage(serverEntity.name);
        }
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppController.ServersListBusiness serversListBusiness = AppController.getBusiness(getActivity(), AppController.ServersListBusiness.class);
                if (serversListBusiness != null) {
                    serversListBusiness.onServerEntityConfirmed(getArguments().getInt(POSITION_ARG));
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

}