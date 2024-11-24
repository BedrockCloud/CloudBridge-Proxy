package de.bedrockcloud.cloudbridge.network.packet.impl.request;

import de.bedrockcloud.cloudbridge.network.packet.RequestPacket;
import de.bedrockcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckPlayerMaintenanceRequestPacket extends RequestPacket {

    private String player;

    public CheckPlayerMaintenanceRequestPacket(String player) {
        this.player = player;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(player);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        player = packetData.readString();
    }

}