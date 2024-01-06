package com.bedrockcloud.cloudbridge.network.packets;

import dev.waterdog.waterdogpe.ProxyServer;
import org.json.simple.JSONObject;
import com.bedrockcloud.cloudbridge.network.DataPacket;

public class ProxyServerDisconnectPacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject) {
        ProxyServer.getInstance().shutdown();
    }
}
