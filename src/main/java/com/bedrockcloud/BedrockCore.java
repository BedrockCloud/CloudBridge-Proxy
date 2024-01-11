package com.bedrockcloud;

import com.bedrockcloud.commands.HubCommand;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferRequestEvent;
import dev.waterdog.waterdogpe.network.connection.handler.IJoinHandler;
import dev.waterdog.waterdogpe.network.connection.handler.IReconnectHandler;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.network.serverinfo.ServerInfo;
import com.bedrockcloud.cloudbridge.network.packets.*;
import dev.waterdog.waterdogpe.utils.config.YamlConfig;
import com.bedrockcloud.handler.ReconnectHandler;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import com.bedrockcloud.handler.JoinHandler;
import com.bedrockcloud.cloudbridge.CloudBridge;
import dev.waterdog.waterdogpe.plugin.Plugin;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BedrockCore extends Plugin
{
    private static BedrockCore instance;
    private CloudBridge cloudBridge;
    public JoinHandler joinHandler;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    
    public void onStartup() {
        BedrockCore.instance = this;
    }
    
    public void onEnable() {
        this.cloudBridge = new CloudBridge();
        this.getProxy().getEventManager().subscribe(ServerTransferRequestEvent.class, this::listen);
        this.getProxy().getEventManager().subscribe(PlayerLoginEvent.class, this::onPlayerJoin);
        this.getProxy().getEventManager().subscribe(PlayerDisconnectedEvent.class, this::onPlayerDisconnectListener);
        getInstance().getProxy().setJoinHandler((IJoinHandler)(this.joinHandler = new JoinHandler()));
        getInstance().getProxy().setReconnectHandler((IReconnectHandler)new ReconnectHandler(this.joinHandler));
        this.getProxy().getCommandMap().registerCommand(new HubCommand(this.joinHandler));

        final ServerInfo server = ProxyServer.getInstance().getServerInfo("lobby1");
        if (server != null){
            ProxyServer.getInstance().getServerInfoMap().remove("lobby1");
            ProxyServer.getInstance().getConfiguration().getPriorities().remove("lobby1");
        }
    }

    public JoinHandler getJoinHandler() {
        return joinHandler;
    }

    public void onDisable() {
        final CloudServerDisconnectPacket packet = new CloudServerDisconnectPacket();
        packet.addValue("serverName", this.cloudBridge.getServerName());
        packet.pushPacket(packet);
    }

    public void onPlayerDisconnectListener(PlayerDisconnectedEvent event) {
        final CloudPlayerQuitPacket newpacket = new CloudPlayerQuitPacket();
        newpacket.playerName = event.getPlayer().getName();
        try {
            newpacket.leftServer = event.getPlayer().getServerInfo().getServerName();
        } catch (NullPointerException e) {
            newpacket.leftServer = "NOT FOUND";
        }
        newpacket.pushPacket(newpacket);
    }
    
    private void listen(final ServerTransferRequestEvent event) {
        final ServerInfo server = event.getTargetServer();
        if (server == JoinHandler.EMPTY_SERVER_INFO) {
            event.setCancelled(true);
            return;
        }
        final ServerInfo oldServer = event.getPlayer().getServerInfo();
        if (oldServer != null) {
            final CloudPlayerChangeServerPacket packet = new CloudPlayerChangeServerPacket();
            packet.playerName = event.getPlayer().getName().toLowerCase(Locale.ROOT).replace(" ", "_");
            packet.server = server.getServerName();
            packet.pushPacket(packet);
        }
    }
    
    public void onPlayerJoin(final PlayerLoginEvent event) {
        final ProxiedPlayer player = event.getPlayer();
        final CloudPlayerJoinPacket packet = new CloudPlayerJoinPacket();
        packet.playerName = player.getName().toLowerCase(Locale.ROOT).replace(" ", "_");
        packet.joinedServer = "Loading...";
        packet.address = String.valueOf(player.getAddress());
        packet.xuid = player.getXuid();
        packet.uuid = String.valueOf(player.getUniqueId());
        packet.pushPacket(packet);
    }
    
    public static BedrockCore getInstance() {
        return BedrockCore.instance;
    }
    
    public CloudBridge getCloudBridge() {
        return this.cloudBridge;
    }

    public String getCloudPath() {
        return getProxyConfig().getString("cloud-path");
    }

    public YamlConfig getProxyConfig() {
        File configFile = new File(getProxy().getDataPath().toString() + "/config.yml");
        return new YamlConfig(configFile);
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }
}
