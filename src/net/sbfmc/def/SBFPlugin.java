/*
 Copyright 2014 Reo_SP

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 	http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package net.sbfmc.def;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sbfmc.logging.DebugUtils;
import net.sbfmc.logging.LogFilter;
import net.sbfmc.module.Module;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.admin.AdminModule;
import net.sbfmc.modules.anticheat.AnticheatModule;
import net.sbfmc.modules.homes.HomesModule;
import net.sbfmc.modules.inv.InventoryModule;
import net.sbfmc.modules.messenger.MessengerModule;
import net.sbfmc.modules.mooncraft.MooncraftModule;
import net.sbfmc.modules.prefixer.PrefixerModule;
import net.sbfmc.modules.psycho.PsychoModule;
import net.sbfmc.modules.secret.SecretModule;
import net.sbfmc.modules.tools.ToolsModule;
import net.sbfmc.modules.worldsave.WorldSaveModule;
import net.sbfmc.server.SBFServer;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.virtualplayers.VirtualPlayersManager;
import net.sbfmc.virtualplayers.light.LightVirtualPlayersListener;
import net.sbfmc.world.GenerationQueue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

public final class SBFPlugin extends JavaPlugin {
	private static SBFPlugin plugin;
	private static Collection<Module> modules = new ArrayList<Module>();

	@Override
	public synchronized void onEnable() {
		plugin = this;

		DebugUtils.debug("CORE", "SBF Server Mod is enabling...");

		// starting sbf server
		try {
			SBFServer.startServer();
		} catch (Exception err) {
			DebugUtils.debugError("sbf-server", "SBF sub server can't start!", err);
		}

		// starting generate queue
		getLogger().info("Starting generation queue");
		try {
			GenerationQueue.start();
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Generation queue can't start!", err);
		}

		// registering modules
		getLogger().info("Registering all modules");
		try {
			registerAllModules();
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Modules can't be registered!", err);
		}

		// enabling mods
		for (Module module : modules) {
			try {
				getLogger().info("Enabling module \"" + module.getName() + "\"...");
				module.setEnabled(true);
				getLogger().info("Module \"" + module.getName() + "\" enabled!");
			} catch (Exception err) {
				DebugUtils.debugError("CORE", "Module \"" + module.getName() + "\" has an error on enabling! ", err);
			}
		}

		// adding all player sessions
		getLogger().info("Adding all player sessions of online players");
		for (Player player : Bukkit.getOnlinePlayers()) {
			for (Module module : modules) {
				try {
					module.registerPlayer(player);
				} catch (Exception err) {
					DebugUtils.debugError("CORE", "Module \"" + module.getName() + "\" has an error on player register!", err);
				}
			}
		}

		// registering listeners
		getLogger().info("Registering listeners");
		try {
			Bukkit.getPluginManager().registerEvents(new SBFListener(), this);
			if (GeneralUtils.isPluginEnabled("ProtocolLib")) {
				Bukkit.getPluginManager().registerEvents(new LightVirtualPlayersListener(), this);
				ProtocolLibrary.getProtocolManager().addPacketListener(new LightVirtualPlayersListener());
			}
			// Bukkit.getPluginManager().registerEvents(new DangerousVirtualPlayersListener(), this); // explodes
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Some of our listeners can't be registered!", err);
		}

		// registering log filter
		getLogger().info("Registering log filter");
		try {
			LogFilter.registerMe();
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Log filter can't be registered!", err);
		}

		// saying about enabling
		getLogger().info("SBF Server Mod enabled!");
		GeneralUtils.sayToAll(ChatColor.GOLD + "----- " + ChatColor.AQUA + ChatColor.BOLD + "SBF Server Mod" + ChatColor.GOLD + " -----");
		GeneralUtils.sayToAll("");
		GeneralUtils.sayToAll(ChatColor.RED + "Плагин запущен!");
		GeneralUtils.sayToAll("");
	}

	@Override
	public synchronized void onDisable() {
		DebugUtils.debug("CORE", "SBF Server Mod is disabling...");

		// removing all player sessions
		getLogger().info("Removing all player sessions");
		for (Player player : Bukkit.getOnlinePlayers()) {
			for (Module module : modules) {
				try {
					module.unregisterPlayer(player);
				} catch (Exception err) {
					DebugUtils.debugError("CORE", "Module \"" + module.getName() + "\" has an error on player unregister!", err);
				}
			}
		}

		// stoping generation queue
		getLogger().info("Stopping generation queue");
		try {
			GenerationQueue.stop();
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Can't stop generation queue", err);
		}

		// disabling mods
		getLogger().info("Disabling modules");
		for (Module module : modules) {
			try {
				getLogger().info("Disabling module \"" + module.getName() + "\"...");
				module.setEnabled(false);
				getLogger().info("Module " + module.getName() + " disabled!");
			} catch (Exception err) {
				DebugUtils.debugError("CORE", "Module \"" + module.getName() + "\" has an error on disabling! ", err);
			}
		}

		// unregistering mods
		try {
			modules.clear();
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Can't unregister mods", err);
		}

		// reseting crafts
		getLogger().info("Reseting recipes to default");
		try {
			Bukkit.resetRecipes();
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Can't reset recipes", err);
		}

		// canceling tasks
		getLogger().info("Canceling tasks");
		try {
			Bukkit.getScheduler().cancelTasks(this);
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Can't cancel tasks", err);
		}

		// disconnecting all virtual players
		getLogger().info("Removing all virtual players");
		try {
			VirtualPlayersManager.removeAll();
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Can't remove all virtual players", err);
		}

		getLogger().info("Unregistering log filter");
		try {
			LogFilter.unregisterMe();
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Can't unregister log filter", err);
		}

		// stopping sbf server
		try {
			SBFServer.stopServer();
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "SBF sub server can't stop! ", err);
		}

		// unregistering listeners
		getLogger().info("Unregistering listeners");
		try {
			if (GeneralUtils.isPluginEnabled("ProtocolLib")) {
				ProtocolLibrary.getProtocolManager().removePacketListeners(this);
			}
		} catch (Exception err) {
			DebugUtils.debugError("CORE", "Some of our listeners can't be registered!", err);
		}

		// set plugin instance to null
		plugin = null;

		// saying about disabling
		getLogger().info("SBF Server Mod disabled!");
		GeneralUtils.sayToAll(ChatColor.GOLD + "----- " + ChatColor.AQUA + ChatColor.BOLD + "SBF Server Mod" + ChatColor.GOLD + " -----");
		GeneralUtils.sayToAll("");
		GeneralUtils.sayToAll(ChatColor.RED + "Плагин отключен!");
		GeneralUtils.sayToAll(ChatColor.RED + "Не совершайте никаких действий пока плагин не будет запущен!");
		GeneralUtils.sayToAll("");
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command bukkitCommand, String label, String[] bukkitArgs) {
		try {
			// if player haven't executed /sbf command
			if (!bukkitCommand.getName().equalsIgnoreCase("sbf")) {
				int modID = 0;

				if (bukkitCommand.getName().equalsIgnoreCase("home")) {
					modID = 5;
				} else if (bukkitCommand.getName().equalsIgnoreCase("kit")) {
					modID = 1;
				} else if (bukkitCommand.getName().equalsIgnoreCase("tospawn")) {
					modID = 1;
				}

				getModule(modID).fireCommand(sender, bukkitCommand.getName(), bukkitArgs);

				return true;
			}

			boolean result = false;
			int modID = 0;

			// filtering args and executing command
			if (bukkitArgs.length > 1) {
				String[] args = new String[bukkitArgs.length - 2];
				System.arraycopy(bukkitArgs, 2, args, 0, bukkitArgs.length - 2);

				Module module = getModule(bukkitArgs[0]);
				result = module.fireCommand(sender, bukkitArgs[1], args);
				modID = module.getID();
			} else if (bukkitArgs.length == 1) {
				modID = getModule(bukkitArgs[0]).getID();
			}

			// if command execution failed
			if (modID == 0 || !result) {
				sender.sendMessage(ChatColor.GOLD + "----- " + ChatColor.AQUA + ChatColor.BOLD + "SBF Server Mod" + ChatColor.GOLD + " -----");
				sender.sendMessage("");
			}
			if (modID == 0) {
				// printing all modules
				sender.sendMessage(ChatColor.AQUA + "Модули:");

				for (Module module : modules) {
					sender.sendMessage(ChatColor.BLUE + module.getName() +
							(module.getName().equalsIgnoreCase(module.getAlias()) ? "" : ChatColor.DARK_BLUE + " (" + module.getAlias() + ")") +
							ChatColor.RED + " [ID: " + module.getID() + "]");
				}

				// additional info
				sender.sendMessage("");
				sender.sendMessage(ChatColor.AQUA + " * Для выведения помощи по модулю введите \"" + ChatColor.BLUE + "/sbf НАЗВАНИЕ_МОДУЛЯ" + ChatColor.AQUA + "\"");
			} else if (!result) {
				// printing all commands of module
				Module module = getModule(modID);

				sender.sendMessage(ChatColor.AQUA + "Комманды модуля " + ChatColor.UNDERLINE + module.getName() +
						(module.getName().equalsIgnoreCase(module.getAlias()) ? "" : ChatColor.DARK_AQUA + " (" + module.getAlias() + ")") +
						ChatColor.AQUA + ":");

				if (module.getCommands() == null) { // if module doesn't have any commands
					sender.sendMessage(ChatColor.RED + "Их нет :(");
				} else { // if has
					sender.sendMessage(ChatColor.BLUE + "/sbf " + ChatColor.BOLD + module.getName().toLowerCase() +
							(module.getName().equalsIgnoreCase(module.getAlias()) ? "" : ChatColor.DARK_BLUE + " (" + module.getAlias() + ")") +
							ChatColor.GRAY + " ...");

					for (Command command : module.getCommands()) {
						sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + command.getName() +
								(command.getName().equalsIgnoreCase(command.getAlias()) ? "" : ChatColor.DARK_BLUE + " (" + command.getAlias() + ")") +
								(command.getDescription().equals("") ? "" : ChatColor.GREEN + " - " + command.getDescription()) +
								(command.isDangerous() ? ChatColor.RED + " (!)" : ""));
					}
				}

				// additional info
				sender.sendMessage("");
				sender.sendMessage(ChatColor.AQUA + " * Комманды с знаком " + ChatColor.RED + "(!)" + ChatColor.AQUA +
						" следует использовать осторожно! А лучше вообще не трогать");
			}
			if (modID == 0 || !result) {
				// additional info
				sender.sendMessage("");
				sender.sendMessage(ChatColor.AQUA + " * Слова в скобках обозначают возможные сокращения");
				sender.sendMessage("");
			}
		} catch (Exception err) {
			sender.sendMessage(ChatColor.RED + "Вовремя запуска комманды случилась ошибка! " + err);
			DebugUtils.debugError("CORE", sender.getName() + " can't execute command " + bukkitCommand.getName(), err);
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command bukkitCommand, String alias, String[] args) {
		List<String> completions = new ArrayList<String>();

		if (bukkitCommand.getName().equalsIgnoreCase("sbf")) {
			if (args.length == 1) {
				for (Module module : modules) {
					if (GeneralUtils.startsWith(module.getName(), args[0])) {
						completions.add(module.getName());
						continue;
					}
					if (GeneralUtils.startsWith(module.getAlias(), args[0])) {
						completions.add(module.getAlias());
					}
				}
			} else if (args.length == 2) {
				Module module = getModule(args[0]);
				if (module.getCommands() != null) {
					for (Command command : module.getCommands()) {
						if (GeneralUtils.startsWith(command.getName(), args[1])) {
							completions.add(command.getName());
							continue;
						}
						if (GeneralUtils.startsWith(command.getAlias(), args[1])) {
							completions.add(command.getAlias());
						}
					}
				}
			}
		}

		if (completions.isEmpty() && args.length > 0 && !bukkitCommand.getName().equalsIgnoreCase("tospawn") && !bukkitCommand.getName().equalsIgnoreCase("kit")) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (GeneralUtils.startsWith(player.getName(), args[args.length - 1])) {
					completions.add(player.getName());
				}
			}
		}

		return completions;
	}

	private void registerAllModules() {
		modules.add(new ToolsModule());
		modules.add(new MooncraftModule());
		modules.add(new AdminModule());
		modules.add(new InventoryModule());
		modules.add(new HomesModule());
		modules.add(new PsychoModule());
		modules.add(new PrefixerModule());
		modules.add(new AnticheatModule());
		modules.add(new MessengerModule());
		// modules.add(new SbfDevSiteModule());
		// modules.add(new AchievementsModule());
		modules.add(new SecretModule());
		modules.add(new WorldSaveModule());
		// modules.add(new VanillaModule());
	}

	public static Module getModule(int id) {
		for (Module module : modules) {
			if (module.getID() == id) {
				return module;
			}
		}
		return null;
	}

	public static Module getModule(String name) {
		for (Module module : modules) {
			if (module.getName().equalsIgnoreCase(name) || module.getAlias().equalsIgnoreCase(name)) {
				return module;
			}
		}
		return null;
	}

	public static SBFPlugin getPlugin() {
		return plugin;
	}

	public static Collection<Module> getModules() {
		return modules;
	}
}
