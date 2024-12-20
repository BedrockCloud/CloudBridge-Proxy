package de.bedrockcloud.cloudbridge.network.request;

import de.bedrockcloud.cloudbridge.network.Network;
import de.bedrockcloud.cloudbridge.network.packet.RequestPacket;
import de.bedrockcloud.cloudbridge.network.packet.ResponsePacket;
import de.bedrockcloud.cloudbridge.task.RequestCheckTask;
import dev.waterdog.waterdogpe.ProxyServer;
import lombok.Getter;

import java.util.HashMap;
import java.util.function.Consumer;

public class RequestManager {

    @Getter
    private static RequestManager instance;
    @Getter
    private final HashMap<String, RequestPacket> requests = new HashMap<>();

    public RequestManager() {
        instance = this;
    }

    public RequestPacket sendRequest(RequestPacket requestPacket) {
        requestPacket.prepare();
        requests.put(requestPacket.getRequestId(), requestPacket);
        ProxyServer.getInstance().getScheduler().scheduleRepeating(new RequestCheckTask(requestPacket), 20);
        Network.getInstance().sendPacket(requestPacket);
        return requestPacket;
    }

    public void removeRequest(RequestPacket packet) {
        removeRequest(packet.getRequestId());
    }

    public void removeRequest(String requestId) {
        requests.remove(requestId);
    }

    public void callThen(ResponsePacket responsePacket) {
        if (requests.containsKey(responsePacket.getRequestId())) {
            RequestPacket requestPacket = requests.getOrDefault(responsePacket.getRequestId(), null);
            if (requestPacket != null) {
                for (Consumer<ResponsePacket> then : requestPacket.getThen()) {
                    then.accept(responsePacket);
                }
            }
        }
    }

    public void callFailure(RequestPacket requestPacket) {
        if (requests.containsKey(requestPacket.getRequestId())) {
            if (requestPacket.getFailure() != null) {
                requestPacket.getFailure().accept(requestPacket);
            }
        }
    }

    public RequestPacket getRequest(String requestId) {
        return requests.getOrDefault(requestId, null);
    }
}