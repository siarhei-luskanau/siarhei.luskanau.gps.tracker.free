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
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.database.ContentProvider;
import siarhei.luskanau.gps.tracker.free.database.LocationColumns;
import siarhei.luskanau.gps.tracker.free.model.LocationModel;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;

public class LocationDAO extends BaseDAO {

    public static Uri getUri(Context context) {
        return Uri.withAppendedPath(ContentProvider.getProviderAuthorityUri(context), LocationColumns.TABLE_NAME);
    }

    public static long insertOrUpdateLocationPacket(Context context, LocationModel locationModel) {
        ContentValues values = toContentValues(locationModel);
        if (locationModel.rowId != null) {
            String selection = LocationColumns._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(locationModel.rowId)};
            context.getContentResolver().update(getUri(context), values, selection, whereArgs);
        } else {
            Uri idUri = context.getContentResolver().insert(getUri(context), values);
            locationModel.rowId = Long.parseLong(idUri.getPathSegments().get(1));
            AppSettings.setLastLocationPacket(context, locationModel);

        }
        return locationModel.rowId;
    }

    public static List<LocationModel> queryNextLocations(Context context, int limit) {
        List<LocationModel> packets = new ArrayList<LocationModel>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Uri.withAppendedPath(Uri.withAppendedPath(Uri.withAppendedPath(getUri(context), null), null), String.valueOf(limit)), null, null, null, LocationColumns._ID);
            if (cursor.moveToFirst()) {
                do {
                    packets.add(fromCursor(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            close(cursor);
        }
        return packets;
    }

    public static int deleteLocations(Context context, List<LocationModel> packets) {
        if (packets == null || packets.size() == 0) {
            return 0;
        }
        String[] whereArgs = new String[packets.size()];
        StringBuilder whereClause = new StringBuilder();
        whereClause.append("CAST(").append(BaseColumns._ID).append(" AS INTEGER) IN (");
        for (int i = 0; i < packets.size(); i++) {
            whereArgs[i] = String.valueOf(packets.get(i).rowId);
            if (i > 0) {
                whereClause.append(",");
            }
            whereClause.append("?");
        }
        whereClause.append(")");
        return context.getContentResolver().delete(getUri(context), whereClause.toString(), whereArgs);
    }

    public static boolean hasLocations(Context context) {
        return queryCount(context, LocationColumns.TABLE_NAME) > 0;
    }

    private static ContentValues toContentValues(LocationModel locationModel) {
        ContentValues values = new ContentValues();
        values.put(LocationColumns.PACKET, AppConstants.GSON.toJson(locationModel));
        return values;
    }

    private static LocationModel fromCursor(Cursor cursor) {
        LocationModel locationEntity = AppConstants.GSON.fromJson(cursor.getString(cursor.getColumnIndex(LocationColumns.PACKET)), LocationModel.class);
        locationEntity.rowId = cursor.getLong(cursor.getColumnIndex(LocationColumns._ID));
        return locationEntity;
    }

}