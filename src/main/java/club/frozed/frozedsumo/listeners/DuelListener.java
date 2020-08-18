package club.frozed.frozedsumo.listeners;

import club.frozed.frozedsumo.commands.DuelCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class DuelListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (DuelCommand.duelMap.containsKey(event.getPlayer())) {
            DuelCommand.duelMap.put(event.getPlayer(),null);
        }
    }
}
