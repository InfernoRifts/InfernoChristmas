package dev.demondev.infernoChristmas.menu.menus;

import dev.demondev.infernoChristmas.menu.Menu;
import dev.demondev.infernoChristmas.menu.PlayerMenuUtility;
import dev.demondev.infernoChristmas.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
