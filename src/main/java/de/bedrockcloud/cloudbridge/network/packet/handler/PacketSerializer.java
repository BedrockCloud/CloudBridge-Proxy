package de.bedrockcloud.cloudbridge.network.packet.handler;

import com.google.gson.Gson;
import de.bedrockcloud.cloudbridge.network.packet.CloudPacket;
import de.bedrockcloud.cloudbridge.network.packet.pool.PacketPool;
import de.bedrockcloud.cloudbridge.network.packet.utils.PacketData;
import de.bedrockcloud.cloudbridge.util.GeneralSettings;
import de.bedrockcloud.cloudbridge.util.Utils;
import dev.waterdog.waterdogpe.logger.MainLogger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class PacketSerializer {

    public static String encode(CloudPacket packet) {
        PacketData buffer = new PacketData();
        packet.encode(buffer);
        try {
            String jsonString = new Gson().toJson(buffer.getData());
            if (GeneralSettings.isNetworkEncryptionEnabled()) {
                return new String(Utils.compress(jsonString));
            } else return jsonString;
        } catch (Exception e) {
            MainLogger.getLogger().error("§cFailed to encode packet: §e" + packet.getClass().getSimpleName());
            MainLogger.getLogger().throwing(e);
        }

        return "";
    }

    public static CloudPacket decode(String buffer) {
        try {
            if (buffer.isBlank()) return null;
            if (GeneralSettings.isNetworkEncryptionEnabled()) buffer = Utils.decompress(buffer.getBytes(StandardCharsets.UTF_8));
            ArrayList<Object> data = (ArrayList<Object>) new Gson().fromJson(buffer, ArrayList.class);
            if (!data.isEmpty()) {
                CloudPacket packet = PacketPool.getInstance().getPacketById(data.get(0).toString());
                if (packet != null) {
                    packet.decode(new PacketData(data));
                    return packet;
                }
            }
        } catch (Exception e) {
            MainLogger.getLogger().error("§cFailed to decode a packet!");
            MainLogger.getLogger().throwing(e);
        }
        return null;
    }
}
