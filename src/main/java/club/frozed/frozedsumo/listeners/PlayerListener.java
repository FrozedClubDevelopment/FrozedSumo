package club.frozed.frozedsumo.listeners;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Match;
import club.frozed.frozedsumo.managers.PlayerState;
import club.frozed.frozedsumo.managers.InventoryManager;
import club.frozed.frozedsumo.utils.LocationUtil;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(LocationUtil.toLoc(FrozedSumo.arenaConfig.getString("Lobby")));
        event.setJoinMessage(null);
        InventoryManager.loadLobbyItem(player);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(LocationUtil.toLoc(FrozedSumo.arenaConfig.getString("Lobby")));
            }
        };
        task.runTaskLater(FrozedSumo.getInstance(), 3);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("sumocore.admin") || event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("sumocore.admin") || event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            if (PlayerDataListener.getState(player.getUniqueId()) != PlayerState.FIGHTING || PlayerDataListener.getState(damager.getUniqueId()) != PlayerState.FIGHTING) {
                event.setCancelled(true);
                return;
            }
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    player.setHealth(20);
                    damager.setHealth(20);
                }
            };
            task.runTaskLater(FrozedSumo.getInstance(), 1);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.FIGHTING) {
            Match match = FrozedSumo.getMatchManager().getByPlayer(player);
            if (!match.isStarted() && !match.isEnded()) {
                Location to = event.getTo();
                Location from = event.getFrom();
                if ((to.getX() != from.getX() || to.getZ() != from.getZ())) {
                    player.teleport(from);
                }
            }
        }
    }

    @EventHandler
    public void voidWater(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.FIGHTING) {
            Match match = FrozedSumo.getMatchManager().getByPlayer(player);
            if (match == null) return;
            if (match.isEnded()) return;
            Material block = event.getPlayer().getLocation().getBlock().getType();

            //if (block == Material.PISTON_EXTENSION) {
            //
            //}

            if (block == Material.STATIONARY_WATER || block == Material.WATER) {
                if (match.isBestof() && match.isStarted()) {

                    Player opponent = getOpponent(match, player);
                    boolean isTeam1 = match.isContainTeam1(opponent) ? true : false;

                    if (isTeam1) {
                        match.setTeam1win(match.getTeam1win() - 1);
                    } else {
                        match.setTeam2win(match.getTeam2win() - 1);
                    }
                    if (match.getTeam1win() <= 0 || match.getTeam2win() <= 0) {
                        match.endMatch();
                    } else {
                        match.nextRound(player);
                        return;
                    }
                } else {
                    match.endMatch();
                }

                Player opponent = getOpponent(match, player);

                // Add matches to both players
                PlayerDataListener.getStats(opponent).addMatches();
                PlayerDataListener.getStats(player).addMatches();

                // Add Win to Winner && Add Loss to Loser
                PlayerDataListener.getStats(opponent).addWins();
                PlayerDataListener.getStats(player).addLosses();

                // Add Kills to winner && Add Deaths to loser
                PlayerDataListener.getStats(opponent).addKills();
                PlayerDataListener.getStats(player).addDeaths();

                CraftPlayer playerCp = (CraftPlayer) player;
                EntityPlayer playerEp = playerCp.getHandle();
                playerEp.getDataWatcher().watch(6, 0.0f);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.SPAWN) {
                event.setCancelled(true);
                return;
            }
            if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.ENDING) {
                event.setCancelled(true);
                return;
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
                return;
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
                player.teleport(LocationUtil.toLoc(FrozedSumo.arenaConfig.getString("Lobby")));
                event.setCancelled(true);
                return;
            }
            /*Match match = FrozedSumo.getMatchManager().getByPlayer(player);
            if (match == null)return;
            if (match.isEnded()) {
                event.setCancelled(true);
                return;
            }*/
        }
    }

    /*@EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().contains("unnick") || event.getMessage().contains("nick")) {
            if (FrozedSumo.getQueueManager().isQueued(player) || FrozedSumo.getQueueManager().isQueued(player,true)) {
                event.setCancelled(true);
                player.sendMessage("§cYou need leave queue to nick/unnick.");
                return;
            }

            if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.FIGHTING) {
                event.setCancelled(true);
                player.sendMessage("§cYou cant nick/unnick while in game.");
                return;
            }
        }
    }*/

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().hasPermission("sumocore.admin") || event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickInv(InventoryClickEvent event) {
        if (!event.getWhoClicked().hasPermission("sumocore.admin") || event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
    }

    public Player getOpponent(Match match, Player player) {
        return match.getTeam1().get(0).equals(player) ? match.getTeam2().get(0) : match.getTeam1().get(0);
    }
}
