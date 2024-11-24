package de.bedrockcloud.cloudbridge.network.packet.impl.normal;

import de.bedrockcloud.cloudbridge.network.packet.CloudPacket;
import de.bedrockcloud.cloudbridge.network.packet.utils.PacketData;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProxyUnregisterServerPacket extends CloudPacket {

    private String serverName;

    public ProxyUnregisterServerPacket(String serverName) {
        this.serverName = serverName;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(serverName);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        serverName = packetData.readString();
    }

    @Override
    public void handle() {
        ProxyServer.getInstance().removeServerInfo(serverName);
    }

}