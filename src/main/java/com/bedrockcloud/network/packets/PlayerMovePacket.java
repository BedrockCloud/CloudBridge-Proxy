package com.bedrockcloud.network.packets;

import com.bedrockcloud.network.DataPacket;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;

public class PlayerMovePacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject) {
        final String serverName = jsonObject.get("toServer").toString();
        final String playerName = jsonObject.get("playerName").toString();
        if (ProxyServer.getInstance().getServerInfo(serverName) == null) {
            ProxyServer.getInstance().getLogger().info("This service don't exists");
        } else if (ProxyServer.getInstance().getPlayer(playerName) != null) {
            ProxyServer.getInstance().getPlayer(playerName).connect(ProxyServer.getInstance().getServerInfo(serverName));
        }
    }
}
