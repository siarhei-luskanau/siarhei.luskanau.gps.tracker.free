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

package siarhei.luskanau.gps.tracker.free.dao;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.CupboardFactory;
import nl.qbusict.cupboard.ProviderCompartment;
import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.provider.AppContract;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class ServerDAO {

    private static final String TAG = "ServerDAO";

    public static Uri putServer(Context context, ServerEntity server) {
        Uri uri = CupboardFactory.cupboard().withContext(context).put(AppContract.SERVER_URI, server);
        return uri;
    }

    public static ServerEntity getServerByRowId(Context context, long rowId) {
        Uri uri = ContentUris.withAppendedId(AppContract.SERVER_URI, rowId);
        ServerEntity server = CupboardFactory.cupboard().withContext(context).get(uri, ServerEntity.class);
        return server;
    }

    public static List<ServerEntity> getServers(Context context) {
        ProviderCompartment.QueryBuilder queryBuilder = CupboardFactory.cupboard().withContext(context).query(AppContract.SERVER_URI, ServerEntity.class);
        List<ServerEntity> serverList = queryBuilder.list();
        queryBuilder.orderBy(BaseColumns._ID + " ASC");
        return serverList;
    }

    public static List<ServerEntity> getAssetsServers(Context context) {
        List<ServerEntity> list = new ArrayList<ServerEntity>();
        try {
            String json = new String(Utils.getBytes(context.getAssets().open("servers.json")), "utf-8");
            List<ServerEntity> serverEntities = AppConstants.GSON.fromJson(json, ServerEntity.COLLECTION_TYPE);
            if (serverEntities != null) {
                list.addAll(serverEntities);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return list;
    }

}