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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.database.ContentProvider;
import siarhei.luskanau.gps.tracker.free.database.ServerColumns;
import siarhei.luskanau.gps.tracker.free.model.ServerEntity;
import siarhei.luskanau.gps.tracker.free.utils.Utils;

public class ServerDAO extends BaseDAO {

    private static final String TAG = "ServerDAO";
    private final static Uri SERVER_URI = Uri.withAppendedPath(ContentProvider.URI, ServerColumns.TABLE_NAME);

    public static long insertOrUpdate(Context context, ServerEntity serverEntity) {
        ContentValues values = toContentValues(serverEntity);
        if (serverEntity.rowId != null) {
            String selection = ServerColumns._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(serverEntity.rowId)};
            context.getContentResolver().update(SERVER_URI, values, selection, whereArgs);
        } else {
            Uri idUri = context.getContentResolver().insert(SERVER_URI, values);
            serverEntity.rowId = Long.parseLong(idUri.getPathSegments().get(1));
        }
        return serverEntity.rowId;
    }

    public static ServerEntity getServerByRowId(Context context, long rowId) {
        Cursor cursor = null;
        try {
            String selection = ServerColumns._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(rowId)};
            cursor = context.getContentResolver().query(SERVER_URI, null, selection, whereArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }
        } finally {
            close(cursor);
        }
        return null;
    }

    public static List<ServerEntity> getServers(Context context) {
        List<ServerEntity> list = new ArrayList<ServerEntity>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(SERVER_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    list.add(fromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            close(cursor);
        }
        return list;
    }

    private static ContentValues toContentValues(ServerEntity serverEntity) {
        ContentValues values = new ContentValues();
        values.put(ServerColumns.SERVER, AppConstants.GSON.toJson(serverEntity));
        return values;
    }

    private static ServerEntity fromCursor(Cursor cursor) {
        ServerEntity serverEntity = AppConstants.GSON.fromJson(cursor.getString(cursor.getColumnIndex(ServerColumns.SERVER)), ServerEntity.class);
        serverEntity.rowId = cursor.getLong(cursor.getColumnIndex(ServerColumns._ID));
        return serverEntity;
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