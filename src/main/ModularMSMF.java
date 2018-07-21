package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import commands.*;
import listeners.Events;
import util.ChatUtils;
import util.ChatUtils.ChatFormat;
import util.DataManager;
import util.LanguageManager;

/**
 * @author Lightkeks
 * @author davidmc971
 */

public class ModularMSMF extends JavaPlugin implements CommandExecutor {
	private DataManager dataManager;
	private LanguageManager languageManager;
	private CommandMotd motd;

	public final boolean debug = true;
	private String debugTimestamp = "";

	public String getDebugTimestamp() {
		return debugTimestamp;
	}

	private Events mainEvents;

	public Events getMainEvents() {
		return mainEvents;
	}

	public String pluginver = this.getDescription().getVersion();
	public String nameplugin = this.getDescription().getName();
	public List<String> authors = this.getDescription().getAuthors();

	//here our plugin is loaded and will be enabled
	@Override
	public void onEnable() {
		dataManager = new DataManager(this.getLogger());
		dataManager.init();
		this.getServer().getPluginManager().registerEvents(dataManager, this);

		languageManager = new LanguageManager(this);

		motd = new CommandMotd(this);
		motd.load();

		getLogger().info("Loading events...");
		mainEvents = new Events(this);
		this.getServer().getPluginManager().registerEvents(mainEvents, this);
		//this.getServer().getPluginManager().registerEvents(ecoSys, this);

		getLogger().info("Loading commands...");
		//DONE: get list of commands instantiated from commands package
		//DONE: compare to list of commands from plugin.yml
		//DONE: inform about missing commands
		
		CommandLoader loader = new CommandLoader(this);
		ArrayList<AbstractCommand> commandList = loader.loadCommands(this.getClassLoader());

		YamlConfiguration pluginyaml = YamlConfiguration
				.loadConfiguration(new InputStreamReader(this.getResource("plugin.yml")));
		ConfigurationSection cs = pluginyaml.getConfigurationSection("commands");
		Set<String> keyset = cs.getKeys(true);
		keyset.removeIf(s -> s.contains("."));
		keyset.forEach(s -> s.toLowerCase());
		
		ArrayList<String> loadedCommandLabels = new ArrayList<String>();
		commandList.forEach(cmd -> loadedCommandLabels.addAll(Arrays.asList(cmd.getCommandLabels())));
		
		ArrayList<String> commandsNotCoveredByYaml = new ArrayList<String>(loadedCommandLabels);
		commandsNotCoveredByYaml.removeAll(keyset);
		commandsNotCoveredByYaml.forEach(s -> getLogger().severe("The command " + s + " has been loaded but is not in the plugin.yml, it will be unloaded!"));
		//TODO: modify own plugin.yml to include commands
		
		ArrayList<String> commandsNotCoveredByCommandList = new ArrayList<String>(keyset);
		commandsNotCoveredByCommandList.removeAll(loadedCommandLabels);
		commandsNotCoveredByCommandList.forEach(s -> getLogger().severe("The command " + s + " from the plugin.yml is not loaded! A notification will be displayed when it is executed."));

		commandList.removeIf(cmd -> commandsNotCoveredByYaml.containsAll(Arrays.asList(cmd.getCommandLabels())));
		//commandList.forEach(cmd -> this.getCommand(cmd.getCommandLabel().toLowerCase()).setExecutor(cmd));
		commandList.forEach(
				cmd -> {
					Arrays.asList(cmd.getCommandLabels()).forEach(
							s -> {
								this.getCommand(s).setExecutor(cmd);
								});
					});
		
		commandsNotCoveredByCommandList.forEach(s -> this.getCommand(s).setExecutor(this));
		
		commandsNotCoveredByYaml.clear();
		commandsNotCoveredByCommandList.clear();
		
		if (debug) {
			InputStream in = this.getResource("build_timestamp.properties");
			Properties buildprop = new Properties();
			try {
				buildprop.load(in);
				debugTimestamp = buildprop.getProperty("timestamp").replaceAll("_", " ");
				this.getLogger().info("Debug: Build [" + debugTimestamp + "]");
			} catch (IOException e) {
				Bukkit.getLogger().warning("Loading buildprop failed.");
			}
		}
		
		getLogger().info("We are finished with enabling ModularMSMF, hooray!");
	}

	@Override
	public void onLoad() {
		this.getLogger().info("ModularMSMF is loading up.");
	}

	@Override
	public void onDisable() {
		dataManager.saveAllUserdata();
		this.getLogger().info("ModularMSMF has been disabled.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		sender.sendMessage(ChatUtils.getFormattedPrefix(ChatFormat.ERROR) + "I guess that command is not correctly implemented yet :o");
		sender.sendMessage(ChatUtils.getFormattedPrefix(ChatFormat.INFO) + "You can report this using /report bug <description> if you wish!");
		return true;
	}

	public LanguageManager getLanguageManager() {
		return languageManager;
	}

	public DataManager getDataManager() {
		return dataManager;
	}
}