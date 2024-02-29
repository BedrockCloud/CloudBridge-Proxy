package com.bedrockcloud.network.packets;

import com.bedrockcloud.CloudBridge;
import com.bedrockcloud.network.DataPacket;
import org.json.simple.JSONObject;

public class KeepALivePacket extends DataPacket {

    @Override
    public void handle(JSONObject jsonObject) {
        KeepALivePacket pk = this;
        pk.addValue("serverName", CloudBridge.getInstance().getServerName());
        this.pushPacket(pk);
    }
}
