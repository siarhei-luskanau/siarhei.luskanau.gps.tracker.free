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

package siarhei.luskanau.gps.tracker.free.utils.bugreport;

import android.os.Build;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BuildDebugInfo {

    public static Map<String, Object> createBuildDebugInfo() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            putFieldsInfo(map, Build.class);
            putFieldsInfo(map, Build.VERSION.class);
            putFieldsInfo(map, Build.VERSION_CODES.class);
            return map;
        } catch (Throwable t) {
        }
        return null;
    }

    private static void putFieldsInfo(Map<String, Object> map, Class<?> clazz) {
        try {
            Map<String, Object> json = new HashMap<String, Object>();
            for (Field field : clazz.getDeclaredFields()) {
                try {
                    String fieldName = field.getName();
                    Object value = field.get(null);
                    json.put(fieldName, value);
                } catch (Exception e) {
                }
            }
            map.put(clazz.getSimpleName(), json);
        } catch (Exception e) {
        }
    }

}
