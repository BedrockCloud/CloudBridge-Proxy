package de.bedrockcloud.cloudbridge.network.packet.impl.normal;

import de.bedrockcloud.cloudbridge.api.CloudAPI;
import de.bedrockcloud.cloudbridge.api.registry.Registry;
import de.bedrockcloud.cloudbridge.api.server.CloudServer;
import de.bedrockcloud.cloudbridge.network.packet.CloudPacket;
import de.bedrockcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ServerSyncPacket extends CloudPacket {

    private CloudServer server;
    private boolean removal;

    public ServerSyncPacket(CloudServer server, boolean removal) {
        this.server = server;
        this.removal = removal;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeServer(server);
        packetData.write(removal);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        server = packetData.readServer();
        removal = packetData.readBool();
    }

    @Override
    public void handle() {
        if (CloudAPI.getInstance().getServerByName(server.getName()) == null) {
            if (!removal) Registry.registerServer(server);
        } else {
            if (removal) {
                Registry.unregisterServer(server.getName());
            } else Registry.updateServer(server.getName(), server.getServerStatus());
        }
    }

}
