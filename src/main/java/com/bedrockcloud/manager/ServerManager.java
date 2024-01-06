package com.bedrockcloud.manager;

import dev.waterdog.waterdogpe.network.serverinfo.BedrockServerInfo;
import java.net.InetSocketAddress;
import dev.waterdog.waterdogpe.ProxyServer;

public class ServerManager
{
    public static void registerServer(final String servername, final String ip, final int port) {
        ProxyServer.getInstance().registerServerInfo(new BedrockServerInfo(servername, new InetSocketAddress(ip, port), null));
        ProxyServer.getInstance().getLogger().info("§8[§2+§8] §r" + servername);
    }
    
    public static void unregisterServer(final String servername) {
        if (ProxyServer.getInstance().getServerInfo(servername) != null) {
            ProxyServer.getInstance().removeServerInfo(servername);
            ProxyServer.getInstance().getLogger().info("§8[§4-§8] §r" + servername);
        }
    }
}
