package com.bedrockcloud.network.packets;

import com.bedrockcloud.network.DataPacket;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;

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
