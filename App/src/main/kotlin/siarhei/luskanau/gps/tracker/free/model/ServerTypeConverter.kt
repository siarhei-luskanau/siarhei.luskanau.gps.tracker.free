package siarhei.luskanau.gps.tracker.free.model

import androidx.room3.ColumnTypeConverter

class ServerTypeConverter {

    @ColumnTypeConverter
    fun fromServerType(value: ServerType?): String? = value?.name

    @ColumnTypeConverter
    fun toServerType(value: String?): ServerType? = value?.let { ServerType.valueOf(it) }
}
