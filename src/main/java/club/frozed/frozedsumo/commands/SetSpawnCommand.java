package club.frozed.frozedsumo.commands;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import club.frozed.frozedsumo.utils.LocationUtil;

import java.io.IOException;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("sumo.setspawn")) {
            sender.sendMessage(Messages.CC("&cNo Permission."));
            return true;
        }

        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        setSpawn(LocationUtil.toString(player.getLocation()));
        player.sendMessage(Messages.CC("&aSpawn set."));
        return true;
    }

    public void setSpawn(String loc) {
        FrozedSumo.arenaConfig.set("Lobby", loc);
        try {
            FrozedSumo.arenaConfig.save(FrozedSumo.arena);
            FrozedSumo.arenaConfig.load(FrozedSumo.arena);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
