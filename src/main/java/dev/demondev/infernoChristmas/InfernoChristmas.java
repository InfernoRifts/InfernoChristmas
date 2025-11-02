package dev.demondev.infernoChristmas;

import dev.demondev.infernoChristmas.commands.PresentCommand;
import dev.demondev.infernoChristmas.listeners.MenuListener;
import dev.demondev.infernoChristmas.listeners.PresentListener;
import dev.demondev.infernoChristmas.menu.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class InfernoChristmas extends JavaPlugin {

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static InfernoChristmas plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;

        getCommand("present").setExecutor(new PresentCommand());

        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new PresentListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static InfernoChristmas getPlugin() {
        return plugin;
    }

    //Provide a player and return a menu system for that player
    //create one if they don't already have one
    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a playermenuutility "saved" for them

            //This player doesn't. Make one for them add add it to the hashmap
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }
}
