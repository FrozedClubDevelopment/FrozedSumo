package club.frozed.frozedsumo.game;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.listeners.PlayerDataListener;
import club.frozed.frozedsumo.managers.PlayerState;
import club.frozed.frozedsumo.managers.InventoryManager;
import club.frozed.frozedsumo.utils.LocationUtil;
import club.frozed.frozedsumo.utils.Messages;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Match {

    private List<Player> team1;
    private List<Player> team2;
    private Arena arena;
    private int count;
    private int team1win;
    private int team2win;
    private boolean bestof;
    private boolean started;
    private boolean ended;

    public Match(List<Player> team1, List<Player> team2, Arena arena, int round) {
        this.team1 = team1;
        this.team2 = team2;
        this.arena = arena;
        this.count = 5;
        this.team1win = round;
        this.team2win = round;
        this.bestof = false;
        this.started = false;
        this.ended = false;
    }

    public Match(List<Player> team1, List<Player> team2, Arena arena, int round, boolean bestof) {
        this.team1 = team1;
        this.team2 = team2;
        this.arena = arena;
        this.count = 5;
        this.team1win = round;
        this.team2win = round;
        this.bestof = bestof;
        this.started = false;
        this.ended = false;
    }

    public List<Player> getTeam1() {
        return team1;
    }

    public void setTeam1(List<Player> team1) {
        this.team1 = team1;
    }

    public List<Player> getTeam2() {
        return team2;
    }

    public void setTeam2(List<Player> team2) {
        this.team2 = team2;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTeam1win() {
        return team1win;
    }

    public void setTeam1win(int team1win) {
        this.team1win = team1win;
    }

    public int getTeam2win() {
        return team2win;
    }

    public void setTeam2win(int team2win) {
        this.team2win = team2win;
    }

    public boolean isBestof() {
        return bestof;
    }

    public void setBestof(boolean bestof) {
        this.bestof = bestof;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public void startMatch() {
        FrozedSumo.getMatchManager().addMatch(this);
        startCountdown();
        for (Player player1 : getTeam1()) {
            PlayerDataListener.SetState(player1.getUniqueId(), PlayerState.FIGHTING);
            player1.getInventory().clear();
            player1.teleport(getArena().getSpawn1());

            for (Player all : FrozedSumo.getInstance().getServer().getOnlinePlayers()) {
                all.hidePlayer(player1);
                player1.hidePlayer(all);
            }

            for (Player t1 : getTeam2()) {
                player1.showPlayer(t1);
            }
        }
        for (Player player2 : getTeam2()) {
            PlayerDataListener.SetState(player2.getUniqueId(), PlayerState.FIGHTING);
            player2.getInventory().clear();
            player2.teleport(getArena().getSpawn2());

            for (Player all : FrozedSumo.getInstance().getServer().getOnlinePlayers()) {
                if (!getTeam1().contains(all)) {
                    all.hidePlayer(player2);
                    player2.hidePlayer(all);
                }
            }

            for (Player t2 : getTeam1()) {
                player2.showPlayer(t2);
            }
        }
    }

    public void startCountdown() {
        if (getTeam1().size() == 0 || getTeam2().size() == 0) {
            endMatch();
            return;
        }
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (getCount() >= 1) {
                    String seconds = getCount() == 1 ? " second" : " seconds";
                    broadcastWithSound(getTeam1(), Messages.CC("&eThe match starts in &b" + getCount() + seconds + "&e..."), Sound.NOTE_STICKS, 1);
                    broadcastWithSound(getTeam2(), Messages.CC("&eThe match starts in &b" + getCount() + seconds + "&e..."), Sound.NOTE_STICKS, 1);
                    count -= 1;
                    return;
                }

                if (getCount() == 0) {
                    broadcastWithSound(getTeam1(), Messages.CC("&eThe match has started!"), Sound.LEVEL_UP, 1);
                    broadcastWithSound(getTeam2(), Messages.CC("&eThe match has started!"), Sound.LEVEL_UP, 1);
                    setStarted(true);
                    this.cancel();
                }
            }
        };
        task.runTaskTimer(FrozedSumo.getInstance(), 0, 20);
    }

    public void endMatch() {
        setEnded(true);
        Match match = this;
        broadcast(getTeam1(), Messages.CC("&eMatch ended."));
        broadcast(getTeam2(), Messages.CC("&eMatch ended."));
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player1 : getTeam1()) {
                    PlayerDataListener.SetState(player1.getUniqueId(), PlayerState.SPAWN);
                    InventoryManager.loadLobbyItem(player1);
                    player1.teleport(LocationUtil.toLoc(FrozedSumo.arenaConfig.getString("Lobby")));
                }

                for (Player player2 : getTeam2()) {
                    PlayerDataListener.SetState(player2.getUniqueId(), PlayerState.SPAWN);
                    InventoryManager.loadLobbyItem(player2);
                    player2.teleport(LocationUtil.toLoc(FrozedSumo.arenaConfig.getString("Lobby")));
                }

                for (Player all : FrozedSumo.getInstance().getServer().getOnlinePlayers()) {
                    for (Player player1 : getTeam1()) {
                        player1.hidePlayer(all);
                        player1.showPlayer(all);
                        all.hidePlayer(player1);
                        all.showPlayer(player1);
                    }

                    for (Player player2 : getTeam2()) {
                        player2.hidePlayer(all);
                        player2.showPlayer(all);
                        all.hidePlayer(player2);
                        all.showPlayer(player2);
                    }
                }
                FrozedSumo.getMatchManager().removeMatch(match);
            }
        };
        task.runTaskLater(FrozedSumo.getInstance(), 4 * 20);
    }

    public void nextRound(Player player) {
        Match match = this;

        if (getTeam1win() >= 1 || getTeam2win() >= 1) {
            for (Player player1 : getTeam1()) {
                player1.teleport(getArena().getSpawn1());
            }

            for (Player player2 : getTeam2()) {
                player2.teleport(getArena().getSpawn2());
            }
        }

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (isEnded()) return;
                setStarted(false);
                setCount(5);

                Player opponent = getOpponent(match, player);
                boolean isTeam1 = isContainTeam1(opponent);

                if (isTeam1) {
                    broadcast(getTeam1(), Messages.CC("&b" + opponent.getName() + " &ewon the round, You need &b" + getTeam1win() + " &emore to win."));
                    broadcast(getTeam2(), Messages.CC("&b" + opponent.getName() + " &ewon the round, They need &b" + getTeam1win() + " &emore to win."));
                } else {
                    broadcast(getTeam1(), Messages.CC("&b" + opponent.getName() + " &ewon the round, They need &b" + getTeam2win() + " &emore to win."));
                    broadcast(getTeam2(), Messages.CC("&b" + opponent.getName() + " &ewon the round, You need &b" + getTeam2win() + " &emore to win."));
                }
                startCountdown();
            }
        };
        task.runTaskLater(FrozedSumo.getInstance(), 3);
    }

    public void broadcastWithSound(List<Player> team, String msg, Sound sound, int pitch) {
        for (Player player : team) {
            player.sendMessage(Messages.CC(msg));
            player.playSound(player.getLocation(), sound, 1, pitch);
        }
    }

    public void broadcast(List<Player> team, String msg) {
        for (Player player : team) {
            player.sendMessage(Messages.CC(msg));
        }
    }

    public boolean isContain(Player player) {
        return getTeam1().contains(player) || getTeam2().contains(player);
    }

    public boolean isContainTeam1(Player player) {
        return getTeam1().contains(player);
    }

    public Player getOpponent(Match match, Player player) {
        return match.getTeam1().get(0).equals(player) ? match.getTeam2().get(0) : match.getTeam1().get(0);
    }
}
