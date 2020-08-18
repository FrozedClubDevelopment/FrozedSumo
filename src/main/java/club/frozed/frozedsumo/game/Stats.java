package club.frozed.frozedsumo.game;

import club.frozed.frozedsumo.FrozedSumo;
import org.bukkit.entity.Player;

public class Stats {

    private Player player;
    private int kills;
    private int deaths;
    private int matches;
    private int wins;
    private int losses;
    private int hits;

    public Stats(Player player) {
        this.player = player;
        this.kills = FrozedSumo.statsConfig.getInt(player.getUniqueId().toString() + ".kills");
        this.deaths = FrozedSumo.statsConfig.getInt(player.getUniqueId().toString() + ".deaths");
        this.matches = FrozedSumo.statsConfig.getInt(player.getUniqueId().toString() + ".matches");
        this.wins = FrozedSumo.statsConfig.getInt(player.getUniqueId().toString() + ".wins");
        this.losses = FrozedSumo.statsConfig.getInt(player.getUniqueId().toString() + ".losses");
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void addKills() {
        this.kills += 1;
    }

    public void addDeaths() {
        this.deaths += 1;
    }

    public void addMatches() {
        this.matches += 1;
    }

    public void addWins() {
        this.wins += 1;
    }

    public void addLosses() {
        this.losses += 1;
    }
}
