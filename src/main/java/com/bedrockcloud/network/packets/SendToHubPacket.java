package com.bedrockcloud.network.packets;

import com.bedrockcloud.CloudBridge;
import com.bedrockcloud.network.DataPacket;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;

public class SendToHubPacket extends DataPacket
{

    @Override
    public void handle(final JSONObject jsonObject) {
        final String playerName = jsonObject.get("playerName").toString();
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player != null){
            CloudBridge.getInstance().getJoinHandler().determineServer(((ProxiedPlayer) player), ((ProxiedPlayer) player).getServerInfo());
        }
    }
}
