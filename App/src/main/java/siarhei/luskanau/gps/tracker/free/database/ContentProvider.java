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

package siarhei.luskanau.gps.tracker.free.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

public class ContentProvider extends android.content.ContentProvider {

    public static final String RAW_QUERY_SEGMENT = "rawQuery";

    private DataBaseHelper dataBaseHelper;

    public static Uri getProviderAuthorityUri(Context context) {
        return Uri.parse("content://" + context.getPackageName() + ".provider");
    }

    @Override
    public boolean onCreate() {
        dataBaseHelper = new DataBaseHelper(this);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        List<String> pathSegments = uri.getPathSegments();
        String table = null;
        String groupBy = null;
        String having = null;
        String limit = null;
        if (pathSegments.size() == 2 && RAW_QUERY_SEGMENT.equals(uri.getPathSegments().get(0))) {
            return dataBaseHelper.getReadableDatabase().rawQuery(uri.getPathSegments().get(1), selectionArgs);
        }
        switch (pathSegments.size()) {
            case 4:
                limit = uri.getPathSegments().get(3);
                if ("null".equals(limit)) {
                    limit = null;
                }
            case 3:
                groupBy = uri.getPathSegments().get(2);
                if ("null".equals(groupBy)) {
                    groupBy = null;
                }
            case 2:
                having = uri.getPathSegments().get(1);
                if ("null".equals(having)) {
                    having = null;
                }
            case 1:
                table = uri.getPathSegments().get(0);
        }
        return dataBaseHelper.getReadableDatabase().query(table, projection, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = uri.getPathSegments().get(0);
        long id = dataBaseHelper.getWritableDatabase().insertOrThrow(table, null, values);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = uri.getPathSegments().get(0);
        return dataBaseHelper.getWritableDatabase().delete(table, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table = uri.getPathSegments().get(0);
        return dataBaseHelper.getWritableDatabase().update(table, values, selection, selectionArgs);
    }

}