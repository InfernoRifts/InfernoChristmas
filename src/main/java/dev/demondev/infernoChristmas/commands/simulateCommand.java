package dev.demondev.infernoChristmas.commands;

import dev.demondev.infernoChristmas.InfernoChristmas;
import dev.demondev.infernoChristmas.menu.menus.calendarGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class simulateCommand implements CommandExecutor {


    private final calendarGUI gui;
    private final InfernoChristmas plugin;


    public simulateCommand(calendarGUI gui, InfernoChristmas plugin) {
        this.gui = gui;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("adventcalendar.simulate")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }


        if (args.length == 0) {
            sender.sendMessage("Usage: /adventsimulate <day|off>");
            return true;
        }


        if (args[0].equalsIgnoreCase("off")) {
            gui.clearSimulatedDay();
            sender.sendMessage("Simulation disabled. Advent calendar uses real time now.");
            return true;
        }


        try {
            int day = Integer.parseInt(args[0]);
            int total = plugin.getConfig().getInt("settings.total_days");
            if (day < 1 || day > total) {
                sender.sendMessage("Day must be between 1 and " + total + ".");
                return true;
            }


            gui.setSimulatedDay(day);
            sender.sendMessage("Simulation enabled: now simulating December " + day + ".");


            if (sender instanceof Player) {
                Player p = (Player) sender;
                p.openInventory(gui.getGUI(p));
            }


        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid day number. Use a number or 'off'.");
        }


        return true;
    }
}
