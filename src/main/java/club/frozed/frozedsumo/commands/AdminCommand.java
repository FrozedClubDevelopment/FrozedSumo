package club.frozed.frozedsumo.commands;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.game.Match;
import club.frozed.frozedsumo.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("sumo.admin")) {
            sender.sendMessage(Messages.CC("&cNo Permission."));
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info")) {
                List<Match> matches = FrozedSumo.getMatchManager().getMatchs();
                if (matches == null || matches.size() == 0) {
                    sender.sendMessage(Messages.CC("&cNo matches running."));
                    return true;
                }
                sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
                sender.sendMessage(Messages.CC("&bServer running &f" + matches.size() + " &dmatch(s)."));
                sender.sendMessage(Messages.CC(""));
                sender.sendMessage(Messages.CC("&fPlayers &7┃ &fArena &7┃ &fType &7┃ &fStatus"));
                for (Match match : matches) {
                    String type = match.isBestof() ? "&bBest Of 3" : "&bNormal";
                    String status = match.isEnded() ? "&cEnded":"&aRunning";
                    sender.sendMessage(Messages.CC("&b" + match.getTeam1().get(0).getName() + " vs " + match.getTeam2().get(0).getName() + " &7┃ &b"+ match.getArena().getName() + " &7┃ " + type + " &7┃ "  + status));
                }
                sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
                return true;
            }
        }
        sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
        sender.sendMessage(Messages.CC("&bSumo FrozedSumo &7[" + FrozedSumo.getInstance().getDescription().getVersion() + "&7]"));
        sender.sendMessage(Messages.CC(""));
        sender.sendMessage(Messages.CC("&3* &bAdmin Commands:"));
        sender.sendMessage(Messages.CC("&f/sumo info &8- &7Display running games"));
        sender.sendMessage(Messages.CC("&f/arena <args> &8- &7Manage your arenas"));
        sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
        return true;
    }
}
