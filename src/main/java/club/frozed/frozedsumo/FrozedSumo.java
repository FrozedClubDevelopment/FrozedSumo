package club.frozed.frozedsumo;

import club.frozed.frozedsumo.commands.AdminCommand;
import club.frozed.frozedsumo.commands.ArenaCommand;
import club.frozed.frozedsumo.commands.DuelCommand;
import club.frozed.frozedsumo.commands.SetSpawnCommand;
import club.frozed.frozedsumo.listeners.*;
import club.frozed.frozedsumo.managers.ArenaManager;
import club.frozed.frozedsumo.managers.MatchManager;
import club.frozed.frozedsumo.managers.QueueManager;
import club.frozed.frozedsumo.managers.StatsSaving;
import club.frozed.frozedsumo.providers.GameScoreboard;
import club.frozed.frozedsumo.utils.Messages;
import club.frozed.frozedsumo.utils.assemble.Assemble;
import club.frozed.frozedsumo.utils.assemble.AssembleStyle;
import club.frozed.frozedsumo.listeners.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class FrozedSumo extends JavaPlugin {

    public static FrozedSumo instance;

    public static QueueManager queueManager;

    public static ArenaManager arenaManager;

    public static MatchManager matchManager;

    public static File arena;
    public static FileConfiguration arenaConfig;

    public static File stats;
    public static FileConfiguration statsConfig;

    public static File config;
    public static FileConfiguration pluginConfig;

    @Override
    public void onEnable() {
        instance = this;

        if (!this.getDescription().getAuthors().contains("Elb1to") || !this.getDescription().getName().equals("FrozedSumo") || !this.getDescription().getDescription().equals("FrozedSumo - Practice-like core for Sumo")) {
            System.exit(0);
            Bukkit.shutdown();
        }

        queueManager = new QueueManager();
        arenaManager = new ArenaManager();
        matchManager = new MatchManager();

        getServer().getConsoleSender().sendMessage(Messages.CC(" "));
        getServer().getConsoleSender().sendMessage(Messages.CC("&7&m-------------------------------------------"));
        getServer().getConsoleSender().sendMessage(Messages.CC("&b&lFrozedSumo &7- &f" + getDescription().getVersion()));
        getServer().getConsoleSender().sendMessage(Messages.CC("&7Developed by &bElb1to"));
        getServer().getConsoleSender().sendMessage(Messages.CC(" "));
        registerConfigs();
        registerListeners();
        registerCommands();
        registerStuffs();
        getServer().getConsoleSender().sendMessage(Messages.CC("&7&m-------------------------------------------"));
        getServer().getConsoleSender().sendMessage(Messages.CC("&bYou're running Spigot - " + getNmsVersion()));
    }

    private String getNmsVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    }

    public void registerConfigs() {
        saveResource("arenas.yml", false);
        saveResource("stats.yml", false);
        saveResource("config.yml", false);

        arena = new File(this.getDataFolder() + "/arenas.yml");
        arenaConfig = YamlConfiguration.loadConfiguration(arena);

        stats = new File(this.getDataFolder() + "/stats.yml");
        statsConfig = YamlConfiguration.loadConfiguration(stats);

        config = new File(this.getDataFolder() + "/config.yml");
        pluginConfig = YamlConfiguration.loadConfiguration(config);

        getServer().getConsoleSender().sendMessage(Messages.CC("&bConfigs Registered Successfully"));
    }

    public void registerCommands() {
        getCommand("sumo").setExecutor(new AdminCommand());
        getCommand("arena").setExecutor(new ArenaCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("duel").setExecutor(new DuelCommand());

        getServer().getConsoleSender().sendMessage(Messages.CC("&bCommands Registered Successfully"));
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerDataListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new QueueListener(), this);
        Bukkit.getPluginManager().registerEvents(new MatchListener(), this);
        Bukkit.getPluginManager().registerEvents(new StatsListener(), this);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), this);
        Bukkit.getPluginManager().registerEvents(new DuelListener(), this);

        getServer().getConsoleSender().sendMessage(Messages.CC("&bListeners Registered Successfully"));
    }

    public void registerStuffs() {
        arenaManager.loadArenas();

        Assemble assemble = new Assemble(this, new GameScoreboard());
        //assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.KOHI);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        StatsSaving.run();

        getServer().getConsoleSender().sendMessage(Messages.CC("&bStuffs Registered Successfully"));
    }

    public static ArenaManager getArenaManager() {
        return arenaManager;
    }

    public static QueueManager getQueueManager() {
        return queueManager;
    }

    public static MatchManager getMatchManager() {
        return matchManager;
    }

    public static FrozedSumo getInstance() {
        return instance;
    }
}
