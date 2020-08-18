package club.frozed.frozedsumo.utils.assemble;

import club.frozed.frozedsumo.utils.assemble.events.AssembleBoardCreatedEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class AssembleBoard {

	private final List<AssembleBoardEntry> entries = new ArrayList<>();
	private final List<String> identifiers = new ArrayList<>();
	private Scoreboard scoreboard;
	private Objective objective;

	private UUID uuid;

	private Assemble assemble;

	public AssembleBoard(Player player, Assemble assemble) {
		this.assemble = assemble;
		this.setup(player);
		this.uuid = player.getUniqueId();
	}

	private void setup(Player player) {


		// Register new scoreboard if needed
		if (getAssemble().isHook() || !(player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard())) {
			this.scoreboard = player.getScoreboard();
		} else {
			this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		}

		// Setup sidebar objective
		this.objective = this.scoreboard.registerNewObjective("Default2", "dummy");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);


		/*
		for (Player player1 : FrozedSumo.plugin.getServer().getOnlinePlayers()) {
			Team team = null;
			//team = this.scoreboard.registerNewTeam(player1.getName());


			if (scoreboard.getTeam(player1.getName()) == null) {
				team = scoreboard.registerNewTeam(player1.getName());
			}else {
				team = scoreboard.getTeam(player1.getName());
			}
			team.setPrefix(FrozedSumo.plugin.getConfig().getString("Settings.namecolor").replace("&", "§"));
			team.addPlayer(player1);
		}*/


		// Update scoreboard
		player.setScoreboard(this.scoreboard);
		//Send Update
		AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
		Bukkit.getPluginManager().callEvent(createdEvent);
	}

	public AssembleBoardEntry getEntryAtPosition(int pos) {
		if (pos >= this.entries.size()) {
			return null;
		} else {
			return this.entries.get(pos);
		}
	}

	public String getUniqueIdentifier(String text) {
		String identifier = getRandomChatColor() + ChatColor.WHITE;

		while (this.identifiers.contains(identifier)) {
			identifier = identifier + getRandomChatColor() + ChatColor.WHITE;
		}

		// This is rare, but just in case, make the method recursive
		if (identifier.length() > 16) {
			return this.getUniqueIdentifier(text);
		}

		// Add our identifier to the list so there are no duplicates
		this.identifiers.add(identifier);

		return identifier;
	}

	// Gets a random ChatColor and returns the String value of it
	private static String getRandomChatColor() {
		return ChatColor.values()[ThreadLocalRandom.current().nextInt(ChatColor.values().length)].toString();
	}
}
