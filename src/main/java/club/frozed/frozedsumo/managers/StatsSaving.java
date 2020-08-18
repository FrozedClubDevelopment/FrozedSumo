package club.frozed.frozedsumo.managers;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Stats;
import club.frozed.frozedsumo.listeners.PlayerDataListener;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

public class StatsSaving {

    public static void run() {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player all : FrozedSumo.getInstance().getServer().getOnlinePlayers()) {
                    Stats stats = PlayerDataListener.getStats(all);
                    if (stats == null) continue;
                    FrozedSumo.statsConfig.set(all.getUniqueId().toString() + ".kills", stats.getKills());
                    FrozedSumo.statsConfig.set(all.getUniqueId().toString() + ".deaths", stats.getDeaths());
                    FrozedSumo.statsConfig.set(all.getUniqueId().toString() + ".matches", stats.getMatches());
                    FrozedSumo.statsConfig.set(all.getUniqueId().toString() + ".wins", stats.getWins());
                    FrozedSumo.statsConfig.set(all.getUniqueId().toString() + ".losses", stats.getLosses());
                    try {
                        FrozedSumo.statsConfig.save(FrozedSumo.stats);
                        FrozedSumo.statsConfig.load(FrozedSumo.stats);
                    } catch (IOException | InvalidConfigurationException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        task.runTaskTimer(FrozedSumo.getInstance(), 180 * 20, 180 * 20);
    }
}
