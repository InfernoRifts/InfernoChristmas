package dev.demondev.infernoChristmas.listeners;

import dev.demondev.infernoChristmas.InfernoChristmas;
import dev.demondev.infernoChristmas.utils.ColorUtils;
import dev.demondev.infernoChristmas.utils.PresentUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.TileState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class PresentListener implements Listener {

    @EventHandler
    public void onPresentPlace(BlockPlaceEvent e) {

        ItemStack itemPlaced = e.getItemInHand();


        if (itemPlaced.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(InfernoChristmas.getPlugin(), "presents"), PersistentDataType.STRING)) {
            
            Chest c = (Chest) e.getBlockPlaced().getState();
            try {

                ItemStack[] items = PresentUtils.getItemsFromPresent(itemPlaced.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(InfernoChristmas.getPlugin(), "presents"), PersistentDataType.STRING));
                c.getSnapshotInventory().setContents(items);

                PersistentDataContainer container = c.getPersistentDataContainer();
                container.set(new NamespacedKey(InfernoChristmas.getPlugin(), "present"), PersistentDataType.INTEGER, 0);

                if (itemPlaced.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(InfernoChristmas.getPlugin(), "presentMessage"), PersistentDataType.STRING)) {
                    container.set(new NamespacedKey(InfernoChristmas.getPlugin(), "presentMessage"), PersistentDataType.STRING, itemPlaced.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(InfernoChristmas.getPlugin(), "presentMessage"), PersistentDataType.STRING));
                }

                if (itemPlaced.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(InfernoChristmas.getPlugin(), "presentRecipient"), PersistentDataType.STRING)) {
                    container.set(new NamespacedKey(InfernoChristmas.getPlugin(), "presentRecipient"), PersistentDataType.STRING, itemPlaced.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(InfernoChristmas.getPlugin(), "presentRecipient"), PersistentDataType.STRING));
                }

                c.update();

            } catch (IOException | ClassNotFoundException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    @EventHandler
    public void onPresentOpen(InventoryOpenEvent e){

        if (e.getInventory().getHolder() instanceof Chest || e.getInventory().getHolder() instanceof DoubleChest) {

            Player p = (Player) e.getPlayer();

            TileState state = (TileState) e.getInventory().getHolder();

            PersistentDataContainer container = state.getPersistentDataContainer();

            if (container.has(new NamespacedKey(InfernoChristmas.getPlugin(), "present"), PersistentDataType.INTEGER)){

                if (container.has(new NamespacedKey(InfernoChristmas.getPlugin(), "presentRecipient"), PersistentDataType.STRING)){

                    if(container.get(new NamespacedKey(InfernoChristmas.getPlugin(), "presentRecipient"), PersistentDataType.STRING).equalsIgnoreCase(p.getDisplayName())){

                        e.setCancelled(true);

                        e.getPlayer().sendMessage(ColorUtils.translateColorCodes("&4You are not the recipient of this present! Keep trying and you will end up on the naughty list."));

                        return;

                    }
                }

                  Chest c = (Chest) e.getInventory().getHolder();
                   Block chest = state.getBlock();
                  ItemStack[] items = c.getInventory().getContents();

                  e.setCancelled(true);

                ArmorStand merryChristmas = (ArmorStand) chest.getLocation().getWorld().spawnEntity(chest.getLocation().add(0.5, -0.5, 0.5), EntityType.ARMOR_STAND);
                merryChristmas.setGravity(false);
                merryChristmas.setCanPickupItems(false);
                merryChristmas.setCustomName(ColorUtils.translateColorCodes("&c&l&k||&c&lM&a&le&c&lr&a&lr&c&ly &a&lC&c&lh&a&lr&a&li&c&ls&a&lt&c&lm&a&la&c&ls&b&l!!!&c&l&k||"));
                merryChristmas.setCustomNameVisible(true);
                merryChristmas.setVisible(false);

                ArmorStand message = (ArmorStand) chest.getLocation().getWorld().spawnEntity(chest.getLocation().add(0.5, -1, 0.5), EntityType.ARMOR_STAND);
                message.setGravity(false);
                message.setCanPickupItems(false);
                  if (container.has(new NamespacedKey(InfernoChristmas.getPlugin(), "presentMessage"), PersistentDataType.STRING)) {
                  message.setCustomName(ColorUtils.translateColorCodes("\" &f&o" + container.get(new NamespacedKey(InfernoChristmas.getPlugin(), "presentMessage"), PersistentDataType.STRING) + "&r\""));
                  message.setCustomNameVisible(true);
                      } else {
                       message.setCustomName(" ");
                       message.setCustomNameVisible(false);
                        }
                        message.setVisible(false);

                ArmorStand tree = (ArmorStand) chest.getLocation().getWorld().spawnEntity(chest.getLocation().add(0.5, -0.5, 0.5), EntityType.ARMOR_STAND);

                 tree.setGravity(false);
                 tree.setCanPickupItems(false);
                 tree.getEquipment().setHelmet(new ItemStack(Material.SPRUCE_SAPLING, 1));
                 tree.setVisible(false);

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        if((tree.getLocation().getY() - chest.getLocation().getY()) >= 3.5) {

                            tree.remove();

                             p.setInvulnerable(true);

                            final Firework fw = (Firework) chest.getWorld().spawnEntity(tree.getLocation().add(0.5, 0, 0.5), EntityType.FIREWORK_ROCKET);
                            FireworkMeta fm = fw.getFireworkMeta();
                            fm.addEffect(FireworkEffect.builder()
                                    .flicker(true)
                                    .trail(true)
                                    .with(FireworkEffect.Type.BURST)
                                    .withColor(Color.RED, Color.GREEN, Color.LIME)
                                    .build());
                                    fm.setPower(0);
                                    fw.setFireworkMeta(fm);

                                    fw.detonate();

                                    tree.getWorld().playSound(tree.getLocation(), "minecraft:inferno.caroloftheblocks", 1f, 1f);
                                    
                            p.setInvulnerable(false);
                            Location location =   chest.getLocation().add(0.5, 0, 0.5);
                            for (ItemStack item : items){
                                if(item != null){
                                   chest.getWorld().dropItem(location.add(0, 1,0), item);
                                }
                            }

                            this.cancel();
                        } else {
                            tree.teleport(tree.getLocation().add(0, 0.25, 0));
                            tree.setRotation(tree.getLocation().getYaw() + 25f, tree.getLocation().getPitch() + 25f);
                        }
                    }
                }.runTaskTimer(InfernoChristmas.getPlugin(), 0, 5);
            }
        }
    }
}
