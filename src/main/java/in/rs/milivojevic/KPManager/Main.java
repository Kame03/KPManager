package in.rs.milivojevic.KPManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("Starting!");
        getLogger().warning("This plugin is made by Kamey_");
        this.saveDefaultConfig();
        JoinWelcomer joinWelcomer = new JoinWelcomer(this);
        getServer().getPluginManager().registerEvents(joinWelcomer, this);
    }




    @Override
    public void onDisable() {
        Bukkit.getServer().getPluginManager().disablePlugin(this);
    }
}
