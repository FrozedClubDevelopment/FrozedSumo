package club.frozed.frozedsumo.providers;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Match;
import club.frozed.frozedsumo.listeners.PlayerDataListener;
import club.frozed.frozedsumo.utils.assemble.AssembleAdapter;
import club.frozed.frozedsumo.managers.PlayerState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameScoreboard implements AssembleAdapter {

    @Override
    public String getTitle(Player player) {
        return FrozedSumo.getInstance().getConfig().getString("SCOREBOARD.TITLE");
    }

    @Override
    public List<String> getLines(Player player) {
        final List<String> toReturn = new ArrayList<>();
        if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.SPAWN) {
            return getLobby(player);
        }
        if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.FIGHTING) {
            Match match = FrozedSumo.getMatchManager().getByPlayer(player);
            if (match.isEnded()) {
                return getEnded(player);
            }
            Player opponent = getOpponent(match, player);
            if (opponent == null) {
                return getLobby(player);
            }
            for (String sb : FrozedSumo.getInstance().getConfig().getStringList("SCOREBOARD.MATCH")) {
                String msg = sb;
                msg = msg.replace("<player_ping>", String.valueOf(getPing(player)));
                msg = msg.replace("<player_cps>", String.valueOf(PlayerDataListener.getCPS(player)));
                msg = msg.replace("<opponent>", opponent.getName());
                msg = msg.replace("<opponent_ping>", String.valueOf(getPing(opponent)));
                msg = msg.replace("<opponent_cps>", String.valueOf(PlayerDataListener.getCPS(opponent)));
                toReturn.add(msg);
            }
        }
        return toReturn;
    }

    public List<String> getLobby(Player player) {
        List<String> toReturn = new ArrayList<>();
        if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.SPAWN) {
            for (String sb : FrozedSumo.getInstance().getConfig().getStringList("SCOREBOARD.LOBBY")) {
                String msg = sb;
                msg = msg.replace("<online_players>", String.valueOf(FrozedSumo.getInstance().getServer().getOnlinePlayers().size()));
                msg = msg.replace("<match_players>", String.valueOf(FrozedSumo.getMatchManager().getPlaying()));
                toReturn.add(msg);
            }
        }
        return toReturn;
    }

    public List<String> getEnded(Player player) {
        List<String> toReturn = new ArrayList<>();
        Match match = FrozedSumo.getMatchManager().getByPlayer(player);
        Player opponent = getOpponent(match, player);
        if (match.isEnded()) {
            for (String sb : FrozedSumo.getInstance().getConfig().getStringList("SCOREBOARD.MATCH-END")) {
                String msg = sb;
                msg = msg.replace("<opponent>", opponent.getName());
                toReturn.add(msg);
            }
        }
        return toReturn;
    }

    public int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    public Player getOpponent(Match match, Player player) {
        return match.getTeam1().get(0).equals(player) ? match.getTeam2().get(0) : match.getTeam1().get(0);
    }
}
