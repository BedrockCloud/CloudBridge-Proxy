package com.bedrockcloud.cloudbridge.network.packets;

import com.bedrockcloud.BedrockCore;
import com.bedrockcloud.cloudbridge.CloudBridge;
import com.bedrockcloud.cloudbridge.network.DataPacket;
import org.json.simple.JSONObject;

public class KeepALivePacket extends DataPacket {

    @Override
    public void handle(JSONObject jsonObject) {
        KeepALivePacket pk = this;
        pk.addValue("serverName", BedrockCore.getInstance().getCloudBridge().getServerName());
        this.pushPacket(pk);
    }
}
