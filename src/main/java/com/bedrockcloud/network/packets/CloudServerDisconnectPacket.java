package com.bedrockcloud.network.packets;

import com.bedrockcloud.network.DataPacket;
import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;

public class CloudServerDisconnectPacket extends DataPacket {

    @Override
    public void handle(final JSONObject jsonObject) {
        ProxyServer.getInstance().shutdown();
    }
}
