package in.rs.milivojevic.KPManager;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

public final class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("Starting!");
        getLogger().warning("This plugin is made by Kamey_");
        this.saveDefaultConfig();
        JoinWelcomer joinWelcomer = new JoinWelcomer(this);
        getServer().getPluginManager().registerEvents(joinWelcomer, this);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        TabCustomizer tabCustomizer = new TabCustomizer(scoreboard,this);
        getServer().getPluginManager().registerEvents(tabCustomizer, this);
    }
    @Override
    public void onDisable() {
        Bukkit.getServer().getPluginManager().disablePlugin(this);
    }
}
