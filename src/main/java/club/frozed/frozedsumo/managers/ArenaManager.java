package club.frozed.frozedsumo.managers;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Arena;
import org.bukkit.Location;
import club.frozed.frozedsumo.utils.LocationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArenaManager {

    private List<Arena> arenas;

    public ArenaManager() {
        this.arenas = new ArrayList<>();
    }

    public void loadArenas() {
        if (FrozedSumo.arenaConfig.get("Arenas") == null) return;
        for (String r : FrozedSumo.arenaConfig.getConfigurationSection("Arenas").getKeys(false)) {
            Location spawn1 = LocationUtil.toLoc(FrozedSumo.arenaConfig.getString("Arenas." + r + ".spawn1"));
            Location spawn2 = LocationUtil.toLoc(FrozedSumo.arenaConfig.getString("Arenas." + r + ".spawn2"));
            Arena arena = new Arena(r,spawn1,spawn2);
            if (arena == null)continue;
            arenas.add(arena);
        }
    }

    public static Arena getRandomArena() {
        return FrozedSumo.getArenaManager().getArenas().get(new Random().nextInt(FrozedSumo.getArenaManager().getArenas().size()));
    }

    public Arena getByName(String name) {
        for (Arena arena : FrozedSumo.getArenaManager().getArenas()) {
            if (arena.getName().equals(name)) {
                return arena;
            }
        }
        return null;
    }

    public List<Arena> getArenas() {
        return arenas;
    }
}
