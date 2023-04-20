package mcplugins.nerfkeepinventory;

import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class onRespawn implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (event.getPlayer().getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY) == true) {
            boolean ThalynCheck = false;
            int armourCount = 0;
            for (ItemStack i : event.getPlayer().getInventory().getContents()) {
                if (i == null)
                    continue;
                final String typeNameString = i.getType().name();
                if (typeNameString.endsWith("_HELMET")
                        || typeNameString.endsWith("_CHESTPLATE")
                        || typeNameString.endsWith("_LEGGINGS")
                        || typeNameString.endsWith("_BOOTS")
                        || typeNameString.endsWith("ELYTRA")) {
                    armourCount++;
                    ThalynCheck = true;
                }
            }
            if (ThalynCheck && (event.getPlayer().getName().equals("ariah_them") || event.getPlayer().getName().equals("Coyote_Fox"))) {
                event.getPlayer().sendMessage("§dI saved you §b" + armourCount + "§d clicks! §c(You're welcome)");
            }
            StringBuilder respawnMessage = new StringBuilder("§aWhen you died, you lost ");
            if (OnPlayerDeath.lostItems.get(event.getPlayer()) == null || OnPlayerDeath.lostItems.get(event.getPlayer()).size() < 1) {
                event.getPlayer().sendMessage("§aHooray! §eYou lost §ano §eitems on death!");
                return;
            }
            for (ItemStack i : OnPlayerDeath.lostItems.get(event.getPlayer())) {

                respawnMessage.append("§e" + i.getAmount() +
                        " §2" + i.getType().toString().replace("_", " ").toLowerCase() +
                        "§a, ");


            }
            //removing the extra ", " at the end and replacing it with an explanation point
            event.getPlayer().sendMessage(respawnMessage.toString().substring(0, respawnMessage.toString().length() - 2) + "§a!");
            OnPlayerDeath.lostItems.remove(event.getPlayer());
        }
    }
}
