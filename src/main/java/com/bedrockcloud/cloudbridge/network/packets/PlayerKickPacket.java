package com.bedrockcloud.cloudbridge.network.packets;

import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class PlayerKickPacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject) {
        final String reason = jsonObject.get("reason").toString();
        final String playerName = jsonObject.get("playerName").toString();
        if (ProxyServer.getInstance().getPlayer(playerName) != null) {
            ProxyServer.getInstance().getScheduler().scheduleTask(() -> ProxyServer.getInstance().getPlayer(playerName).disconnect(reason.replace("&", "§")), false);
        }
    }
}
