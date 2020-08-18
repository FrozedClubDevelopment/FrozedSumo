package club.frozed.frozedsumo.utils;

import club.frozed.frozedsumo.FrozedSumo;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeUtil {

    public static void sendPlayer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
        }
        player.sendMessage(Messages.CC("&bConnecting to &o" + server + "&7..."));
        player.sendPluginMessage(FrozedSumo.getInstance(), "BungeeCord", b.toByteArray());
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(Messages.CC("&cFailed to connect " + server + "."));
            }
        };
        task.runTaskLater(FrozedSumo.getInstance(), 20);
    }
}
