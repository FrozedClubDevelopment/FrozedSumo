package club.frozed.frozedsumo.commands;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Arena;
import club.frozed.frozedsumo.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import club.frozed.frozedsumo.utils.LocationUtil;

import java.io.IOException;

public class ArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("sumo.arena")) {
            sender.sendMessage(Messages.CC("&cNo Permission."));
            return true;
        }
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage("This command is only executable by players.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                createArena(args[1]);
                player.sendMessage(Messages.CC("&aSuccessfully created the arena &b" + args[1] + " &a!"));
                player.sendMessage(Messages.CC("&4&lIMPORTANT &cYou need set spawn1 and spawn2."));
            }
            if (args[0].equalsIgnoreCase("setspawn1")) {
                setSpawn(args[1],1, LocationUtil.toString(player.getLocation()));
                player.sendMessage(Messages.CC("&aSuccessfully set the spawn1 of &b" + args[1] + " &a!"));
            }
            if (args[0].equalsIgnoreCase("setspawn2")) {
                setSpawn(args[1],2, LocationUtil.toString(player.getLocation()));
                player.sendMessage(Messages.CC("&aSuccessfully set the spawn2 of &b" + args[1] + " &a!"));
            }
            return true;
        }
        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------");
        player.sendMessage(Messages.CC("&b&lArena Help &7- &fInformation on how to use arena commands"));
        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------");
        player.sendMessage(Messages.CC("&3* &bArena Creation Commands:"));
        player.sendMessage(Messages.CC("&f/arena create &f<Name>"));
        player.sendMessage(Messages.CC("&f/arena remove &f<Name>"));
        player.sendMessage(Messages.CC(" "));
        player.sendMessage(Messages.CC("&3* &bArena Spawn Commands:"));
        player.sendMessage(Messages.CC("&r  &f/arena setspawn1 &f<Name>"));
        player.sendMessage(Messages.CC("&r  &f/arena setspawn2 &f<Name>"));
        player.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "------------------------------------------------");
        return true;
    }

    public void createArena(String name) {
        FrozedSumo.arenaConfig.set("Arenas." + name + ".spawn1", "None");
        FrozedSumo.arenaConfig.set("Arenas." + name + ".spawn2", "None");
        try {
            FrozedSumo.arenaConfig.save(FrozedSumo.arena);
            FrozedSumo.arenaConfig.load(FrozedSumo.arena);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Arena arena = new Arena(name, null,null);
        FrozedSumo.getArenaManager().getArenas().add(arena);
    }

    public void setSpawn(String name, int number, String loc) {
        FrozedSumo.arenaConfig.set("Arenas." + name + ".spawn" + number, loc);
        try {
            FrozedSumo.arenaConfig.save(FrozedSumo.arena);
            FrozedSumo.arenaConfig.load(FrozedSumo.arena);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Location location = LocationUtil.toLoc(loc);
        FrozedSumo.getArenaManager().getByName(name).setSpawn1(location);
    }
}
