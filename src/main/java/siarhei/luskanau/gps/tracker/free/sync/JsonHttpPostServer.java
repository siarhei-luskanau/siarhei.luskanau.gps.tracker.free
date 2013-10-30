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

package siarhei.luskanau.gps.tracker.free.sync;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import siarhei.luskanau.gps.shared.LocationPacket;
import siarhei.luskanau.gps.tracker.free.AppConstants;

public class JsonHttpPostServer {

    private static final String TAG = "JsonHttpServer";
    private static final int CONNECTION_TIMEOUT = 7 * 1000;
    private static final boolean DEBUG = false;

    public static String sendLocationsForm(String url, List<LocationPacket> locationEntities)
            throws Exception {
        String requestJsonString = AppConstants.GSON.toJson(locationEntities);

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("track", requestJsonString));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        if (DEBUG) {
            Log.d(TAG, "Send request: " + httpPost.getURI() + " " + requestJsonString);
        }
        String responseJsonString = read(requestInputStream(httpPost));
        if (DEBUG) {
            Log.d(TAG, "Received response: " + responseJsonString);
        }
        return responseJsonString;
    }

    public static String sendLocations(String url, List<LocationPacket> locationEntities) throws Exception {
        String requestJsonString = AppConstants.GSON.toJson(locationEntities);

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
        httpPost.setEntity(new StringEntity(requestJsonString));

        if (DEBUG) {
            Log.d(TAG, "Send request: " + httpPost.getURI() + " " + requestJsonString);
        }
        String responseJsonString = read(requestInputStream(httpPost));
        if (DEBUG) {
            Log.d(TAG, "Received response: " + responseJsonString);
        }
        return responseJsonString;
    }

    private static InputStream requestInputStream(HttpUriRequest httpUriRequest) throws Exception {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(httpParams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);

        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpResponse httpResponse = httpClient.execute(httpUriRequest);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode != 200 && statusCode != 201) {
            String responseString = null;
            try {
                responseString = read(httpResponse.getEntity().getContent());
            } catch (Throwable t) {
                responseString = null;
            }
            throw new HttpResponseException(statusCode, statusCode + " "
                    + httpResponse.getStatusLine().getReasonPhrase() + "\n" + responseString);
        }
        return httpResponse.getEntity().getContent();
    }

    private static String read(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[8192];
        int read;
        StringBuilder sb = new StringBuilder();
        while ((read = inputStream.read(buffer)) != -1) {
            if (read > 0) {
                sb.append(new String(buffer, 0, read));
            } else {
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        return sb.toString();
    }

}
