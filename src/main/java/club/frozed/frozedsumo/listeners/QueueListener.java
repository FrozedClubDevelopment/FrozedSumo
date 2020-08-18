package club.frozed.frozedsumo.listeners;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.providers.StatsInventory;
import club.frozed.frozedsumo.utils.BungeeUtil;
import club.frozed.frozedsumo.utils.ItemBuilder;
import club.frozed.frozedsumo.utils.Messages;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class QueueListener implements Listener {

    //private HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getItem().getType() == Material.AIR || event.getItem().getItemMeta() == null|| event.getItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        Player player = event.getPlayer();
        if (event.getAction().toString().startsWith("RIGHT_")) {
            /*if (cooldown.containsKey(player.getUniqueId()) && cooldown.get(player.getUniqueId()) > System.currentTimeMillis()) {
                event.setCancelled(true);
                long remainingTime = cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                player.sendMessage(Messages.CC("&cPlease wait &l" + remainingTime/1000 + " seconds &cbefore using this again!"));
            } else {
                cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (3 * 1000));
            }*/
                if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Messages.CC("&bSumo &3[Best of 1] &7(Right Click)"))) {
                    event.setCancelled(true);
                    if (FrozedSumo.getQueueManager().isQueued(player, true)) {
                        player.sendMessage(Messages.CC("&cYou're already queueing on the &bSumo &3[Best of 3] &cQueue!"));
                        return;
                    }
                    FrozedSumo.getQueueManager().addQueue(player);
                    player.sendMessage(Messages.CC("&7You were added to the &bSumo &3[Best of 1] &bQueue&7."));
                    ItemStack item = ItemBuilder.build(Material.IRON_SWORD, 1, 0, "&cLeave Sumo &4[Best of 1] &cQueue &7(Right Click)", null);
                    item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                    item.getItemMeta().spigot().setUnbreakable(true);
                    player.setItemInHand(item);
                }

                if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Messages.CC("&cLeave Sumo &4[Best of 1] &cQueue &7(Right Click)"))) {
                    event.setCancelled(true);
                    FrozedSumo.getQueueManager().removeQueue(player);
                    player.sendMessage(Messages.CC("&7You were removed from the &bSumo &3[Best of 3] &7Queue."));
                    player.setItemInHand(ItemBuilder.build(Material.IRON_SWORD, 1,0,"&bSumo &3[Best of 1] &7(Right Click)",null));
                }

                if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Messages.CC("&bSumo &3[Best of 3] &7(Right Click)"))) {
                    event.setCancelled(true);
                    if (FrozedSumo.getQueueManager().isQueued(player)) {
                        player.sendMessage(Messages.CC("&cYou're already queueing on the &bSumo &3[Best of 1] &cQueue!"));
                        return;
                    }
                    FrozedSumo.getQueueManager().addQueue(player,true);
                    player.sendMessage(Messages.CC("&7You were added to the &bSumo &3[Best of 3] &7Queue&7."));
                    ItemStack item = ItemBuilder.build(Material.DIAMOND_SWORD, 1,0,"&cLeave Sumo &4[Best of 3] &cQueue &7(Right Click)",null);
                    item.addEnchantment(Enchantment.DAMAGE_ALL,1);
                    item.getItemMeta().spigot().setUnbreakable(true);
                    player.setItemInHand(item);
                }

                if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Messages.CC("&cLeave Sumo &4[Best of 3] &cQueue &7(Right Click)"))) {
                    event.setCancelled(true);
                    FrozedSumo.getQueueManager().removeQueue(player,true);
                    player.sendMessage(Messages.CC("&7You were removed from the &bSumo &3[Best of 3] &7Queue&7."));
                    player.setItemInHand(ItemBuilder.build(Material.DIAMOND_SWORD, 1,0,"&bSumo &3[Best of 3] &7(Right Click)",null));
                }

                if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Messages.CC("&bView stats &7(Right Click)"))) {
                    event.setCancelled(true);
                    StatsInventory.openStats(player);
                }

                if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(Messages.CC("&cBack to Lobby &7(Right Click)"))) {
                    BungeeUtil.sendPlayer(player, FrozedSumo.getInstance().getConfig().getString("LobbyServer"));
                }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage(null);
        if (FrozedSumo.getQueueManager().isQueued(player)) {
            FrozedSumo.getQueueManager().removeQueue(player );
        }

        if (FrozedSumo.getQueueManager().isQueued(player, true)) {
            FrozedSumo.getQueueManager().removeQueue(player, true);
        }
    }
}
