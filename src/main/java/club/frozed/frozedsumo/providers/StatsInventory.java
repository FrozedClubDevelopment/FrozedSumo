package club.frozed.frozedsumo.providers;

import club.frozed.frozedsumo.listeners.PlayerDataListener;
import club.frozed.frozedsumo.game.Stats;
import club.frozed.frozedsumo.utils.ItemBuilder;
import club.frozed.frozedsumo.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.math.BigDecimal;

public class StatsInventory {

    public static void openStats(Player player) {
        Stats stats = PlayerDataListener.getStats(player);
        if (stats == null) return;
        Inventory menu = Bukkit.createInventory(null, 9 * 3, Messages.CC("&8Stats of " + player.getName()));
        for (int x = 0; x < 27; x++) {
            menu.setItem(x, ItemBuilder.build(Material.STAINED_GLASS_PANE, 1, 15, "&0.", null));
        }
        ItemBuilder stat = new ItemBuilder(Material.SKULL_ITEM);
        stat.setDurability(3);
        stat.setName("&3Your Statistics");
        stat.addLoreLine("&7&m-----------------------");
        stat.addLoreLine("&bKills&7: &f" + stats.getKills());
        stat.addLoreLine("&bDeaths&7: &f" + stats.getDeaths());
        stat.addLoreLine("&3 * &bKDR&7: &f" + toKDR(stats.getKills(), stats.getDeaths()));
        stat.addLoreLine("&8&m-----------------------");
        stat.addLoreLine("&bMatches played&7: &f" + stats.getMatches());
        stat.addLoreLine("&3 * &bWins&7: &f" + stats.getWins());
        stat.addLoreLine("&3 * &bLosses&7: &f" + stats.getLosses());
        stat.addLoreLine("&7&m-----------------------");
        menu.setItem(13, stat.toItemStack());
        player.openInventory(menu);
    }

    public static double toKDR(int kills, int deaths) {
        double kd = kills;
        if (deaths > 0) {
            kd = (double) kills / deaths;
            BigDecimal bd = new BigDecimal(kd);
            BigDecimal bd2 = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            kd = bd2.doubleValue();
        }
        return kd;
    }
}
