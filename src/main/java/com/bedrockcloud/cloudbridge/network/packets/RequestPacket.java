package com.bedrockcloud.cloudbridge.network.packets;

import com.bedrockcloud.cloudbridge.network.DataPacket;

public class RequestPacket extends DataPacket {
    public String requestId;
    public int type;
    
    @Override
    public String encode() {
        this.addValue("requestId", this.requestId);
        this.addValue("type", this.type);
        return super.encode();
    }
}
