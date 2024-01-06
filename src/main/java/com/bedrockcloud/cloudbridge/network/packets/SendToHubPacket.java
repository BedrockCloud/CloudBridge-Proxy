package com.bedrockcloud.cloudbridge.network.packets;

import com.bedrockcloud.BedrockCore;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class SendToHubPacket extends DataPacket
{

    @Override
    public void handle(final JSONObject jsonObject) {
        final String playerName = jsonObject.get("playerName").toString();
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);
        if (player != null){
            BedrockCore.getInstance().getJoinHandler().determineServer(((ProxiedPlayer) player), ((ProxiedPlayer) player).getServerInfo());
        }
    }
}
