
package siarhei.luskanau.gps.tracker.free.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import siarhei.luskanau.gps.shared.LocationPacket;
import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.utils.AppSettings;

public class LocationDAO {

    private final static String COUNT_SELECT = "count(*) as " + BaseColumns._COUNT;
    private final static Uri LOCATION_URI = Uri.withAppendedPath(ContentProvider.URI, LocationColumns.TABLE_NAME);

    public static long insertOrUpdateLocationPacket(Context context, LocationPacket locationPacket) {
        ContentValues values = new ContentValues();
        values.put(LocationColumns.PACKET, AppConstants.GSON.toJson(locationPacket));
        if (locationPacket.rowId != null) {
            String selection = LocationColumns._ID + "=?";
            String[] whereArgs = new String[]{String.valueOf(locationPacket.rowId)};
            context.getContentResolver().update(LOCATION_URI, values, selection, whereArgs);
        } else {
            Uri idUri = context.getContentResolver().insert(LOCATION_URI, values);
            locationPacket.rowId = Long.parseLong(idUri.getPathSegments().get(1));
            AppSettings.setLastLocationPacket(context, locationPacket);

        }
        return locationPacket.rowId;
    }

    public static long getCountPacket(Context context) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(LOCATION_URI, new String[]{COUNT_SELECT}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndex(BaseColumns._COUNT));
            }
        } finally {
            close(cursor);
        }
        return -1;
    }

    public static List<LocationPacket> queryNextLocations(Context context, int limit) {
        List<LocationPacket> packets = new ArrayList<LocationPacket>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Uri.withAppendedPath(Uri.withAppendedPath(Uri.withAppendedPath(LOCATION_URI, null), null), String.valueOf(limit)), null, null, null, LocationColumns._ID);
            if (cursor.moveToFirst()) {
                do {
                    packets.add(cursorToLocationEntity(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            close(cursor);
        }
        return packets;
    }

    public static int deleteLocations(Context context, List<LocationPacket> packets) {
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
        return context.getContentResolver().delete(LOCATION_URI, whereClause.toString(), whereArgs);
    }

    private static LocationPacket cursorToLocationEntity(Cursor cursor) {
        LocationPacket locationEntity = AppConstants.GSON.fromJson(cursor.getString(cursor.getColumnIndex(LocationColumns.PACKET)), LocationPacket.class);
        locationEntity.rowId = cursor.getLong(cursor.getColumnIndex(LocationColumns._ID));
        return locationEntity;
    }

    protected static void close(Cursor cursor) {
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            // do noting
        }
    }

}