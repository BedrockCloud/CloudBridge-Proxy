package de.bedrockcloud.cloudbridge.network.packet.impl.response;

import de.bedrockcloud.cloudbridge.network.packet.ResponsePacket;
import de.bedrockcloud.cloudbridge.network.packet.utils.PacketData;
import de.bedrockcloud.cloudbridge.network.packet.impl.types.VerifyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponsePacket extends ResponsePacket {

    private VerifyStatus verifyStatus;

    public LoginResponsePacket(VerifyStatus verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeVerifyStatus(verifyStatus);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        verifyStatus = packetData.readVerifyStatus();
    }

    @Override
    public void handle() {}

}
