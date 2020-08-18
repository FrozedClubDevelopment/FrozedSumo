package club.frozed.frozedsumo.commands;

import club.frozed.frozedsumo.FrozedSumo;
import club.frozed.frozedsumo.listeners.PlayerDataListener;
import club.frozed.frozedsumo.managers.PlayerState;
import club.frozed.frozedsumo.game.Duel;
import club.frozed.frozedsumo.utils.Messages;
import club.frozed.frozedsumo.utils.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class DuelCommand implements CommandExecutor {

    public static HashMap<Player, Duel> duelMap = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("accept")) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(Messages.CC("&cThat player is offline."));
                    return true;
                }
                if (FrozedSumo.getQueueManager().isQueued(player) || (FrozedSumo.getQueueManager().isQueued(player, true)
                || FrozedSumo.getQueueManager().isQueued(target) || (FrozedSumo.getQueueManager().isQueued(target, true)))) {
                    player.sendMessage(Messages.CC("&cYou or " + target.getName() + " already in the queue."));
                    return true;
                }
                if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.FIGHTING) {
                    player.sendMessage(Messages.CC("&cYou already in the game."));
                    return true;
                }
                if (PlayerDataListener.getState(target.getUniqueId()) == PlayerState.FIGHTING) {
                    player.sendMessage(Messages.CC("&c" +target.getName() + " already in the game."));
                    return true;
                }
                Duel duel = duelMap.get(target);
                if (duel == null) {
                    player.sendMessage(Messages.CC("&c" + target.getName() + "'s duel request has expired."));
                    return true;
                }
                duel.accept();
                return true;
            }
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Messages.CC("&cThat player is offline."));
                return true;
            }
            if (player == target) {
                player.sendMessage(Messages.CC("&cYou cant duel yourself."));
                return true;
            }
            if (PlayerDataListener.getState(player.getUniqueId()) == PlayerState.FIGHTING) {
                player.sendMessage(Messages.CC("&cYou already in the game."));
                return true;
            }
            if (PlayerDataListener.getState(target.getUniqueId()) == PlayerState.FIGHTING) {
                player.sendMessage(Messages.CC("&c" +target.getName() + " already in the game."));
                return true;
            }
            if (duelMap.get(player) == target) {
                player.sendMessage(Messages.CC("&cYou already sent duel request to " + target.getName() + "."));
                return true;
            }
            sendDuel(new Duel(player, target));
            return true;
        }
        player.sendMessage(Messages.CC("&cUsage: /duel <player>"));
        return true;
    }

    public void sendDuel(Duel duel) {
        Player sender = duel.getSender();
        Player target = duel.getTarget();
        duelMap.put(sender, duel);
        sender.sendMessage(Messages.CC("&eYou have sent a duel request to &b" + target.getName() + " &ewith kit &bSumo &eon arena &b" + duel.getArena().getName() + "&e."));
        target.sendMessage(Messages.CC("&b" + sender.getName() + " &ehas sent you a duel request with kit &bSumo &eon Arena &b" + duel.getArena().getName() + "&e."));
        new FancyMessage("§a[Click to accept]").tooltip("§eClick to accept §b" + sender.getName() + "§e's duel request").command("/duel accept " + sender.getName()).send(target);
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (sender == null) {
                    duelMap.put(sender, null);
                }
            }
        };
        task.runTaskLater(FrozedSumo.getInstance(), 180*20);
    }
}
