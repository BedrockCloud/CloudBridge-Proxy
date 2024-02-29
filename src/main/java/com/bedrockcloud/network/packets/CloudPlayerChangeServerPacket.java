package com.bedrockcloud.network.packets;

import com.bedrockcloud.network.DataPacket;

public class CloudPlayerChangeServerPacket extends DataPacket
{
    public String playerName;
    public String server;
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        this.addValue("server", this.server);
        return super.encode();
    }
}
