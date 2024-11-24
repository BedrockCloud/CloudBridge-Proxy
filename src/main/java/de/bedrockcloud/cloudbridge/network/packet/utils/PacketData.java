package de.bedrockcloud.cloudbridge.network.packet.utils;

import de.bedrockcloud.cloudbridge.api.player.CloudPlayer;
import de.bedrockcloud.cloudbridge.api.server.CloudServer;
import de.bedrockcloud.cloudbridge.api.server.status.ServerStatus;
import de.bedrockcloud.cloudbridge.api.template.Template;
import de.bedrockcloud.cloudbridge.network.packet.impl.types.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@NoArgsConstructor
public class PacketData {

    private ArrayList<Object> data = new ArrayList<>();

    public PacketData(ArrayList<Object> data) {
        this.data = data;
    }

    public void write(Object v) {
        data.add(v);
    }

    public void writeServer(CloudServer server) {
        write(server.toArray());
    }

    public void writeCommandExecutionResult(CommandExecutionResult commandExecutionResult) {
        write(commandExecutionResult.toArray());
    }

    public void writeLogType(LogType logType) {
        write(logType.name());
    }

    public void writeServerStatus(ServerStatus serverStatus) {
        write(serverStatus.getName());
    }

    public void writeTemplate(Template template) {
        write(template.toArray());
    }

    public void writePlayer(CloudPlayer player) {
        write(player.toArray());
    }

    public void writeDisconnectReason(DisconnectReason disconnectReason) {
        write(disconnectReason.name());
    }

    public void writeErrorReason(ErrorReason errorReason) {
        write(errorReason.name());
    }

    public void writeVerifyStatus(VerifyStatus verifyStatus) {
        write(verifyStatus.name());
    }

    public void writeTextType(TextType textType) {
        write(textType.name());
    }

    public Object read() {
        if (!data.isEmpty()) {
            Object get = data.get(0);
            data.remove(0);
            Collection<Object> oldContent = new ArrayList<>(data);
            data.clear();
            data.addAll(oldContent);
            return get;
        }
        return null;
    }

    public String readString() {
        try {
            return read().toString();
        } catch (Exception e) {
            return "";
        }
    }

    public Integer readInt() {
        try {
            Object read = read();
            return (read instanceof Integer ? (Integer) read : 0);
        } catch (Exception e) {
            return 0;
        }
    }

    public Double readDouble() {
        try {
            Object read = read();
            return (read instanceof Double ? (Double) read : 0.0);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Float readFloat() {
        try {
            Object read = read();
            return (read instanceof Float ? (Float) read : 0.0f);
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public Boolean readBool() {
        try {
            Object read = read();
            return (read instanceof Boolean ? (Boolean) read : false);
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<?> readArray() {
        try {
            Object read = read();
            return (read instanceof ArrayList<?> ? (ArrayList<?>) read : new ArrayList<>());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Map<?,?> readMap() {
        try {
            Object read = read();
            return (read instanceof Map<?,?> ? (Map<?, ?>) read : Map.of());
        } catch (Exception e) {
            return Map.of();
        }
    }

    public CloudServer readServer() {
        try {
            return CloudServer.fromArray(readMap());
        } catch (Exception e) {
            return null;
        }
    }

    public CommandExecutionResult readCommandExecutionResult() {
        try {
            return CommandExecutionResult.fromArray(readMap());
        } catch (Exception e) {
            return null;
        }
    }

    public LogType readLogType() {
        try {
            return LogType.valueOf(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public ServerStatus readServerStatus() {
        try {
            return ServerStatus.valueOf(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public Template readTemplate() {
        try {
            return Template.fromArray(readMap());
        } catch (Exception e) {
            return null;
        }
    }

    public CloudPlayer readPlayer() {
        try {
            return CloudPlayer.fromArray(readMap());
        } catch (Exception e) {
            return null;
        }
    }

    public DisconnectReason readDisconnectReason() {
        try {
            return DisconnectReason.valueOf(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public ErrorReason readErrorReason() {
        try {
            return ErrorReason.valueOf(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public VerifyStatus readVerifyStatus() {
        try {
            return VerifyStatus.valueOf(readString());
        } catch (Exception e) {
            return null;
        }
    }

    public TextType readTextType() {
        try {
            return TextType.valueOf(readString());
        } catch (Exception e) {
            return null;
        }
    }
}