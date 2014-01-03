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

package siarhei.luskanau.gps.tracker.free.fragment.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import siarhei.luskanau.gps.shared.LocationPacket;
import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.settings.ServerEntity;
import siarhei.luskanau.gps.tracker.free.sync.JsonHttpPostServer;

public class CheckServerDialogFragment extends DialogFragment {

    public static final String TAG = "CheckServerDialogFragment";
    private static final String SERVER_ENTITY_ARG = "SERVER_ENTITY_ARG";

    public static CheckServerDialogFragment newInstance(ServerEntity serverEntity) {
        CheckServerDialogFragment fragment = new CheckServerDialogFragment();
        Bundle args = new Bundle();
        args.putString(SERVER_ENTITY_ARG, AppConstants.GSON.toJson(serverEntity));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ServerEntity serverEntity = AppConstants.GSON.fromJson(getArguments().getString(SERVER_ENTITY_ARG), ServerEntity.class);
        Log.e(TAG, AppConstants.GSON.toJson(serverEntity));
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("connecting...");
        progressDialog.setIndeterminate(true);
        new CheckAsyncTask(serverEntity).execute();
        return progressDialog;
    }

    private class CheckAsyncTask extends AsyncTask<Void, Void, Exception> {
        private ServerEntity serverEntity;

        public CheckAsyncTask(ServerEntity serverEntity) {
            this.serverEntity = serverEntity;
        }

        @Override
        protected Exception doInBackground(Void... params) {
            try {
                if (serverEntity == null || serverEntity.server_type == null || serverEntity.server_address == null) {
                    throw new NullPointerException();
                }

                if (AppConstants.SERVER_TYPE_SOCKET.equalsIgnoreCase(serverEntity.server_type)) {
                    SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(serverEntity.server_address), serverEntity.server_port);
                    Socket socket = new Socket();
                    socket.connect(socketAddress, 5 * 1000);
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.close();
                    socket.close();
                }
                if (AppConstants.SERVER_TYPE_JSON_BODY.equalsIgnoreCase(serverEntity.server_type)) {
                    JsonHttpPostServer.sendLocations(serverEntity.server_address, new ArrayList<LocationPacket>());
                }
                if (AppConstants.SERVER_TYPE_JSON_FORM.equalsIgnoreCase(serverEntity.server_type)) {
                    JsonHttpPostServer.sendLocationsForm(serverEntity.server_address, new ArrayList<LocationPacket>());
                }
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception exception) {
            super.onPostExecute(exception);
            CheckServerDialogFragment.this.dismiss();
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                if (exception != null) {
                    AlertDialogFragment.newInstance("Error", exception.getMessage(), android.R.drawable.ic_dialog_alert).show(fragmentManager, "AlertDialogFragment");
                } else {
                    AlertDialogFragment.newInstance("Success", null, android.R.drawable.ic_dialog_info).show(fragmentManager, "AlertDialogFragment");
                }
            }
        }
    }

}
