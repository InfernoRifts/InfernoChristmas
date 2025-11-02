package dev.demondev.infernoChristmas.menu.menus;

import dev.demondev.infernoChristmas.menu.Menu;
import dev.demondev.infernoChristmas.menu.PlayerMenuUtility;
import dev.demondev.infernoChristmas.utils.ColorUtils;
import dev.demondev.infernoChristmas.utils.PresentUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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

        if (e.getSlot() == 40){

            e.setCancelled(true);

            ArrayList<ItemStack> items = new ArrayList<>();
            ItemStack present;
            try{
                for(int i = 0; i < 27; i++){
                    if (e.getInventory().getItem(i) != null){
                        items.add(e.getInventory().getItem(i));
                    }
                }

                present = PresentUtils.createPresent(items, playerMenuUtility.getOwner().getDisplayName(), playerMenuUtility.getRecipient(), playerMenuUtility.getPresentMessage());
                p.closeInventory();
                p.getInventory().addItem(present);
                p.sendMessage("You have been given the present");

            }catch (IOException ex){
                p.sendMessage("Unable to create present. Sorry");
            }
        } else if (e.getSlot() > 27 && e.getSlot() < 54){
            e.setCancelled(true);
        }

    }

    @Override
    public void setMenuItems() {


        for (int i = 27; i < 54; i++){
            inventory.setItem(i, FILLER_GLASS);
        }

        ItemStack create = makeItem(Material.BELL, ColorUtils.translateColorCodes("Create Present"),
                ColorUtils.translateColorCodes("Click this item and the Elves..."),
                ColorUtils.translateColorCodes("will package your present"));

        inventory.setItem(40, create);
    }
}
