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

package siarhei.luskanau.gps.tracker.free.sync.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;

import siarhei.luskanau.gps.shared.LocationPacket;
import siarhei.luskanau.gps.shared.Tr151ModLocationPacketAdapter;
import siarhei.luskanau.gps.tracker.free.AppConstants;
import siarhei.luskanau.gps.tracker.free.database.LocationDAO;
import siarhei.luskanau.gps.tracker.free.settings.AppSettings;
import siarhei.luskanau.gps.tracker.free.settings.ServerEntity;
import siarhei.luskanau.gps.tracker.free.sync.JsonHttpPostServer;
import siarhei.luskanau.gps.tracker.free.sync.StatusResponseEntity;

public class SendLocationTask extends BaseTask {

    private static Socket socket;
    private static OutputStream outputStream;
    private Tr151ModLocationPacketAdapter packetAdapter = new Tr151ModLocationPacketAdapter();

    public SendLocationTask(Context context) {
        super(context);
    }

    @Override
    public void doTask() throws Exception {
        // Log.d(context.getPackageName(), "Start SendLocationTask");
        ServerEntity serverEntity = AppSettings.getServerEntity(context);
        if (AppConstants.SERVER_TYPE_SOCKET.equalsIgnoreCase(serverEntity.server_type)) {
            for (; ; ) {
                List<LocationPacket> locationEntities = LocationDAO.queryNextLocations(context, 100);
                if (locationEntities != null && locationEntities.size() > 0) {
                    openSocket(serverEntity.server_address, serverEntity.server_port);
                    for (LocationPacket locationEntity : locationEntities) {
                        outputStream.write(packetAdapter.tr151ModPacketToString(packetAdapter.fromLocationPacket(locationEntity)).getBytes());
                        outputStream.flush();
                    }
                    LocationDAO.deleteLocations(context, locationEntities);
                    incPacketCounter(context, locationEntities.size());
                    Log.d(context.getPackageName(), "Packet is send: " + locationEntities.size());
                } else {
                    break;
                }
            }
            closeSocket();
        } else if (AppConstants.SERVER_TYPE_JSON_BODY.equalsIgnoreCase(serverEntity.server_type)
                || AppConstants.SERVER_TYPE_JSON_FORM.equalsIgnoreCase(serverEntity.server_type)) {
            for (; ; ) {
                List<LocationPacket> locationEntities = LocationDAO.queryNextLocations(context, 100);
                if (locationEntities != null && locationEntities.size() > 0) {
                    String responseJsonString = null;

                    if (AppConstants.SERVER_TYPE_JSON_BODY.equalsIgnoreCase(serverEntity.server_type)) {
                        responseJsonString = JsonHttpPostServer.sendLocations(serverEntity.server_address, locationEntities);
                    } else if (AppConstants.SERVER_TYPE_JSON_FORM.equalsIgnoreCase(serverEntity.server_type)) {
                        responseJsonString = JsonHttpPostServer.sendLocationsForm(serverEntity.server_address, locationEntities);
                    }
                    StatusResponseEntity statusResponse = AppConstants.GSON.fromJson(responseJsonString, StatusResponseEntity.class);
                    if (statusResponse != null && statusResponse.success) {
                        LocationDAO.deleteLocations(context, locationEntities);
                        incPacketCounter(context, locationEntities.size());
                        Log.d(context.getPackageName(), "Packet is send: " + locationEntities.size() + " " + responseJsonString);
                    } else {
                        Log.e(context.getPackageName(), "Packet is not send: " + responseJsonString);
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void incPacketCounter(Context context, int count) {
        AppSettings.setPacketCounter(context, AppSettings.getPacketCounter(context) + count);
        context.sendBroadcast(new Intent(AppConstants.ACTION_PACKET_COUNTER_CHANGED));
    }

    private void openSocket(String address, int port) throws Exception {
        if (outputStream == null) {
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(address), port);
            socket = new Socket();
            socket.setSoTimeout(60 * 1000);
            socket.connect(socketAddress, 5 * 1000);
            outputStream = socket.getOutputStream();
        }
    }

    private void closeSocket() {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
        }
        outputStream = null;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
        }
        socket = null;
    }

}
