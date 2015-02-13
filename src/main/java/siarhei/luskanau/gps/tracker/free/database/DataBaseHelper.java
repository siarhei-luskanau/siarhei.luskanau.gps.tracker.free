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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    private static final boolean DEBUG = true;
    private static final int DATABASE_VERSION = 14;
    private Context context;

    public DataBaseHelper(ContentProvider contentProvider) {
        super(contentProvider.getContext(), contentProvider.getContext().getPackageName() + ".db", null, DATABASE_VERSION);
        context = contentProvider.getContext();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA FOREIGN_KEYS=ON;");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String sqlString;
            sqlString = LocationColumns.queryCreateTable();
            db.execSQL(sqlString);

            sqlString = ServerColumns.queryCreateTable();
            db.execSQL(sqlString);
        } catch (Throwable tr) {
            Log.e(TAG, "onCreate", tr);
        }
    }

    public void cleanDatabase(SQLiteDatabase db) {
        Log.d(TAG, "Cleaning database which will destroy all old data");

        DataBaseSettings.clear(context);

        queryDropTable(db, ServerColumns.TABLE_NAME);
        queryDropTable(db, LocationColumns.TABLE_NAME);

        onCreate(db);
    }

    private void queryDropTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            try {
                makeChanges(db, oldVersion, newVersion);
            } catch (Throwable tr) {
                Log.e(TAG, tr.getMessage(), tr);
                cleanDatabase(db);
            }
        } catch (Throwable tr) {
            Log.e(TAG, "onUpgrade", tr);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            try {
                makeChanges(db, oldVersion, newVersion);
            } catch (Throwable tr) {
                Log.e(TAG, tr.getMessage(), tr);
                cleanDatabase(db);
            }
        } catch (Throwable tr) {
            Log.e(TAG, "onUpgrade", tr);
        }
    }

    private void makeChanges(SQLiteDatabase db, int oldVersion, int newVersion) throws Exception {
        int toVersion = DATABASE_VERSION;
        int fromVersion = 0;
        if (oldVersion != DATABASE_VERSION) {
            fromVersion = oldVersion;
        }
        if (newVersion != DATABASE_VERSION) {
            fromVersion = newVersion;
        }

        if (toVersion > fromVersion) {
            Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
            for (int i = fromVersion; i < toVersion; i++) {
                String fileName = "upgrade_" + i + "_to_" + (i + 1) + ".sql";
                InputStream inputStream = context.getResources().getAssets().open(fileName);
                String upgradeString = new String(read(inputStream), "UTF-8").trim();
                inputStream.close();
                String[] sqlStrings = upgradeString.split(";");
                if (sqlStrings != null) {
                    for (String sqlString : sqlStrings) {
                        if (DEBUG) {
                            Log.d(TAG, "onUpgrade: " + fileName + ": " + sqlString);
                        }
                        if (sqlString.length() > 0) {
                            db.execSQL(sqlString);
                        }
                    }
                }
            }
        } else {
            Log.d(TAG, "Downgrading database from version " + oldVersion + " to " + newVersion);
            for (int i = fromVersion; i > toVersion; i--) {
                String fileName = "downgrade_" + i + "_to_" + (i - 1) + ".sql";
                InputStream inputStream = context.getResources().getAssets().open(fileName);
                String sqlString = new String(read(inputStream), "UTF-8").trim();
                inputStream.close();
                if (DEBUG) {
                    Log.d(TAG, "onDowngrade: " + fileName + ": " + sqlString);
                }
                if (sqlString.length() > 0) {
                    db.execSQL(sqlString);
                }
            }
        }
    }

    private byte[] read(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((read = inputStream.read(buffer)) != -1) {
            if (read > 0) {
                byteArrayOutputStream.write(buffer, 0, read);
            } else {
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

}
