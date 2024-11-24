package de.bedrockcloud.cloudbridge.network.packet.impl.normal;

import de.bedrockcloud.cloudbridge.api.CloudAPI;
import de.bedrockcloud.cloudbridge.api.registry.Registry;
import de.bedrockcloud.cloudbridge.api.template.Template;
import de.bedrockcloud.cloudbridge.network.packet.CloudPacket;
import de.bedrockcloud.cloudbridge.network.packet.utils.PacketData;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TemplateSyncPacket extends CloudPacket {

    private Template template;
    private boolean removal;

    public TemplateSyncPacket(Template template, boolean removal) {
        this.template = template;
        this.removal = removal;
    }

    @Override
    protected void encodePayload(PacketData packetData) {
        super.encodePayload(packetData);
        packetData.writeTemplate(template);
        packetData.write(removal);
    }

    @Override
    protected void decodePayload(PacketData packetData) {
        super.decodePayload(packetData);
        template = packetData.readTemplate();
        removal = packetData.readBool();
    }

    @Override
    public void handle() {
        if (CloudAPI.getInstance().getTemplateByName(template.getName()) == null) {
            if (!removal) Registry.registerTemplate(template);
        } else {
            if (removal) {
                Registry.unregisterTemplate(template.getName());
            } else Registry.updateTemplate(template.getName(), template.toArray());
        }
    }

}
