package in.rs.milivojevic.KPManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

public final class Main extends JavaPlugin implements Listener {
    private boolean updateAvailable;
    @Override
    public void onEnable() {
        getLogger().info("Starting!");
        getLogger().warning("This plugin is made by Kamey_");
        this.saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        JoinWelcomer joinWelcomer = new JoinWelcomer(this);
        getServer().getPluginManager().registerEvents(joinWelcomer, this);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        TabCustomizer tabCustomizer = new TabCustomizer(scoreboard,this);
        getServer().getPluginManager().registerEvents(tabCustomizer, this);

        new UpdateChecker(this, 107412).getVersion(version -> {
            updateAvailable = !(this.getDescription().getVersion().equals(version));
            if(updateAvailable) {
                if (getConfig().getString("updateAvailable") !=null) {
                    getLogger().info(getConfig().getString("updateAvailable"));
                } else {
                    getLogger().severe("Error couldn't read config value updateAvailable, please check your config.yml file");
                }
            }
        });
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(updateAvailable && (player.hasPermission("KPManager.update") || player.isOp())) {
            player.sendMessage(getConfig().getString("updateAvailablePlayer").replace('&',ChatColor.COLOR_CHAR));
        }
    }

    @Override
    public void onDisable() {
        setEnabled(false);
    }
}

