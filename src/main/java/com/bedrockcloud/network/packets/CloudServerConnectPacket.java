package com.bedrockcloud.network.packets;

import com.bedrockcloud.network.DataPacket;
import dev.waterdog.waterdogpe.ProxyServer;

public class CloudServerConnectPacket extends DataPacket
{
    
    @Override
    public String encode() {
        this.addValue("serverPort", Integer.parseInt(ProxyServer.getInstance().getConfiguration().getBindAddress().toString().split(":")[1]));
        this.addValue("serverPid", Math.toIntExact(ProcessHandle.current().pid()));
        return super.encode();
    }
}
