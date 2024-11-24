package de.bedrockcloud.cloudbridge.language;

import de.bedrockcloud.cloudbridge.util.GeneralSettings;
import dev.waterdog.waterdogpe.utils.config.YamlConfig;
import lombok.Getter;

import java.util.List;

@Getter
public enum Language {

    GERMAN("German", GeneralSettings.getCloudPath() + "storage/de_DE.yml", List.of("de_DE", "ger", "Deutsch")),
    ENGLISH("English", GeneralSettings.getCloudPath() + "storage/en_US.yml", List.of("en_US", "en", "Englisch"));

    public static Language current() {
        return getLanguage(GeneralSettings.getLanguage());
    }

    public static Language fallback() {
        return getLanguage("en");
    }

    public static Language getLanguage(String name) {
        for (Language language : values()) {
            if (language.getName().equals(name) || language.getAliases().contains(name)) return language;
        }
        return fallback();
    }

    private final String name;
    private final List<String> aliases;
    private final YamlConfig config;

    Language(String name, String filePath, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
        config = new YamlConfig(filePath);
    }

    public String translate(String key, String ...params) {
        String message = config.getString(key, "").replace("{PREFIX}", config.getString("inGame.prefix", ""));
        for (int i = 0; i < params.length; i++) message = message.replace("%" + i + "%", params[i]);
        return message;
    }

}
