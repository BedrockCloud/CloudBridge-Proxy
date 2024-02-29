package com.bedrockcloud.network.packets;

import com.bedrockcloud.network.DataPacket;
import dev.waterdog.waterdogpe.event.defaults.InitialServerDeterminedEvent;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import dev.waterdog.waterdogpe.event.Event;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.ProxyServer;
import com.bedrockcloud.CloudBridge;
import org.json.simple.JSONObject;

public class UnregisterServerPacket extends DataPacket
{
    
    @Override
    public void handle(final JSONObject jsonObject) {
        final String serverName = jsonObject.get("serverName").toString();
        final ServerInfo server = ProxyServer.getInstance().getServerInfo(serverName);

        if (server != null) {
            if (!server.getServerName().equals("Lobby")) {
                ProxyServer.getInstance().getServerInfoMap().remove(serverName);
            } else {
                ProxyServer.getInstance().getServerInfoMap().remove(serverName);
                CloudBridge.getInstance().joinHandler.removeDefault(server);
                ProxyServer.getInstance().getConfiguration().getPriorities().remove(serverName);
            }
        }

        for (final ProxiedPlayer player : server.getPlayers()) {
            final ServerInfo initialServer = CloudBridge.getInstance().getProxy().getJoinHandler().determineServer(player);
            if (initialServer == null) {
                player.disconnect(
                        "§6BedrockCloud" +
                                "\n" +
                                "§cNo free Cloud-Server found."
                );
                if (player.getServerInfo().getServerName().equals("Lobby") && serverName.equals("Lobby")){
                    player.disconnect(
                            "§6BedrockCloud" +
                                    "\n" +
                                    "§cLobbyServer was stopped."
                    );
                }
            } else {
                final InitialServerDeterminedEvent serverEvent = new InitialServerDeterminedEvent(player, initialServer);
                CloudBridge.getInstance().getProxy().getEventManager().callEvent((Event)serverEvent);
                player.connect(initialServer);
            }
        }
    }
}