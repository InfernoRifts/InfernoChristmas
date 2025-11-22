package dev.demondev.infernoChristmas;

import dev.demondev.infernoChristmas.calendarStorage.adventStorage;
import dev.demondev.infernoChristmas.commands.PresentCommand;
import dev.demondev.infernoChristmas.commands.calendarCommand;
import dev.demondev.infernoChristmas.commands.simulateCommand;
import dev.demondev.infernoChristmas.listeners.MenuListener;
import dev.demondev.infernoChristmas.listeners.PresentListener;
import dev.demondev.infernoChristmas.menu.PlayerMenuUtility;
import dev.demondev.infernoChristmas.menu.menus.calendarGUI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class InfernoChristmas extends JavaPlugin {

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static InfernoChristmas plugin;
    private adventStorage data;
    private calendarGUI gui;


    @Override
    public void onEnable() {
        plugin = this;

        data = new adventStorage(this);
        gui = new calendarGUI(this, data);


        getCommand("present").setExecutor(new PresentCommand());
        getCommand("advent").setExecutor(new calendarCommand(gui));
        getCommand("adventsimulate").setExecutor(new simulateCommand(gui, this));

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new PresentListener(), this);
        getServer().getPluginManager().registerEvents(gui, this);
    }


    @Override
    public void onDisable() {
    }


    public static InfernoChristmas getPlugin() {
        return plugin;
    }



    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!playerMenuUtilityMap.containsKey(p)) {
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);
            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p);
        }
    }
}
