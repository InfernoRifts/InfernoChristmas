package dev.demondev.infernoChristmas.utils;

import dev.demondev.infernoChristmas.InfernoChristmas;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PresentUtils  {

    public static ItemStack createPresent(List<ItemStack> items, String from, String to, String message) throws IOException {

        ByteArrayOutputStream bio = new ByteArrayOutputStream();
        BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(bio);

        outputStream.writeInt(items.size());

        for (ItemStack item : items) {
            try{
                outputStream.writeObject(item);
            }catch (IOException ex){
                System.out.println(ex);
            }
        }

        outputStream.flush();

        byte[] serialized = bio.toByteArray();
        String encodedString = Base64.getEncoder().encodeToString(serialized);

        ItemStack present = new ItemStack(Material.CHEST, 1);
        ItemMeta presentMeta = present.getItemMeta();
        presentMeta.setDisplayName("Present From: " + from );

        PersistentDataContainer container = presentMeta.getPersistentDataContainer();
        ArrayList<String> lore = new ArrayList<>();
        container.set(new NamespacedKey(InfernoChristmas.getPlugin(), "presents"), PersistentDataType.STRING, encodedString);
        if (to != null){
            container.set(new NamespacedKey(InfernoChristmas.getPlugin(), "presentRecipient"), PersistentDataType.STRING, to);
            lore.add(ColorUtils.translateColorCodes("&a&lTO: &c&l" + to));
            presentMeta.setLore(lore);
        }

        if (message != null){
            container.set(new NamespacedKey(InfernoChristmas.getPlugin(), "presentMessage"), PersistentDataType.STRING, message);
        }

        present.setItemMeta(presentMeta);

        bio.close();
        outputStream.close();

        return present;
    }

    public static ItemStack[] getItemsFromPresent(String encodedString) throws IOException, ClassNotFoundException {

        byte[] rawData = Base64.getDecoder().decode(encodedString);
        ByteArrayInputStream bio = new ByteArrayInputStream(rawData);
        BukkitObjectInputStream inputStream = new BukkitObjectInputStream(bio);

        int size = inputStream.readInt();

        ItemStack[] items = new ItemStack[size];
        for(int i = 0; i < size; i++){
            items[i] = (ItemStack) inputStream.readObject();
        }

        inputStream.close();

        return items;
    }

}
