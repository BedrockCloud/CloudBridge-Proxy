package com.bedrockcloud.network.packets;

import com.bedrockcloud.config.Config;
import com.bedrockcloud.network.DataPacket;

public class CloudPlayerJoinPacket extends DataPacket
{
    public String playerName;
    public String joinedServer;
    public String address;
    public String uuid;
    public String xuid;
    
    @Override
    public String encode() {
        final Config config = new Config("./cloud.yml", 2);
        this.addValue("playerName", this.playerName);
        this.addValue("address", this.address);
        this.addValue("uuid", this.uuid);
        this.addValue("xuid", this.xuid);
        this.addValue("currentServer", this.joinedServer);
        this.addValue("currentProxy", config.get("name", "PROXY_SERVER_NAME"));
        this.addValue("joinedServer", this.joinedServer);
        return super.encode();
    }
}
