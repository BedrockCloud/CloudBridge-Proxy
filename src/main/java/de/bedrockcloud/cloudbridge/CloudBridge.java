package de.bedrockcloud.cloudbridge;

import de.bedrockcloud.cloudbridge.api.CloudAPI;
import de.bedrockcloud.cloudbridge.handler.JoinHandler;
import de.bedrockcloud.cloudbridge.handler.ReconnectHandler;
import de.bedrockcloud.cloudbridge.language.Language;
import de.bedrockcloud.cloudbridge.listener.EventListener;
import de.bedrockcloud.cloudbridge.network.Network;
import de.bedrockcloud.cloudbridge.network.packet.impl.normal.DisconnectPacket;
import de.bedrockcloud.cloudbridge.network.packet.impl.types.DisconnectReason;
import de.bedrockcloud.cloudbridge.task.ChangeStatusTask;
import de.bedrockcloud.cloudbridge.task.TimeoutTask;
import de.bedrockcloud.cloudbridge.util.GeneralSettings;
import de.bedrockcloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.ProxyServer;
import dev.waterdog.waterdogpe.event.defaults.PlayerDisconnectedEvent;
import dev.waterdog.waterdogpe.event.defaults.PlayerLoginEvent;
import dev.waterdog.waterdogpe.event.defaults.ServerTransferRequestEvent;
import dev.waterdog.waterdogpe.network.protocol.ProtocolVersion;
import dev.waterdog.waterdogpe.player.ProxiedPlayer;
import dev.waterdog.waterdogpe.plugin.Plugin;
import lombok.Getter;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CloudBridge extends Plugin {

    @Getter
    private static CloudBridge instance;
    @Getter
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    @Getter
    private Network network;
    public long lastKeepALiveCheck = 0;

    @Override
    public void onStartup() {
        instance = this;
        GeneralSettings.sync();

        saveResource("config.yml");

        try {
            network = new Network(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), GeneralSettings.getNetworkPort()));
        } catch (UnknownHostException e) {
            ProxyServer.getInstance().getLogger().error("Failed to build the network", e);
        }
    }

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getEventManager().subscribe(PlayerLoginEvent.class, EventListener::onLogin);
        ProxyServer.getInstance().getEventManager().subscribe(PlayerDisconnectedEvent.class, EventListener::onDisconnected);
        ProxyServer.getInstance().getEventManager().subscribe(ServerTransferRequestEvent.class, EventListener::onTransfer);

        lastKeepALiveCheck = Utils.time();
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new ChangeStatusTask(), 20);
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new TimeoutTask(), 20);

        ProxyServer.getInstance().setJoinHandler(new JoinHandler());
        ProxyServer.getInstance().setReconnectHandler(new ReconnectHandler());

        CloudAPI.getInstance().processLogin();
    }

    @Override
    public void onDisable() {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers().values()) player.disconnect(Language.current().translate("inGame.proxy.stopped"));
        network.sendPacket(new DisconnectPacket(DisconnectReason.SERVER_SHUTDOWN));
        network.close();
        threadPool.shutdownNow();
    }

    public List<Integer> getAcceptedProtocols() {
        List<Integer> acceptedProtocols = new ArrayList<>(ProtocolVersion.latest().getProtocol());
        return getConfig().getList("acceptedProtocols", acceptedProtocols);
    }
}