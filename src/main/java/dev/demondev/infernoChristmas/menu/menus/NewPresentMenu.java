package dev.demondev.infernoChristmas.menu.menus;

import dev.demondev.infernoChristmas.menu.Menu;
import dev.demondev.infernoChristmas.menu.PlayerMenuUtility;
import dev.demondev.infernoChristmas.utils.ColorUtils;
import dev.demondev.infernoChristmas.utils.PresentUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.plaf.ColorUIResource;
import java.io.IOException;
import java.util.ArrayList;

public class NewPresentMenu extends Menu {
    public NewPresentMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "§aNew Present";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player sender = playerMenuUtility.getOwner();
        int slot = e.getSlot();

        // Cancel clicks only for:
        // - Bottom row (27–53) → filler slots
        // - Create Present button (slot 40)
        if ((slot >= 27 && slot <= 53) || slot == 40) {
            e.setCancelled(true);
        }

        // Only proceed if the "Create Present" button is clicked
        if (slot != 40) return;

        // Get and sanitize recipient name
        String recipientNameStr = playerMenuUtility.getRecipient();
        if (recipientNameStr == null || recipientNameStr.trim().isEmpty()) {
            sender.sendMessage("§cYou must specify a recipient.");
            return;
        }
        recipientNameStr = recipientNameStr.trim();

        // Find recipient case-insensitively among online players
        Player recipient = null;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getName().equalsIgnoreCase(recipientNameStr)) {
                recipient = onlinePlayer;
                break;
            }
        }

        if (recipient == null) {
            sender.sendMessage("§cRecipient is offline or does not exist.");
            return;
        }

        // Check if recipient's inventory has space
        if (recipient.getInventory().firstEmpty() == -1) {
            sender.sendMessage("§cRecipient's inventory is full. Cannot deliver present.");
            return;
        }

        // Gather items from the present menu (top 27 slots)
        ArrayList<ItemStack> items = new ArrayList<>();
        int startSlot = (playerMenuUtility.getPresentMessage() != null || playerMenuUtility.getRecipient() != null) ? 1 : 0;
        for (int i = startSlot; i < 27; i++) {
            ItemStack item = e.getInventory().getItem(i);
            if (item != null) items.add(item);
        }

        try {
            // Create the present
            ItemStack present = PresentUtils.createPresent(
                    items,
                    sender.getDisplayName(),
                    recipient.getName(),
                    playerMenuUtility.getPresentMessage()
            );

            sender.closeInventory();
            recipient.getInventory().addItem(present);
            recipient.sendMessage("§cYou have received a present!");
            sender.sendMessage("§aPresent delivered successfully!");

        } catch (IOException ex) {
            sender.sendMessage("§cUnable to create present. Sorry.");
            ex.printStackTrace();
        }
    }




    @Override
    public void setMenuItems() {


        for (int i = 27; i < 54; i++){
            inventory.setItem(i, FILLER_GLASS);
        }


        ItemStack info = makeItem(Material.PAPER, ColorUtils.translateColorCodes("&e&lInfo"),
                ColorUtils.translateColorCodes("&aPut the items you"),
                ColorUtils.translateColorCodes("&awant in the present"),
                ColorUtils.translateColorCodes("&ain the box above."));


        ItemStack create = makeItem(Material.BELL,
                ColorUtils.translateColorCodes("&c&lC&a&lr&c&le&a&la&c&lt&a&le &c&lP&f&lr&c&le&f&ls&c&le&f&ln&c&lt"),
                ColorUtils.translateColorCodes("&aClick this item and the &c&lElves&a..."),
                ColorUtils.translateColorCodes("&awill package your &f&l&nPRESENT"));

        if (playerMenuUtility.getPresentMessage() != null || playerMenuUtility.getRecipient() != null){

            ItemStack tag = new ItemStack(Material.NAME_TAG, 1);
            ItemMeta tagMeta = tag.getItemMeta();

            if(playerMenuUtility.getRecipient() != null){

                tagMeta.setDisplayName(ColorUtils.translateColorCodes("&e&lTo: &f" + playerMenuUtility.getRecipient()));

            }

            ArrayList<String> messageLore = new ArrayList<>();
            if (playerMenuUtility.getPresentMessage() != null) {
                messageLore.add(ColorUtils.translateColorCodes("&#08d30e" + playerMenuUtility.getPresentMessage()));
            }else{
                messageLore.add("And a happy new year!!!");
            }

            tagMeta.setLore(messageLore);
            tag.setItemMeta(tagMeta);

            inventory.addItem(tag);
        }
        inventory.setItem(39, info);
        inventory.setItem(40, create);
    }
}
