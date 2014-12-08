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

package siarhei.luskanau.gps.tracker.free.service.sync.positions;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.GET;
import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.broadcast.AppBroadcastController;
import siarhei.luskanau.gps.tracker.free.dao.LocationDAO;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.shared.LocationPacket;

public class SendRestTask {

    public static void doTask(Context context) throws Exception {
        ServerEntity serverEntity = AppSettings.getServerEntity(context);
        RestClient restClient = RestClientBuilder.build(serverEntity.server_address);
        for (; ; ) {
            List<LocationPacket> locationEntities = LocationDAO.queryNextLocations(context, 100);
            if (locationEntities != null && locationEntities.size() > 0) {
                String result = restClient.send(locationEntities);
                Log.d(context.getPackageName(), "result: " + result);
                LocationDAO.deleteLocations(context, locationEntities);
                incPacketCounter(context, locationEntities.size());
                Log.d(context.getPackageName(), "Packet is send: " + locationEntities.size());
            } else {
                break;
            }
        }
    }

    private static void incPacketCounter(Context context, int count) {
        AppSettings.setPacketCounter(context, AppSettings.getPacketCounter(context) + count);
        AppBroadcastController.sendLastPositionIsUpdatedBroadcast(context);
    }

    private static interface RestClient {
        @GET("/api/tracker")
        public String send(@Body List<LocationPacket> list);
    }

    public static class RestClientBuilder {
        public static final int CONNECTION_TIMEOUT = 30 * 1000;

        public static RestClient build(String serverUrl) {
            OkHttpClient okClient = new OkHttpClient();
            okClient.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            okClient.setReadTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            return new RestAdapter.Builder()
                    .setEndpoint(serverUrl)
                    .setClient(new OkClient(okClient))
                    .setConverter(new GsonConverter(AppConstants.GSON))
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestInterceptor.RequestFacade requestFacade) {
                            requestFacade.addHeader("Connection", "close");
                        }
                    })
                    .setLogLevel(RestAdapter.LogLevel.HEADERS)
                    .build()
                    .create(RestClient.class);
        }
    }

}