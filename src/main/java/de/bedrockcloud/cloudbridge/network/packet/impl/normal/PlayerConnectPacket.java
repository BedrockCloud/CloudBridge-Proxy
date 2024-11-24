package de.bedrockcloud.cloudbridge.network.packet.impl.normal;

import de.bedrockcloud.cloudbridge.api.player.CloudPlayer;
import de.bedrockcloud.cloudbridge.network.packet.CloudPacket;
import de.bedrockcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayerConnectPacket extends CloudPacket {

    private CloudPlayer player;

    public PlayerConnectPacket(CloudPlayer player) {
        this.player = player;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writePlayer(player);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        player = packetData.readPlayer();
    }

    @Override
    public void handle() {}

}
