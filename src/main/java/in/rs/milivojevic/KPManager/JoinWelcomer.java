package in.rs.milivojevic.KPManager;

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

public final class JoinWelcomer implements Listener {
    private final OkHttpClient client = new OkHttpClient();
    private final String messagePlayer = ChatColor.AQUA + "Welcome " + ChatColor.GREEN + "{playerName}" + ChatColor.AQUA + " to my server! " + ChatColor.GREEN + "Enjoy your stay!";
    private final String messageEveryone = ChatColor.AQUA + "{playerName} joined from " + ChatColor.GREEN + "{country}.";

    private String parseCountry(String xml) {
        Document doc = Jsoup.parse(xml, "", org.jsoup.parser.Parser.xmlParser());
        Elements elements = doc.select("country_name");
        return elements.text();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerIP = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();

        Request requestIp;
        requestIp = new Request.Builder().url("https://ipapi.co/" + playerIP + "/xml/").build();

        player.sendMessage(messagePlayer.replace("{playerName}", player.getName()));

        try (Response response = client.newCall(requestIp).execute()) {
            assert response.body() != null;
            String body = response.body().string();
            String country = parseCountry(body);
            if (!country.isEmpty()) {
                Bukkit.broadcastMessage(messageEveryone.replace("{playerName}", player.getName()).replace("{country}", country));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

