package de.bedrockcloud.cloudbridge.listener;

import de.bedrockcloud.cloudbridge.CloudBridge;
import de.bedrockcloud.cloudbridge.api.CloudAPI;
import de.bedrockcloud.cloudbridge.api.player.CloudPlayer;
import de.bedrockcloud.cloudbridge.language.Language;
import de.bedrockcloud.cloudbridge.network.Network;
import de.bedrockcloud.cloudbridge.network.packet.impl.normal.PlayerSwitchServerPacket;
import de.bedrockcloud.cloudbridge.network.packet.impl.normal.PlayerConnectPacket;
import de.bedrockcloud.cloudbridge.network.packet.impl.normal.PlayerDisconnectPacket;
import de.bedrockcloud.cloudbridge.network.packet.impl.request.CheckPlayerMaintenanceRequestPacket;
import de.bedrockcloud.cloudbridge.network.packet.impl.response.CheckPlayerMaintenanceResponsePacket;
import de.bedrockcloud.cloudbridge.network.request.RequestManager;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferRequestEvent;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;

import java.net.InetSocketAddress;

public class EventListener {

    public static void onLogin(PlayerLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (!CloudBridge.getInstance().getAcceptedProtocols().contains(player.getProtocol().getProtocol())) {
            event.setCancelReason("Login failed. (Incompatible Protocol Version)");
            event.setCancelled();
            return;
        }

        Network.getInstance().sendPacket(new PlayerConnectPacket(new CloudPlayer(player.getName(), player.getAddress().getAddress().getHostAddress() + ":" + player.getAddress().getPort(), player.getAddress().getAddress().getHostAddress(), player.getXuid(), player.getUniqueId().toString(), null, null)));

        if (CloudAPI.getInstance().getCurrentTemplate() == null) return;
        if (CloudAPI.getInstance().getCurrentTemplate().isMaintenance()) {
            RequestManager.getInstance().sendRequest(new CheckPlayerMaintenanceRequestPacket(player.getName())).then(responsePacket -> {
                CheckPlayerMaintenanceResponsePacket checkPlayerMaintenanceResponsePacket = (CheckPlayerMaintenanceResponsePacket) responsePacket;
                if (!checkPlayerMaintenanceResponsePacket.isValue()) {
                    player.disconnect(Language.current().translate("inGame.template.kick.maintenance"));
                }
            });
        }
    }

    public static void onDisconnected(PlayerDisconnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Network.getInstance().sendPacket(new PlayerDisconnectPacket(player.getName()));
    }

    public static void onTransfer(ServerTransferRequestEvent event) {
        Network.getInstance().sendPacket(new PlayerSwitchServerPacket(event.getPlayer().getName(), event.getTargetServer().getServerName()));
    }
}
