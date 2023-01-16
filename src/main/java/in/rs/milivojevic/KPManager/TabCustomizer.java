package in.rs.milivojevic.KPManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scheduler.BukkitRunnable;
import in.rs.milivojevic.KPManager.utils.PingUtil;

public class TabCustomizer implements Listener {
    private final Scoreboard scoreboard;
    private FileConfiguration config;
    private String tabHeader;
    private String tabFooter;
    private String pingInt;
    private final Main plugin;
    private boolean hasPingInt;
    private boolean hasTabHeaderFooter;
    public TabCustomizer(Scoreboard scoreboard, Main plugin) {
        this.scoreboard = scoreboard;
        this.plugin = plugin;
        config = plugin.getConfig();

        hasPingInt = config.contains("pingInt") && config.getString("pingInt") != null;

        hasTabHeaderFooter = config.contains("tabHeader") && config.getString("tabHeader") != null && config.contains("tabFooter") && config.getString("tabFooter") != null;



        if (config.getString("tabHeader") !=null){
            tabHeader = config.getString("tabHeader").replace('&', ChatColor.COLOR_CHAR);
        }

        if (config.getString("tabFooter") !=null) {
            tabFooter = config.getString("tabFooter").replace('&', ChatColor.COLOR_CHAR);
        }

        if (config.getString("pingInt") !=null) {
            pingInt = config.getString("pingInt").replace('&', ChatColor.COLOR_CHAR);
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
                int ping = PingUtil.getPing(player);
                if (hasPingInt) {
                    player.setPlayerListName(playerName + ChatColor.translateAlternateColorCodes('&', pingInt.replace("{ping}", String.valueOf(ping))));
                }
                if (hasTabHeaderFooter) {
                    player.setPlayerListHeaderFooter(ChatColor.translateAlternateColorCodes('&', tabHeader.replace("{playerName}", player.getName())), ChatColor.translateAlternateColorCodes('&', tabFooter.replace("{playerName}", player.getName())));
                }
            }
        };
        task.runTaskTimer(plugin, 0, 20);
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Team team = scoreboard.getTeam(player.getName());
        if (team != null) {
            team.removeEntry(player.getName());
        }
    }
}
