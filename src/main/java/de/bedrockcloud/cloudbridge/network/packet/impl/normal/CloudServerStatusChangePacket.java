package de.bedrockcloud.cloudbridge.network.packet.impl.normal;

import de.bedrockcloud.cloudbridge.api.server.status.ServerStatus;
import de.bedrockcloud.cloudbridge.network.packet.CloudPacket;
import de.bedrockcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CloudServerStatusChangePacket extends CloudPacket {

    private ServerStatus newStatus;

    public CloudServerStatusChangePacket(ServerStatus newStatus) {
        this.newStatus = newStatus;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeServerStatus(newStatus);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        newStatus = packetData.readServerStatus();
    }

    @Override
    public void handle() {}

}
