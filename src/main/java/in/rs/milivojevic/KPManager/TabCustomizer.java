package in.rs.milivojevic.KPManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import in.rs.milivojevic.KPManager.utils.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scheduler.BukkitRunnable;
import me.clip.placeholderapi.PlaceholderAPI;

public class TabCustomizer implements Listener {
    private final Scoreboard scoreboard;
    private final ConfigManager config;
    private final Main plugin;
    private String tabHeader;
    private String tabFooter;
    private String pingInt;
    private String PlayerTabName;
    private int taskId;
    public TabCustomizer(Scoreboard scoreboard, ConfigManager config, Main plugin) {
        this.scoreboard = scoreboard;
        this.config = config;
        this.plugin = plugin;

        Bukkit.getLogger().info("TabCustomizer class loaded");


        if (config.getString("tabHeader") != null) {
            tabHeader = config.getString("tabHeader").replace('&', ChatColor.COLOR_CHAR);
        }

        if (config.getString("tabFooter") != null) {
            tabFooter = config.getString("tabFooter").replace('&', ChatColor.COLOR_CHAR);
        }

        if (config.getString("PlayerTabName") != null) {
            PlayerTabName = config.getString("PlayerTabName").replace('&', ChatColor.COLOR_CHAR);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        Team team = scoreboard.getTeam(playerName);
        if (team == null) {
            team = scoreboard.registerNewTeam(playerName);
        }
        team.addEntry(playerName);
        BukkitRunnable task = new BukkitRunnable() {

            @Override
            public void run() {
                if (config.getString("PlayerTabName") != null) {
                    String PlayerTabName = config.getString("PlayerTabName").replace('&', ChatColor.COLOR_CHAR);
                    player.setPlayerListName(
                            ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, PlayerTabName))
                                    .replace("{ping}", String.valueOf(player.getPing()))
                                    .replace("{playerName}", String.valueOf(player.getName()))
                    );
                }

                if (config.getString("tabHeader") != null && config.getString("tabFooter") != null) {
                    String tabHeader = config.getString("tabHeader").replace('&', ChatColor.COLOR_CHAR);
                    String tabFooter = config.getString("tabFooter").replace('&', ChatColor.COLOR_CHAR);
                    player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, tabHeader).replace("{playerName}", String.valueOf(player.getName()))));
                    player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, tabFooter).replace("{playerName}", String.valueOf(player.getName()))));
                }
            }
        };
        task.runTaskTimer(plugin, 0, 40);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Team team = scoreboard.getTeam(player.getName());
        if (team != null) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            if (scheduler.isCurrentlyRunning(taskId)) {
                scheduler.cancelTask(taskId);
            }
            team.removeEntry(player.getName());
        }
    }
}
