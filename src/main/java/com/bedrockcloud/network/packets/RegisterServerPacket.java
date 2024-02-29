package com.bedrockcloud.network.packets;

import com.bedrockcloud.network.DataPacket;
import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.CloudBridge;
import com.bedrockcloud.manager.ServerManager;
import org.json.simple.JSONObject;

public class RegisterServerPacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject) {
        final String serverName = jsonObject.get("serverName").toString();
        final String port = jsonObject.get("serverPort").toString();
        ServerManager.unregisterServer(serverName);
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServerManager.registerServer(serverName, "127.0.0.1", Integer.parseInt(port));
        if (serverName.contains("Lobby")) {
            CloudBridge.getInstance().joinHandler.addDefault(ProxyServer.getInstance().getServerInfo(serverName));
        }
    }
}
