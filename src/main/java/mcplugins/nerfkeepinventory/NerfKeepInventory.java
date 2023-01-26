package mcplugins.nerfkeepinventory;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class NerfKeepInventory extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new OnPlayerDeath(), this);
        Bukkit.getPluginManager().registerEvents(new onRespawn(), this);
        Bukkit.getLogger().info("[NerfKeepInventory] Plugin is up and running!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    //This only exist so Intellij will let me export as a jar file (I'm going back to eclipse)
    public static void main(String args[]) {

    }
}
