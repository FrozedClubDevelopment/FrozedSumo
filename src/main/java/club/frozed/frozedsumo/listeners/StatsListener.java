package club.frozed.frozedsumo.listeners;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Stats;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class StatsListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Stats stats = PlayerDataListener.getStats(player);
        if (stats == null) return;
        FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".kills", stats.getKills());
        FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".deaths", stats.getDeaths());
        FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".matches", stats.getMatches());
        FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".wins", stats.getWins());
        FrozedSumo.statsConfig.set(player.getUniqueId().toString() + ".losses", stats.getLosses());
        try {
            FrozedSumo.statsConfig.save(FrozedSumo.stats);
            FrozedSumo.statsConfig.load(FrozedSumo.stats);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
