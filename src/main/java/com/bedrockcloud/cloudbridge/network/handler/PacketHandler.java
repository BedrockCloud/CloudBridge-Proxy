package com.bedrockcloud.cloudbridge.network.handler;

import com.bedrockcloud.cloudbridge.network.DataPacket;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler
{

    private static final String PACKET_NAME_KEY = "packetName";
    private Map<String, Class<? extends DataPacket>> registeredPackets;

    public PacketHandler() {
        this.registeredPackets = new HashMap<>();
    }

    public void registerPacket(final Class<? extends DataPacket> packet) {
        String packetName = packet.getSimpleName();
        this.registeredPackets.put(packetName, packet);
    }

    public void unregisterPacket(final String name) {
        this.registeredPackets.remove(name);
    }

    public boolean isPacketRegistered(final String name) {
        return this.registeredPackets.containsKey(name);
    }

    public Map<String, Class<? extends DataPacket>> getRegisteredPackets() {
        return Collections.unmodifiableMap(this.registeredPackets);
    }

    public Class<? extends DataPacket> getPacketByName(final String packetName) {
        return this.registeredPackets.get(packetName);
    }

    public String getPacketNameByRequest(final String request) {
        final Object obj = JSONValue.parse(request);
        if (obj != null) {
            final JSONObject jsonObject = (JSONObject)obj;
            if (jsonObject.get("packetName") != null) {
                return jsonObject.get("packetName").toString();
            }
        }
        ProxyServer.getInstance().getLogger().warning("Handling of packet cancelled because the packet is unknown!");
        return "Unknown Packet";
    }

    public JSONObject handleJsonObject(final String packetName, final String input) {
        if (this.isPacketRegistered(packetName)) {
            final Object obj = JSONValue.parse(input);
            final JSONObject jsonObject = (JSONObject)obj;
            return jsonObject;
        }

        ProxyServer.getInstance().getLogger().warning("§eFailed to handle packet: " + packetName);
        return new JSONObject();
    }

    public void handleCloudPacket(final JSONObject jsonObject) {
        if (jsonObject.containsKey(PACKET_NAME_KEY)) {
            final String packetName = jsonObject.get(PACKET_NAME_KEY).toString();
            final Class<? extends DataPacket> packetClass = this.getPacketByName(packetName);
            if (packetClass != null) {
                try {
                    final DataPacket packet = packetClass.newInstance();
                    packet.handle(jsonObject);
                } catch (InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            } else {
                ProxyServer.getInstance().getLogger().warning("Denied unauthorized server.");
            }
        }
    }
}
