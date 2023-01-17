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
    private String messagePlayer;
    private String updateVersion;
    private int resource;
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
        getServer().getPluginManager().registerEvents(this, this);
        if (getConfig().getString("resource") != null) {
            resource = Integer.parseInt(getConfig().getString("resource"));
        }else{
            resource = Integer.parseInt("107412");
        }
            new UpdateChecker(this, resource).getVersion(version -> {
                if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                    updateAvailable = true;
                    updateVersion = version;
                } else {
                    updateAvailable = false;
                    updateVersion = null;
                }
                if (updateAvailable) {
                    if (getConfig().getString("updateAvailable") != null) {
                        if (messagePlayer != null) {
                            getLogger().info(getConfig().getString("updateAvailable").replace("{version}", updateVersion));
                        }
                    } else {
                        getLogger().severe("Error couldn't read config value updateAvailable, please check your config.yml file");
                    }
                }
            });

            if (getConfig().getString("updateAvailablePlayer") != null) {
                messagePlayer = getConfig().getString("updateAvailablePlayer").replace('&', ChatColor.COLOR_CHAR);
            } else {
                getLogger().severe("Error couldn't read config value updateAvailablePlayer, please check your config.yml file");
            }
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (updateAvailable && (player.hasPermission("KPManager.update") || player.isOp())) {
            if (messagePlayer != null) {
                player.sendMessage(messagePlayer.replace("{version}", updateVersion));
            } else {
                getLogger().severe("Error couldn't read config value updateAvailablePlayer, please check your config.yml file");
            }
        }
    }


    @Override
    public void onDisable() {
        setEnabled(false);
    }
}

