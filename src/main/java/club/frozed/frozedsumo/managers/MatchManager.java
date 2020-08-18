package club.frozed.frozedsumo.managers;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Match;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MatchManager {

    private List<Match> matchs;

    public MatchManager() {
        matchs = new ArrayList<>();
    }

    public void addMatch(Match match) {
        matchs.add(match);
    }

    public void removeMatch(Match match) {
        matchs.remove(match);
    }

    public List<Match> getMatchs() {
        return matchs;
    }

    public Match getByPlayer(Player player) {
        for (Match match : FrozedSumo.getMatchManager().getMatchs()) {
            if (match.isContain(player)) {
                return match;
            }
        }
        return null;
    }

    public int getPlaying() {
        int playing = 0;
        for (Match match : getMatchs()) {
            playing += match.getTeam1().size() + match.getTeam2().size();
        }
        return playing;
    }
}
