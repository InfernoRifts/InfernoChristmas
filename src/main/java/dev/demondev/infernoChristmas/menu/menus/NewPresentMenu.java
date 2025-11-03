package dev.demondev.infernoChristmas.menu.menus;

import dev.demondev.infernoChristmas.menu.Menu;
import dev.demondev.infernoChristmas.menu.PlayerMenuUtility;
import dev.demondev.infernoChristmas.utils.ColorUtils;
import dev.demondev.infernoChristmas.utils.PresentUtils;
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
        return "New Present";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = playerMenuUtility.getOwner();

        if(playerMenuUtility.getPresentMessage() != null || playerMenuUtility.getRecipient() != null){
            if(e.getSlot() == 0){
                e.setCancelled(true);
            }
        }

        if (e.getSlot() == 40){

            e.setCancelled(true);

            ArrayList<ItemStack> items = new ArrayList<>();
            ItemStack present;
            try{

                if (playerMenuUtility.getPresentMessage() != null || playerMenuUtility.getRecipient() != null){
                    for(int i = 1; i < 27; i++){
                        if (e.getInventory().getItem(i) != null){
                            items.add(e.getInventory().getItem(i));
                      }
                    }
                }else {
                    for (int i = 0; i < 27; i++) {
                        if (e.getInventory().getItem(i) != null) {
                            items.add(e.getInventory().getItem(i));
                        }
                    }
                }

                present = PresentUtils.createPresent(items, playerMenuUtility.getOwner().getDisplayName(), playerMenuUtility.getRecipient(), playerMenuUtility.getPresentMessage());
                p.closeInventory();
                p.getInventory().addItem(present);
                p.sendMessage("You have been given the present");

            }catch (IOException ex){
                p.sendMessage("Unable to create present. Sorry");
            }
        } else if(e.getSlot() > 27 && e.getSlot() < 54){
            e.setCancelled(true);
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
