/*
This code was taken from xDefcon's spigot-ping project.
Original code can be found at https://github.com/xDefcon/spigot-ping/blob/master/src/main/java/com/xdefcon/spigotping/utils/PingUtil.java
Original author: Luigi Martinelli (xDefcon)
License: GNU GENERAL PUBLIC LICENSE Version 3
This project uses the code under the terms of the license.
*/

package in.rs.milivojevic.KPManager.utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PingUtil {
    public static int getPing(Player p) {
        String v = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        if (!p.getClass().getName().equals("org.bukkit.craftbukkit." + v + ".entity.CraftPlayer")) { //compatibility with some plugins
            p = Bukkit.getPlayer(p.getUniqueId()); //cast to org.bukkit.entity.Player
        }
        try {
            int ping = p.getPing();
            return ping;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}