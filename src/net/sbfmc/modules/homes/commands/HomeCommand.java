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

package net.sbfmc.modules.homes.commands;

import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.homes.HomesModule;
import net.sbfmc.modules.homes.HomesPlayerSession;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand extends Command {
	private HomesModule module = (HomesModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (sender instanceof Player) {
			HomesPlayerSession session = (HomesPlayerSession) module.getPlayerSession((Player) sender);

			if (args.length < 1) {
				boolean result = session.teleport();

				if (result) {
					sender.sendMessage(ChatColor.GREEN + "Вы телепортированны домой!");
				} else {
					sender.sendMessage(ChatColor.RED + "Точка дома не существует в этом мире!");
				}

				return;
			}
			if (args[0].equalsIgnoreCase("set")) {
				session.addLocation(session.getPlayer().getPlayer().getLocation());

				Location location = session.getLocation(session.getPlayer().getPlayer().getWorld().getName());
				if (location != null) {
					session.getPlayer().getPlayer().setCompassTarget(location);
				}

				module.getHomesConf().savePlayer(session);

				sender.sendMessage(ChatColor.GREEN + "Точка дома в этом мире установлена!");
			} else if (args[0].equalsIgnoreCase("remove")) {
				session.removeLocation(session.getPlayer().getPlayer().getWorld().getName());

				Location location = session.getPlayer().getPlayer().getWorld().getSpawnLocation();
				if (location != null) {
					session.getPlayer().getPlayer().setCompassTarget(location);
				}

				module.getHomesConf().savePlayer(session);

				sender.sendMessage(ChatColor.GREEN + "Точка дома в этом мире удалена!");
			} else if (args[0].equalsIgnoreCase("invite")) {
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Нужно больше аргументов!");
					sender.sendMessage(ChatColor.RED + "/home invite НИК");
					return;
				}

				session.getInvitedPlayers().add(args[1]);

				module.getHomesConf().savePlayer(session);

				Player invitedPlayer = Bukkit.getPlayer(args[1]);
				if (invitedPlayer != null) {
					invitedPlayer.sendMessage(ChatColor.GREEN + "Вы приглашены в дом " + session.getPlayer().getName() + "!");
					invitedPlayer.sendMessage(ChatColor.GREEN + "Чтобы попасть в его дом введите " + ChatColor.AQUA + "/home " + session.getPlayer().getName());
				}

				sender.sendMessage(ChatColor.GREEN + args[1] + " приглашён в ваш дом!");
			} else if (args[0].equalsIgnoreCase("uninvite")) {
				if (args.length < 2) {
					sender.sendMessage(ChatColor.RED + "Нужно больше аргументов!");
					sender.sendMessage(ChatColor.RED + "/home uninvite НИК");
					return;
				}

				session.getInvitedPlayers().remove(args[1]);

				module.getHomesConf().savePlayer(session);

				sender.sendMessage(ChatColor.GREEN + args[1] + " выгнан из вашего дома!");

				Player invitedPlayer = Bukkit.getPlayer(args[1]);
				if (invitedPlayer != null) {
					invitedPlayer.sendMessage(ChatColor.RED + "Вы выгнаны из дома " + session.getPlayer().getName() + " :(");
				}
			} else if (args[0].equalsIgnoreCase("public")) {
				session.setPublic(true);

				module.getHomesConf().savePlayer(session);

				sender.sendMessage(ChatColor.GREEN + "В ваш дом могут телепортироваться все!");
			} else if (args[0].equalsIgnoreCase("private")) {
				session.setPublic(false);

				module.getHomesConf().savePlayer(session);

				sender.sendMessage(ChatColor.GREEN + "В ваш дом могут телепортироваться только те, кого вы пригласили!");
			} else if (args[0].equalsIgnoreCase("help")) {
				sender.sendMessage(ChatColor.BLUE + "/home " + ChatColor.GRAY + "...");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "НИК");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "НИК МИР");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "set");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "remove");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "invite");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "uninvite");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "public");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "private");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "help");
			} else {
				String worldName;
				if (args.length > 1) {
					worldName = args[1];
				} else {
					worldName = session.getPlayer().getPlayer().getWorld().getName();
				}

				HomesPlayerSession anotherPlayerSession = (HomesPlayerSession) module.getPlayerSession(Bukkit.getOfflinePlayer(args[0]));

				if (session.getPlayer().getName().equals(anotherPlayerSession.getPlayer().getName()) ||
						module.checkPermisson(sender, "teleportToAll") ||
						anotherPlayerSession.isPublic() ||
						anotherPlayerSession.getInvitedPlayers().contains(session.getPlayer().getName())) {
					Location to = anotherPlayerSession.getLocation(worldName);
					if (to != null) {
						session.getPlayer().getPlayer().teleport(to);
						sender.sendMessage(ChatColor.GREEN + "Вы дома у " + args[0]);
					} else {
						sender.sendMessage(ChatColor.RED + "У " + args[0] + " нет дома в мире " + worldName + " :(");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Вы не приглашены!");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Вы не игрок!");
		}
	}

	@Override
	public String getName() {
		return "home";
	}

	@Override
	public int getModuleID() {
		return 5;
	}

	@Override
	public String getDescription() {
		return "управление точками домов";
	}
}
