package de.bedrockcloud.cloudbridge.network.packet.impl.normal;

import de.bedrockcloud.cloudbridge.CloudBridge;
import de.bedrockcloud.cloudbridge.network.Network;
import de.bedrockcloud.cloudbridge.network.packet.CloudPacket;
import de.bedrockcloud.cloudbridge.util.Utils;

public class KeepAlivePacket extends CloudPacket {

    @Override
    public void handle() {
        CloudBridge.getInstance().lastKeepALiveCheck = Utils.time();
        Network.getInstance().sendPacket(new KeepAlivePacket());
    }
}