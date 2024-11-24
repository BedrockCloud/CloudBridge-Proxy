package de.bedrockcloud.cloudbridge.network.packet.impl.normal;

import de.bedrockcloud.cloudbridge.network.Network;
import de.bedrockcloud.cloudbridge.network.packet.CloudPacket;
import de.bedrockcloud.cloudbridge.network.packet.impl.types.CommandExecutionResult;
import de.bedrockcloud.cloudbridge.network.packet.utils.PacketData;
import de.bedrockcloud.cloudbridge.util.CloudCommandSender;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommandSendPacket extends CloudPacket {

    private String commandLine;

    public CommandSendPacket(String commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.write(commandLine);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        commandLine = packetData.readString();
    }

    @Override
    public void handle() {
        CloudCommandSender cloudCommandSender = new CloudCommandSender();
        ProxyServer.getInstance().dispatchCommand(cloudCommandSender, commandLine);
        Network.getInstance().sendPacket(new CommandSendAnswerPacket(
                new CommandExecutionResult(commandLine, cloudCommandSender.getCachedMessages())
        ));
    }

}
