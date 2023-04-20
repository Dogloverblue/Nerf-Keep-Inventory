package mcplugins.nerfkeepinventory;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;

public class OnPlayerDeath implements Listener {
   private ArrayList<ItemStack> savedItems;
   private HashMap<Integer, ItemStack> saveItems;
   public static HashMap<Player, ArrayList<ItemStack>> lostItems;
    private ArrayList<ItemStack> lostItemsList;
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getPlayer().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY) == true) {
            Player plr = event.getPlayer();
            savedItems = new ArrayList<ItemStack>();
            saveItems = new HashMap<Integer, ItemStack>();
            FileConfiguration config = NerfKeepInventory.getPlugin(NerfKeepInventory.class).getConfig();
            lostItemsList = new ArrayList<ItemStack>();
            if (lostItems == null)
                lostItems = new HashMap<Player, ArrayList<ItemStack>>();
            int iterator = 0;
            for (ItemStack i : plr.getInventory().getContents()) {
                //checking if the items in a hotbar slot
                if (i != null && i.getType() != null && checkIfAnyColorOfShulkerBox(i)) {
                    Bukkit.getLogger().warning("Shulkers: " + config.getBoolean("Shulkerboxes"));
                    if (config.getBoolean("Keep_Shulkerboxes")) {
                        saveItems.put(iterator, i);
                    } else {
                        lostItemsList.add(i);
                        plr.getWorld().dropItem(plr.getLocation(), i);
                    }
                } else if(plr.getInventory().getItemInOffHand().equals(i)) {
                    Bukkit.getLogger().warning("Offhand: " + config.getBoolean("Keep_Offhand"));
                    if (config.getBoolean("Keep_Offhand")) {
                        saveItems.put(iterator, i);
                    } else {
                        lostItemsList.add(i);
                        plr.getWorld().dropItem(plr.getLocation(), i);
                        continue;
                    }
                }
                else if (i != null && i.getItemMeta().hasDisplayName()) {
                    Bukkit.getLogger().warning("Named: " + config.getBoolean("Keep_Named_Items"));
                    if (config.getBoolean("Keep_Named_Items")) {
                        saveItems.put(iterator, i);
                    } else {
                        lostItemsList.add(i);
                        plr.getWorld().dropItem(plr.getLocation(), i);
                    }
                } else if (iterator < 9) {
                    if (i != null) {
                        Bukkit.getLogger().warning("Hotbar: " + config.getBoolean("Keep_Hotbar"));
                        if (config.getBoolean("Keep_Hotbar")) {
                            saveItems.put(iterator, i);
                        } else {
                            lostItemsList.add(i);
                            plr.getWorld().dropItem(plr.getLocation(), i);
                        }
                    }
                } else if (iterator > 35) {
                    if (i != null) {

                            Bukkit.getLogger().warning("Armour: " + config.getBoolean("Keep_Armour"));
                            if (config.getBoolean("Keep_Armour")) {

                                saveItems.put(iterator, i);
                            } else {
                                lostItemsList.add(i);
                                plr.getWorld().dropItem(plr.getLocation(), i);

                        }
                    }
                } else {
                    if (i != null && i.getType() != null) {

                        lostItemsList.add(i);
                        plr.getWorld().dropItem(plr.getLocation(), i);
                    }
                }
                iterator++;
            }
            lostItems.put(plr, lostItemsList);
            plr.getInventory().clear();
            double levelsToKeep = plr.getLevel();
            levelsToKeep *= (1 + (config.getDouble("EXP_Keep_Percentage") / 100));
            Bukkit.getLogger().warning("EXPLEVL: " + config.getDouble("EXP_Keep_Percentage"));
            plr.setLevel((int) levelsToKeep - plr.getLevel());
            for (Integer slot : saveItems.keySet()) {
                plr.getInventory().setItem(slot, saveItems.get(slot));
            }
            //plr.getInventory().setContents(convertToArray(savedItems));
        }
    }

private ItemStack[] convertToArray(ArrayList<ItemStack> input) {
        ItemStack[] items = new ItemStack[input.size()];
        for (int i = 0; i < input.size(); i++) {
            items[i] = input.get(i);
    }
        return items;
}
private boolean checkIfAnyColorOfShulkerBox(ItemStack item) {
        for (Material d : Tag.SHULKER_BOXES.getValues()) {
            if (d.equals(item.getType())) {
                return true;
            }
        }
        return false;
}

}
