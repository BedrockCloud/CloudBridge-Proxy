package com.bedrockcloud.network.packets;

import com.bedrockcloud.network.DataPacket;

public class CloudPlayerQuitPacket extends DataPacket
{
    public String playerName;
    public String leftServer;
    
    @Override
    public String encode() {
        this.addValue("playerName", this.playerName);
        this.addValue("leftServer", this.leftServer);
        return super.encode();
    }
}
