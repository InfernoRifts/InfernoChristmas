package dev.demondev.infernoChristmas.menu.menus;

import dev.demondev.infernoChristmas.InfernoChristmas;
import dev.demondev.infernoChristmas.calendarStorage.adventStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class calendarGUI implements Listener {

    private final InfernoChristmas plugin;
    private final adventStorage data;
    private int simulatedDay = -1;


    public calendarGUI(InfernoChristmas plugin, adventStorage data) {
        this.plugin = plugin;
        this.data = data;
    }


    public void setSimulatedDay(int day) { this.simulatedDay = day; }
    public void clearSimulatedDay() { this.simulatedDay = -1; }

    public int getCurrentDay() {
        if (simulatedDay != -1) return simulatedDay;
        ZoneId zone = ZoneId.of(plugin.getConfig().getString("settings.timezone"));
        LocalDate now = LocalDate.now(zone);
        return now.getMonth() == Month.DECEMBER ? now.getDayOfMonth() : -1;
    }

    public Inventory getGUI(Player p) {
        FileConfiguration cfg = plugin.getConfig();
        int size = cfg.getInt("calendar_gui.size");
        Inventory inv = Bukkit.createInventory(null, size, cfg.getString("calendar_gui.title"));

        // Filler
        Material fillerMat = Material.matchMaterial(cfg.getString("calendar_gui.filler_item.material"));
        ItemStack filler = new ItemStack(fillerMat);
        ItemMeta fm = filler.getItemMeta();
        fm.setDisplayName(cfg.getString("calendar_gui.filler_item.name"));
        filler.setItemMeta(fm);
        for (int i = 0; i < size; i++) inv.setItem(i, filler);

        int today = getCurrentDay();
        if (today == -1) today = 1;

        Map<Integer, Integer> slotToDay = getTreeMapping();

        for (Map.Entry<Integer, Integer> entry : slotToDay.entrySet()) {
            int slot = entry.getKey();
            int day = entry.getValue();
            ItemStack item = buildDayItem(p, day, slot);
            inv.setItem(slot, item);
        }

        return inv;
    }

    private Map<Integer, Integer> getTreeMapping() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(4,1); map.put(12,2); map.put(13,3);
        map.put(14,4); map.put(20,5); map.put(21,6);
        map.put(22,7); map.put(23,8); map.put(24,9); map.put(28,10);
        map.put(29,11); map.put(30,12); map.put(31,13); map.put(32,14); map.put(33,15);
        map.put(34,16); map.put(36,17); map.put(37,18); map.put(38,19); map.put(39,20); map.put(40,21);
        map.put(41,22); map.put(42,23); map.put(43,24); map.put(44,25);
        return map;
    }

    private ItemStack buildDayItem(Player p, int day, int slot) {
        FileConfiguration cfg = plugin.getConfig();
        String path = "calendar_gui.days." + day;
        Material mat;
        List<String> lore = new ArrayList<>(cfg.getStringList(path + ".lore"));
        String displayName = cfg.getString(path + ".name");

        int today = getCurrentDay();

        if (data.hasClaimed(p.getUniqueId(), day)) {
            mat = Material.matchMaterial(cfg.getString("calendar_gui.states.claimed.material"));
            displayName = cfg.getString("calendar_gui.states.claimed.name").replace("%day%", String.valueOf(day));
            lore = cfg.getStringList("calendar_gui.states.claimed.lore");

        } else if (day < today) {
            mat = Material.matchMaterial(cfg.getString("calendar_gui.states.missed.material"));;
            displayName = cfg.getString("calendar_gui.states.missed.name").replace("%day%", String.valueOf(day));
            lore = cfg.getStringList("calendar_gui.states.missed.lore");

        } else if (day == today) {
            mat = Material.matchMaterial(cfg.getString(path + ".material"));

        } else {
            mat = Material.matchMaterial(cfg.getString("calendar_gui.states.locked.material"));
            displayName = cfg.getString("calendar_gui.states.locked.name").replace("%day%", String.valueOf(day));
            lore = cfg.getStringList("calendar_gui.states.locked.lore");
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        if (!e.getView().getTitle().equals(plugin.getConfig().getString("calendar_gui.title"))) return;

        e.setCancelled(true); // prevent taking items

        int slot = e.getSlot();
        Map<Integer, Integer> slotToDay = getTreeMapping();
        if (!slotToDay.containsKey(slot)) return; // clicked a filler

        int day = slotToDay.get(slot);
        int today = getCurrentDay();

        if (data.hasClaimed(p.getUniqueId(), day)) {
            p.closeInventory();
            p.sendMessage(plugin.getConfig().getString("calendar_messages.already_claimed"));
            return;
        }

        if (day < today) {
            p.closeInventory();
            p.sendMessage(plugin.getConfig().getString("calendar_messages.missed_day"));
            return;
        }

        if (day > today) {
            p.closeInventory();
            p.sendMessage(plugin.getConfig().getString("calendar_messages.cannot_claim"));
            return;
        }

        // Claim day
        data.setClaimed(p.getUniqueId(), day);
        p.closeInventory();
        p.sendMessage(plugin.getConfig().getString("calendar_messages.claimed_success").replace("%day%", String.valueOf(day)));

        // Execute rewards
        List<String> rewards = plugin.getConfig().getStringList("rewards." + day);
        for (String cmd : rewards) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", p.getName()));
        }
    }

}
