package mcplugins.nerfkeepinventory;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
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
   public static HashMap<Player, ArrayList<ItemStack>> lostItems;
    private ArrayList<ItemStack> lostItemsList;
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player plr = event.getPlayer();
        savedItems = new ArrayList<ItemStack>();
        lostItemsList = new ArrayList<ItemStack>();
        if (lostItems == null)
            lostItems = new HashMap<Player, ArrayList<ItemStack>>();
        int iterator = 0;
        for (ItemStack i : plr.getInventory().getContents()) {
            //checking if the items in a hotbar slot
            if (iterator < 9 || iterator > 35) {
                if (i != null) {
                    savedItems.add(i);
                }
            } else if (i != null && i.getType() != null && checkIfAnyColorOfShulkerBox(i)) {


                savedItems.add(i);
            } else if (i != null && i.getItemMeta().hasDisplayName()) {
                savedItems.add(i);
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

        plr.getInventory().setContents(convertToArray(savedItems));
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
