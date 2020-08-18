package club.frozed.frozedsumo.listeners;

import club.frozed.frozedsumo.game.Match;
import club.frozed.frozedsumo.FrozedSumo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MatchListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Match match = FrozedSumo.getMatchManager().getByPlayer(player);
        if (match != null) {
            if (!match.isEnded()) {
                match.endMatch();
            }
        }
    }
}
