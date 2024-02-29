package com.bedrockcloud.network;

import java.io.*;
import java.net.*;

import com.bedrockcloud.CloudBridge;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.JSONObject;

public class NetworkManager implements Runnable {
    public DatagramSocket datagramSocket;

    public NetworkManager(final int Port) {
        try {
            this.datagramSocket = new DatagramSocket(Port);
            this.datagramSocket.setReuseAddress(true);
            ProxyServer.getInstance().getLogger().info("§aNetworkManager started on port §e" + Port + "§7.");
        } catch (IOException e) {
            ProxyServer.getInstance().getLogger().error("", e);
        }
        CloudBridge.getInstance().getThreadPool().submit(this);
    }

    public void sendPacket(final DatagramPacket datagramPacket) {
        if (this.datagramSocket != null && !this.datagramSocket.isClosed()) {
            try {
                this.datagramSocket.send(datagramPacket);
            } catch (IOException e) {
                ProxyServer.getInstance().getLogger().error("", e);
            }
        } else {
            ProxyServer.getInstance().getLogger().warning("DatagramSocket is null or closed.");
        }
    }

    @Override
    public void run() {
        while (true) {
            if (this.datagramSocket != null && !this.datagramSocket.isClosed()) {
                if (isLocalHost()) {
                    try {
                        // Wait for a request from the client
                        byte[] buffer = new byte[2048];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        datagramSocket.receive(packet);

                        // Read and process the request
                        DataInputStream dis = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(packet.getData()), 2048));
                        String line = dis.readLine();
                        if (line == null) {
                            return;
                        }

                        JSONObject obj = new JSONObject(line);
                        String jsonString = obj.toString();

                        CloudBridge.getPacketHandler().handleCloudPacket(CloudBridge.getPacketHandler().handleJsonObject(CloudBridge.getPacketHandler().getPacketNameByRequest(jsonString), jsonString));
                    } catch (IOException e) {
                        // Handle the exception
                    }
                }
            }
        }
    }

    /**
     *
     * This is needed to check if is the address an local address.
     */
    public boolean isLocalHost(){
        return this.datagramSocket.getLocalAddress().isAnyLocalAddress();
    }
}