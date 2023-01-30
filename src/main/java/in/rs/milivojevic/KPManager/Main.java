package in.rs.milivojevic.KPManager;

import in.rs.milivojevic.KPManager.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import in.rs.milivojevic.KPManager.utils.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;


public final class Main extends JavaPlugin implements Listener {
    private ConfigManager configManager;
    private TabCustomizer tabCustomizer;
    private boolean updateAvailable;
    private String messagePlayer;
    private String updateVersion;
    private String currentVersion;
    private int resource;

    @Override
    public void onEnable() {
        getLogger().info("Starting!");
        getLogger().warning("This plugin is made by Kamey_");

        int pluginId = 17490;
        Metrics metrics = new Metrics(this, pluginId);

        configManager = new ConfigManager(this);
        configManager.loadConfig();

        JoinWelcomer joinWelcomer = new JoinWelcomer(this, configManager);
        getServer().getPluginManager().registerEvents(joinWelcomer, this);

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        tabCustomizer = new TabCustomizer(scoreboard, configManager, this);
        getServer().getPluginManager().registerEvents(tabCustomizer, this);
        getServer().getPluginManager().registerEvents(this, this);
        if (configManager.getString("resource") != null) {
            resource = Integer.parseInt(configManager.getString("resource"));
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
                if (configManager.getString("updateAvailable") != null) {
                    if (messagePlayer != null) {
                        getLogger().info(configManager.getString("updateAvailable").replace("{version}", updateVersion));
                    }
                } else {
                    getLogger().severe("Error couldn't read config value updateAvailable, please check your config.yml file");
                }
            }
        });

        if (configManager.getString("updateAvailablePlayer") != null) {
            messagePlayer = configManager.getString("updateAvailablePlayer").replace('&', ChatColor.COLOR_CHAR);
        } else {
            getLogger().severe("Error couldn't read config value updateAvailablePlayer, please check your config.yml file");
        }
        if (configManager.getString("currentVersion") != null) {
            currentVersion = configManager.getString("currentVersion").replace('&', ChatColor.COLOR_CHAR);
        } else {
            getLogger().severe("Error couldn't read config value currentVersion, please check your config.yml file");
        }

        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            getLogger().warning("Utilizing PlaceholderAPI");
            //Registering placeholder will be done here
        }
    }


    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (updateAvailable && (player.hasPermission("KPManager.update") || player.isOp())) {
            if (messagePlayer != null) {
                player.sendMessage(messagePlayer.replace("{version}", updateVersion));
                player.sendMessage(currentVersion.replace("{currentVersion}", this.getDescription().getVersion()));
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

