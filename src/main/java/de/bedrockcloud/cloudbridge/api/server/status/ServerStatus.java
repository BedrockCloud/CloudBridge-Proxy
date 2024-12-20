package de.bedrockcloud.cloudbridge.api.server.status;

public enum ServerStatus {

    STARTING("STARTING", "§2STARTING"),
    ONLINE("ONLINE", "§aONLINE"),
    FULL("FULL", "§eFULL"),
    IN_GAME("IN_GAME", "§6INGAME"),
    STOPPING("STOPPING", "§4STOPPING"),
    OFFLINE("OFFLINE", "§cOFFLINE");

    public static ServerStatus getServerStatusByName(String name) {
        return valueOf(name);
    }

    public static ServerStatus[] getServerStatuses() {
        return values();
    }

    private final String name;
    private final String display;

    ServerStatus(String name, String display) {
        this.name = name;
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }
}
