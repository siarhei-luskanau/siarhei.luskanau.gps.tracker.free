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
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
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
    public static final Type STRING_LIST_TYPE_TOKEN = new TypeToken<List<String>>() {
    }.getType();
    public static final Type STRING_MAP_TYPE_TOKEN = new TypeToken<Map<String, String>>() {
    }.getType();

    public final static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(byte[].class, new ByteArrayJsonAdapter())
            .registerTypeAdapter(Date.class, new DateJsonAdapter())
            .setPrettyPrinting().serializeNulls().create();

    public static class ByteArrayJsonAdapter extends TypeAdapter<byte[]> {
        @Override
        public void write(JsonWriter out, byte[] value) throws IOException {
            if (value != null) {
                out.value(Base64.encodeToString(value, Base64.DEFAULT));
            } else if (out.getSerializeNulls()) {
                out.nullValue();
            }
        }

        @Override
        public byte[] read(JsonReader in) throws IOException {
            JsonElement jsonElement = GSON.fromJson(in, JsonElement.class);
            if (jsonElement != null && jsonElement.isJsonPrimitive()) {
                return Base64.decode(jsonElement.getAsString(), Base64.DEFAULT);
            }
            return null;
        }
    }

    public static class DateJsonAdapter extends TypeAdapter<Date> {
        @Override
        public void write(JsonWriter out, Date value) throws IOException {
            if (value != null) {
                out.value(value.getTime());
            } else if (out.getSerializeNulls()) {
                out.nullValue();
            }
        }

        @Override
        public Date read(JsonReader in) throws IOException {
            JsonElement jsonElement = GSON.fromJson(in, JsonElement.class);
            return new Date(jsonElement.getAsLong());
        }
    }

}
