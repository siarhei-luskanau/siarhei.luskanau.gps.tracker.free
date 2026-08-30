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

package siarhei.luskanau.gps.tracker.free.model

import android.provider.BaseColumns
import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

@Entity(tableName = "ServerEntity")
class ServerEntity {

    companion object {
        @JvmField
        val COLLECTION_TYPE: java.lang.reflect.Type =
            object : TypeToken<List<ServerEntity>>() {}.type
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID)
    @SerializedName("rowId")
    @JvmField
    var rowId: Long? = null

    @SerializedName("name")
    @JvmField
    var name: String? = null

    @SerializedName("site_url")
    @JvmField
    var site_url: String? = null

    @SerializedName("server_type")
    @JvmField
    var serverType: ServerType? = null

    @SerializedName("server_address")
    @JvmField
    var server_address: String? = null

    @SerializedName("server_port")
    @JvmField
    var server_port: Int = 0

    @SerializedName("custom")
    @JvmField
    var custom: Boolean = false
}
