package in.rs.milivojevic.KPManager;

import in.rs.milivojevic.KPManager.utils.ConfigManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Objects;

public final class JoinWelcomer implements Listener  {
    private final OkHttpClient client = new OkHttpClient();
    private final ConfigManager config;
    private String messagePlayer;
    private String messageEveryone;
    private String messageError;
    private final Main plugin;
    public JoinWelcomer(Main plugin, ConfigManager configManager) {
        this.config = configManager;
        this.plugin = plugin;
        config.loadConfig();

        if (config.getString("messagePlayer") !=null) {
            messagePlayer = config.getString("messagePlayer").replace('&', ChatColor.COLOR_CHAR);
        }
        if (config.getString("messageEveryone") !=null) {
            messageEveryone = config.getString("messageEveryone").replace('&', ChatColor.COLOR_CHAR);
        }
        if (config.getString("messageError") !=null) {
            messageError = config.getString("messageError").replace('&', ChatColor.COLOR_CHAR);
        }

    }

    private String parseCountry(String xml) {
        Document doc = Jsoup.parse(xml, "", org.jsoup.parser.Parser.xmlParser());
        Elements elements = doc.select("country");
        return elements.text();
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerIP = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();
        event.setJoinMessage(null);
        Request requestIp;
        requestIp = new Request.Builder().url("http://ip-api.com/xml/" + playerIP).build();

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagePlayer.replace("{playerName}", player.getName())));

        try (Response response = client.newCall(requestIp).execute()) {
            assert response.body() != null;
            String body = response.body().string();
            String country = parseCountry(body);

            if (!country.isEmpty()) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', messageEveryone.replace("{playerName}", player.getName()).replace("{country}", country)));
            } else {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', messageError.replace("{playerName}", player.getName())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
