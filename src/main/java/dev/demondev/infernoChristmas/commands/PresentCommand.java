package dev.demondev.infernoChristmas.commands;

import dev.demondev.infernoChristmas.InfernoChristmas;
import dev.demondev.infernoChristmas.menu.PlayerMenuUtility;
import dev.demondev.infernoChristmas.menu.menus.NewPresentMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PresentCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player p){

            // /present <to> <message>

            if (args.length > 0){

                String to = args[0];
                Player recipient = Bukkit.getPlayer(to);

                if(recipient == null){
                    p.sendMessage("The player you specified does not exist. Command /present <to> <message>");
                }else{

                    PlayerMenuUtility playerMenuUtility = InfernoChristmas.getPlayerMenuUtility(p);
                    if (args.length == 1){
                        playerMenuUtility.setRecipient(String.valueOf(recipient.getPlayer().getName()));
                    }else if (args.length > 1){
                        playerMenuUtility.setRecipient(String.valueOf(recipient.getPlayer().getName()));

                        StringBuilder message = new StringBuilder();
                        for (int i = 1; i < args.length; i++){
                            message.append(args[i]).append(" ");
                        }
                        playerMenuUtility.setPresentMessage(message.toString());
                    }

                    new NewPresentMenu(playerMenuUtility).open();
                }
            } else {
                new NewPresentMenu(InfernoChristmas.getPlayerMenuUtility(p)).open();
            }

        }


        return true;
    }
}

