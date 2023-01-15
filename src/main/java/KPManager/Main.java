package KPManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&bStarting!"));
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&bThis plugin is made by Kamey_"));
        Bukkit.getServer().getPluginManager().registerEvents(new JoinWelcomer(), this);
    }


    @Override
    public void onDisable() {
        Bukkit.getServer().getPluginManager().disablePlugin(this);
    }
}
