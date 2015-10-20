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

package siarhei.luskanau.gps.tracker.free.service.sync.tracking.json;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.List;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.broadcast.AppBroadcastController;
import siarhei.luskanau.gps.tracker.free.dao.LocationDAO;
import siarhei.luskanau.gps.tracker.free.model.LocationModel;
import siarhei.luskanau.gps.tracker.free.model.SendLocationsResponseDTO;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;

public class SendJsonForm {

    private static final String TAG = "SendJsonForm";
    private static final boolean DEBUG = false;

    public static void sendLocationsForm(Context context) throws Exception {
        ServerEntity serverEntity = AppSettings.getServerEntity(context);

        for (; ; ) {
            List<LocationModel> locationEntities = LocationDAO.queryNextLocations(context, 100);
            if (locationEntities != null && locationEntities.size() > 0) {

                String requestJsonString = AppConstants.GSON.toJson(locationEntities);


                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(serverEntity.server_address)
                        .build();


                if (DEBUG) {
                    Log.d(TAG, "Send request: " + request.urlString() + " " + requestJsonString);
                }
                Response response = client.newCall(request).execute();
                String responseJsonString = response.body().string();
                if (DEBUG) {
                    Log.d(TAG, "Received response: " + responseJsonString);
                }

                SendLocationsResponseDTO statusResponse = AppConstants.GSON.fromJson(responseJsonString, SendLocationsResponseDTO.class);
                if (statusResponse != null && statusResponse.success) {
                    LocationDAO.deleteLocations(context, locationEntities);
                    incPacketCounter(context, locationEntities.size());
                    Log.d(context.getPackageName(), "Packet is send: " + locationEntities.size() + " " + responseJsonString);
                } else {
                    Log.e(context.getPackageName(), "Packet is not send: " + responseJsonString);
                    break;
                }
            } else {
                break;
            }
        }
    }

    private static void incPacketCounter(Context context, int count) {
        AppSettings.setPacketCounter(context, AppSettings.getPacketCounter(context) + count);
        AppBroadcastController.sendLastPositionIsUpdatedBroadcast(context);
    }

}
