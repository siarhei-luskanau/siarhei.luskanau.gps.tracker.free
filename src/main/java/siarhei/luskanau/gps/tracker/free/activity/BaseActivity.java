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

package siarhei.luskanau.gps.tracker.free.activity;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import siarhei.luskanau.gps.tracker.free.database.ContentProvider;

public class BaseActivity extends ActionBarActivity {

    protected final static String COUNT_SELECT = "count(*) as " + BaseColumns._COUNT;
    private static final String TAG = "BaseDAO";

    protected static void close(Cursor cursor) {
        try {
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            // do noting
        }
    }

    public static synchronized long queryCount(Context context, String tableName) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.withAppendedPath(ContentProvider.URI, tableName);
            cursor = context.getContentResolver().query(uri, new String[]{COUNT_SELECT}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndex(BaseColumns._COUNT));
            }
        } finally {
            close(cursor);
        }
        return -1;
    }

    public static synchronized long queryCount(Context context, String inTables, String selection, String[] whereArgs) {
        Cursor cursor = null;
        try {
            Uri uri = Uri.withAppendedPath(ContentProvider.URI, inTables);
            cursor = context.getContentResolver().query(uri, new String[]{COUNT_SELECT}, selection, whereArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndex(BaseColumns._COUNT));
            }
        } finally {
            close(cursor);
        }
        return -1;
    }

}