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

package siarhei.luskanau.gps.tracker.free.dao

import android.content.Context
import android.util.Log
import kotlinx.coroutines.runBlocking
import siarhei.luskanau.gps.tracker.free.AppConstants
import siarhei.luskanau.gps.tracker.free.model.ServerEntity
import siarhei.luskanau.gps.tracker.free.server.db.ServerDatabase
import siarhei.luskanau.gps.tracker.free.utils.Utils

object ServerDAO {

    private const val TAG = "ServerDAO"

    @JvmStatic
    fun putServer(context: Context, server: ServerEntity) {
        runBlocking { ServerDatabase.getInstance(context).serverDao().putServer(server) }
    }

    @JvmStatic
    fun getServerByRowId(context: Context, rowId: Long): ServerEntity? =
        runBlocking { ServerDatabase.getInstance(context).serverDao().getServerByRowId(rowId) }

    @JvmStatic
    fun getServers(context: Context): List<ServerEntity> =
        runBlocking { ServerDatabase.getInstance(context).serverDao().getServers() }

    @JvmStatic
    fun getAssetsServers(context: Context): List<ServerEntity> {
        val list = ArrayList<ServerEntity>()
        try {
            val json = String(Utils.getBytes(context.assets.open("servers.json")), Charsets.UTF_8)
            val serverEntities: List<ServerEntity>? =
                AppConstants.GSON.fromJson(json, ServerEntity.COLLECTION_TYPE)
            if (serverEntities != null) {
                list.addAll(serverEntities)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }
        return list
    }
}
