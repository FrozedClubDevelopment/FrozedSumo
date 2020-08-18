package club.frozed.frozedsumo.game;

import club.frozed.frozedsumo.commands.DuelCommand;
import club.frozed.frozedsumo.managers.ArenaManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Duel {

    private Player sender;
    private Player target;
    private Arena arena;
    private long timestamp;

    public Duel(Player sender, Player target) {
        this.sender = sender;
        this.target = target;
        this.arena = ArenaManager.getRandomArena();
        this.timestamp = System.currentTimeMillis();
    }

    public Player getSender() {
        return sender;
    }

    public void setSender(Player sender) {
        this.sender = sender;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void accept() {
        List<Player> team1 = new ArrayList<>();
        List<Player> team2 = new ArrayList<>();
        team1.add(getSender());
        team2.add(getTarget());

        Match match = new Match(team1,team2, ArenaManager.getRandomArena(),1);
        match.startMatch();
        DuelCommand.duelMap.remove(getSender());
    }
}
