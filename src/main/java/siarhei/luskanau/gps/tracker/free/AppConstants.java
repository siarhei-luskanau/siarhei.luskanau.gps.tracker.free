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

package siarhei.luskanau.gps.tracker.free;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppConstants {

    public static final String SATELLITES = "satellites";
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz", Locale.ENGLISH);
    public static final String ACTION_TRACKER_STARTED_CHANGED = "siarhei.luskanau.tracker.ACTION_TRACKER_STARTED_CHANGED";
    public static final String ACTION_PACKET_COUNTER_CHANGED = "siarhei.luskanau.tracker.ACTION_PACKET_COUNTER_CHANGED";
    public static final String ACTION_LAST_LOCATION_PACKET_CHANGED = "siarhei.luskanau.tracker.ACTION_LAST_LOCATION_PACKET_CHANGED";
    public static final Type STRING_LIST_TYPE_TOKEN = new TypeToken<List<String>>() {
    }.getType();
    public static final Type STRING_MAP_TYPE_TOKEN = new TypeToken<Map<String, String>>() {
    }.getType();
    public static final String SERVER_TYPE_SOCKET = "socket";
    public static final String SERVER_TYPE_JSON_FORM = "json_form";
    public static final String SERVER_TYPE_JSON_BODY = "json_body";
    private final static JsonSerializer<byte[]> BYTE_ARRAY_JSON_SERIALIZER = new JsonSerializer<byte[]>() {
        @Override
        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(Base64.encodeToString(src, Base64.DEFAULT));
        }
    };
    private final static JsonDeserializer<byte[]> BYTE_ARRAY_JSON_DESERIALIZER = new JsonDeserializer<byte[]>() {
        @Override
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : Base64.decode(json.getAsString(), Base64.DEFAULT);
        }
    };
    private final static JsonSerializer<Date> DATE_JSON_SERIALIZER = new JsonSerializer<Date>() {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return src == null ? null : new JsonPrimitive(String.valueOf(src.getTime()));
        }
    };
    private final static JsonDeserializer<Date> DATE_JSON_DESERIALIZER = new JsonDeserializer<Date>() {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json == null ? null : new Date(Long.valueOf(json.getAsString()));
        }
    };
    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(byte[].class, BYTE_ARRAY_JSON_SERIALIZER)
            .registerTypeAdapter(byte[].class, BYTE_ARRAY_JSON_DESERIALIZER)
            .registerTypeAdapter(Date.class, DATE_JSON_SERIALIZER)
            .registerTypeAdapter(Date.class, DATE_JSON_DESERIALIZER)
            .setPrettyPrinting().create();

}
