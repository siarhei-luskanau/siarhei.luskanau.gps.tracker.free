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

package siarhei.luskanau.gps.tracker.free.service.sync.tracking;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.broadcast.AppBroadcastController;
import siarhei.luskanau.gps.tracker.free.dao.LocationDAO;
import siarhei.luskanau.gps.tracker.free.model.LocationModel;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;

public class SendRestTask {

    public static void doTask(Context context) throws Exception {
        ServerEntity serverEntity = AppSettings.getServerEntity(context);
        RestClient restClient = RestClientBuilder.build(serverEntity.server_address);
        for (; ; ) {
            List<LocationModel> locationEntities = LocationDAO.queryNextLocations(context, 100);
            if (locationEntities != null && locationEntities.size() > 0) {
                Call<String> call = restClient.send(locationEntities);
                Response<String> response = call.execute();
                String result = response.body();
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

    private interface RestClient {
        @GET("api/tracker")
        Call<String> send(@Body List<LocationModel> list);
    }

    public static class RestClientBuilder {
        public static final int CONNECTION_TIMEOUT = 30 * 1000;

        public static RestClient build(String serverUrl) {
            OkHttpClient okClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                    .build();
            String baseUrl = serverUrl.endsWith("/") ? serverUrl : serverUrl + "/";
            return new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create(AppConstants.GSON))
                    .build()
                    .create(RestClient.class);
        }
    }

}