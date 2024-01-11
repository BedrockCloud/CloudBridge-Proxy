package com.bedrockcloud.cloudbridge;

import com.bedrockcloud.BedrockCore;
import com.bedrockcloud.cloudbridge.network.packets.*;
import com.bedrockcloud.cloudbridge.config.Config;

import com.bedrockcloud.cloudbridge.network.handler.PacketHandler;
import com.bedrockcloud.cloudbridge.network.NetworkManager;
import dev.waterdog.waterdogpe.ProxyServer;

public class CloudBridge
{
    private static NetworkManager networkManager;
    private static PacketHandler packetHandler;
    private static int port;
    private static int cloudPort;
    private final String serverName;
    
    public CloudBridge() {
        CloudBridge.port = Integer.parseInt(ProxyServer.getInstance().getConfiguration().getBindAddress().toString().split(":")[1])+1;
        CloudBridge.cloudPort = (int)BedrockCore.getInstance().getProxyConfig().getDouble("cloud-port").doubleValue();
        CloudBridge.packetHandler = new PacketHandler();

        registerPacket();
        CloudBridge.networkManager = new NetworkManager(getSocketPort());
        final CloudServerConnectPacket packet = new CloudServerConnectPacket();
        packet.pushPacket(packet);
        final Config config = new Config("./cloud.yml", 2);
        this.serverName = (String)config.get("name");
    }
    
    public static void registerPacket() {
        getPacketHandler().registerPacket(CloudServerConnectPacket.class);
        getPacketHandler().registerPacket(CloudServerDisconnectPacket.class);
        getPacketHandler().registerPacket(RegisterServerPacket.class);
        getPacketHandler().registerPacket(UnregisterServerPacket.class);
        getPacketHandler().registerPacket(CloudPlayerJoinPacket.class);
        getPacketHandler().registerPacket(CloudPlayerQuitPacket.class);
        getPacketHandler().registerPacket(PlayerTextPacket.class);
        getPacketHandler().registerPacket(CloudNotifyMessagePacket.class);
        getPacketHandler().registerPacket(PlayerMovePacket.class);
        getPacketHandler().registerPacket(PlayerKickPacket.class);
        getPacketHandler().registerPacket(CloudPlayerChangeServerPacket.class);
        getPacketHandler().registerPacket(SendToHubPacket.class);
        getPacketHandler().registerPacket(KeepALivePacket.class);
    }
    
    public static PacketHandler getPacketHandler() {
        return CloudBridge.packetHandler;
    }
    
    public static NetworkManager getNetworkManager() {
        return CloudBridge.networkManager;
    }
    
    public static int getSocketPort() {
        return CloudBridge.port;
    }

    public static int getCloudPort() {
        return cloudPort;
    }

    public String getServerName() {
        return this.serverName;
    }
}
