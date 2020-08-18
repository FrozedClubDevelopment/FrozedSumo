package club.frozed.frozedsumo.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            players.add(online);
        }
        return players;
    }

    public static ItemStack createGlass(String name) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta itemmeta = item.getItemMeta();
        itemmeta.setDisplayName(name);
        item.setItemMeta(itemmeta);

        return item;
    }
}
