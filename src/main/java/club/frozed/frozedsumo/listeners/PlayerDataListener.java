package club.frozed.frozedsumo.listeners;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Stats;
import club.frozed.frozedsumo.managers.PlayerState;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataListener implements Listener {

    public static HashMap<UUID, PlayerState> playerState = new HashMap<>();
    public static HashMap<Player, Integer> cps = new HashMap<>();
    public static HashMap<Player, Stats> stats = new HashMap<>();

    public static PlayerState SetState(UUID uuid, PlayerState state) {
        return playerState.put(uuid, state);
    }

    public static PlayerState getState(UUID uuid) {
        return PlayerDataListener.playerState.getOrDefault(uuid, PlayerState.SPAWN);
    }

    public static int getCPS(Player player) {
        return cps.get(player);
    }

    public static Stats getStats(Player player) {
        return stats.get(player);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        cps.put(player, 0);
        if (FrozedSumo.statsConfig.get(player.getUniqueId().toString()) == null) {
            FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".kills", 0);
            FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".deaths", 0);
            FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".matches", 0);
            FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".wins", 0);
            FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".losses", 0);
            try {
                FrozedSumo.statsConfig.save(FrozedSumo.stats);
                FrozedSumo.statsConfig.load(FrozedSumo.stats);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        stats.put(event.getPlayer(), new Stats(player));
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (getState(player.getUniqueId()) == PlayerState.FIGHTING) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                int current = cps.get(player);
                current = current + 1;
                cps.put(player, current);
                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player == null) return;
                        int current = cps.get(player);
                        current = current - 1;
                        cps.put(player, current);
                    }
                };
                task.runTaskLater(FrozedSumo.getInstance(), 20);
            }
        }
    }
}
