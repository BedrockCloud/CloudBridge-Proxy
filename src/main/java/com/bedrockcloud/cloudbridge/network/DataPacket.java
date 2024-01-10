package com.bedrockcloud.cloudbridge.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

import com.bedrockcloud.cloudbridge.CloudBridge;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONValue;
import com.bedrockcloud.cloudbridge.config.Config;
import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.Map;

import java.net.*;

public class DataPacket {
    public final int TYPE_REQUEST = 0;
    public final int TYPE_RESPONSE = 1;

    public Map<String, Object> data;

    public DataPacket() {
        this.data = new HashMap<String, Object>();
    }

    public void addValue(final String key, final String value) {
        this.data.put(key, value);
    }

    public void addValue(final String key, final int value) {
        this.data.put(key, value);
    }

    public void handle(final JSONObject jsonObject) {
    }

    public String encode() {
        this.addValue("packetName", this.getClass().getSimpleName());
        final Config config = new Config("./cloud.yml", 2);
        this.addValue("serverName", config.get("name", "PROXY_SERVER_NAME"));
        return JSONValue.toJSONString(this.data);
    }

    public void pushPacket(final DataPacket cloudPacket) {
        try (DatagramSocket s = new DatagramSocket(32324)) {
            if (s.isClosed()) {
                return;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                byteArrayOutputStream.write(cloudPacket.encode().getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            byte[] data = byteArrayOutputStream.toByteArray();
            InetAddress address = null;
            try {
                address = InetAddress.getByName("127.0.0.1");
            } catch (UnknownHostException ignored) {
            }
            int port = CloudBridge.getCloudPort();
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, address, port);
            DatagramSocket datagramSocket = null;
            try {
                datagramSocket = new DatagramSocket();
            } catch (SocketException ex) {
                ProxyServer.getInstance().getLogger().error("", ex);
            }
            try {
                datagramSocket.send(datagramPacket);
            } catch (IOException ex) {
                ProxyServer.getInstance().getLogger().error("", ex);
            }
        } catch (Exception exception){
            ProxyServer.getInstance().getLogger().error("", exception);
        }
    }
}