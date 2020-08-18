package club.frozed.frozedsumo.managers;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Match;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class QueueManager {

    private Player queued;
    private Player queuedbestof;

    public QueueManager() {
        this.queued = null;
        this.queuedbestof = null;
    }

    public void addQueue(Player player, boolean bestof) {
        if (bestof) {
            if (queuedbestof != null) {
                if (queuedbestof == player)return;

                List<Player> team1 = new ArrayList<>();
                List<Player> team2 = new ArrayList<>();
                team1.add(queuedbestof);
                team2.add(player);

                Match match = new Match(team1,team2, ArenaManager.getRandomArena(),2, true);
                player.getInventory().clear();
                queuedbestof.getInventory().clear();
                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        match.startMatch();
                        queuedbestof = null;
                    }
                };
                task.runTaskLater(FrozedSumo.getInstance(),1);

            }else {
                this.queuedbestof = player;
            }
        }else {
            addQueue(player);
        }
    }

    public void addQueue(Player player) {
        if (queued != null) {
            if (queued == player)return;

            List<Player> team1 = new ArrayList<>();
            List<Player> team2 = new ArrayList<>();
            team1.add(queued);
            team2.add(player);

            Match match = new Match(team1,team2, ArenaManager.getRandomArena(),1);
            player.getInventory().clear();
            queued.getInventory().clear();
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    match.startMatch();
                    queued = null;
                }
            };
            task.runTaskLater(FrozedSumo.getInstance(),1);

        }else {
            this.queued = player;
        }
    }

    public void removeQueue(Player player, boolean bestof) {
        if (bestof) {
            if (queuedbestof != null && queuedbestof == player) {
                queuedbestof = null;
            }
        }else {
            removeQueue(player);
        }
        if (queued != null && queued == player) {
            queued = null;
        }
    }

    public void removeQueue(Player player) {
        if (queued != null && queued == player) {
            queued = null;
        }
    }

    public boolean isQueued(Player player, boolean bestof) {
        if (bestof) {
            return (queuedbestof != null && queuedbestof == player);
        }else {
            return (queued != null && queued == player);
        }

    }

    public boolean isQueued(Player player) {
        return (queued != null && queued == player);
    }
}
