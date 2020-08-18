package club.frozed.frozedsumo.managers;

import club.frozed.frozedsumo.utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryManager {

    public static void loadLobbyItem(Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.setHealth(20);
        player.setAllowFlight(false);
        player.setFoodLevel(20);
        player.setExp(0);
        player.getInventory().setHeldItemSlot(0);
        ItemStack ironsd = ItemBuilder.build(Material.IRON_SWORD,1,0,"&bSumo &3[Best of 1] &7(Right Click)",null);
        ItemStack diasd = ItemBuilder.build(Material.DIAMOND_SWORD,1,0,"&bSumo &3[Best of 3] &7(Right Click)",null);
        ItemMeta ironsdmeta = ironsd.getItemMeta();
        ItemMeta diasdmeta = diasd.getItemMeta();
        ironsdmeta.spigot().setUnbreakable(true);
        diasdmeta.spigot().setUnbreakable(true);
        ironsd.setItemMeta(ironsdmeta);
        diasd.setItemMeta(diasdmeta);
        player.getInventory().setItem(0, ironsd);
        player.getInventory().setItem(1, diasd);
        player.getInventory().setItem(4, ItemBuilder.build(Material.NETHER_STAR,1,0,"&bView stats &7(Right Click)",null));
        player.getInventory().setItem(8, ItemBuilder.build(Material.REDSTONE,1,0,"&cBack to Lobby &7(Right Click)",null));
    }
}
