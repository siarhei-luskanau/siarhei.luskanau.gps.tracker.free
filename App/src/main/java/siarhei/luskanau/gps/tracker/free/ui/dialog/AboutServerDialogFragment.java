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
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;

public class AboutServerDialogFragment extends DialogFragment {

    public static final String TAG = "AboutServerDialogFragment";
    private static final String SERVER_ENTITY = "SERVER_ENTITY";
    private ServerEntity serverEntity;

    public static AboutServerDialogFragment newInstance(ServerEntity serverEntity) {
        AboutServerDialogFragment fragment = new AboutServerDialogFragment();
        Bundle args = new Bundle();
        args.putString(SERVER_ENTITY, AppConstants.GSON.toJson(serverEntity));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        serverEntity = AppConstants.GSON.fromJson(getArguments().getString(SERVER_ENTITY), ServerEntity.class);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(serverEntity.name);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Site: ").append(serverEntity.site_url).append("\n");
        stringBuilder.append("Type: ").append(serverEntity.serverType.toString()).append("\n");
        stringBuilder.append("Address: ").append(serverEntity.server_address).append("\n");
        stringBuilder.append("Port: ").append(serverEntity.server_port);
        builder.setMessage(stringBuilder.toString());
        builder.setPositiveButton(android.R.string.ok, null);
        return builder.create();
    }

}