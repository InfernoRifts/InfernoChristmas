package dev.demondev.infernoChristmas.commands;

import dev.demondev.infernoChristmas.menu.menus.calendarGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class calendarCommand implements CommandExecutor {

    private final calendarGUI gui;


    public calendarCommand(calendarGUI gui) { this.gui = gui; }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        p.openInventory(gui.getGUI(p));
        return true;

    }
}
