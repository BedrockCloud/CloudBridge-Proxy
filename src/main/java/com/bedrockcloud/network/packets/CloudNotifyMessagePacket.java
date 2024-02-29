package com.bedrockcloud.network.packets;

import com.bedrockcloud.CloudBridge;
import com.bedrockcloud.config.Config;
import com.bedrockcloud.network.DataPacket;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;

import java.io.File;

public class CloudNotifyMessagePacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject) {
        final String message = jsonObject.get("message").toString();
        final String msg = message.replace("&", "§");
        final String fixuni = msg.replace("\u00c2", "");
        for (final ProxiedPlayer p : ProxyServer.getInstance().getPlayers().values()) {
            if ((new File(CloudBridge.getInstance().getCloudPath() + "local/notify/" + p.getName() + ".json").exists())) {
                final Config config = new Config(CloudBridge.getInstance().getCloudPath() + "local/notify/" + p.getName() + ".json", Config.JSON);
                if (config.getBoolean("notify", false)) {
                    p.sendMessage(fixuni);
                }
            }
        }
    }
}
