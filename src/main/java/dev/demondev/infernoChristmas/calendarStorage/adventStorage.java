package dev.demondev.infernoChristmas.calendarStorage;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class adventStorage {


    private final Plugin plugin;
    private final File file;
    private final YamlConfiguration yaml;


    public adventStorage(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "data.yml");
        if (!file.exists()) plugin.saveResource("data.yml", false);
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }


    public boolean hasClaimed(UUID uuid, int day) {
        return yaml.getBoolean("players." + uuid + "." + day, false);
    }


    public void setClaimed(UUID uuid, int day) {
        yaml.set("players." + uuid + "." + day, true);
        save();
    }


    public void save() {
        try { yaml.save(file); } catch (IOException ignored) {}
    }
}
