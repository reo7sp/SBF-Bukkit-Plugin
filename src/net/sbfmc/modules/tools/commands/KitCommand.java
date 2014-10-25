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

package net.sbfmc.modules.tools.commands;

import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.tools.ToolsModule;
import net.sbfmc.modules.tools.ToolsPlayerSession;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitCommand extends Command {
	private ToolsModule module = (ToolsModule) getModule();

	@SuppressWarnings("deprecation")
	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (sender instanceof Player) {
			if (args.length > 0) {
				ToolsPlayerSession session = (ToolsPlayerSession) module.getPlayerSession((Player) sender);

				if (args[0].equalsIgnoreCase("start")) {
					if (session.isStartKitUsed()) {
						session.getPlayer().getPlayer().sendMessage(ChatColor.RED + "Вы уже получали предметы!");
						return;
					}
					if (!session.getPlayer().getPlayer().getWorld().getName().startsWith("Classic")) {
						session.getPlayer().getPlayer().sendMessage(ChatColor.RED + "Вы не на сервере Classic!");
						return;
					}

					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(268));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(258));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(270));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(269));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(50, 8));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(5, 16));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(357, 16));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(351, 8, (short) 0, (byte) 3));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(339, 4));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(335));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(344));

					session.setStartKitUsed(true);

					module.getKitConf().savePlayer(session);
				} else if (args[0].equalsIgnoreCase("gift")) {
					if (session.isGiftKitUsed()) {
						session.getPlayer().getPlayer().sendMessage(ChatColor.RED + "Вы уже получали предметы!");
						return;
					}
					if (System.currentTimeMillis() - session.getCreateTime() < 10800000) { // 10800000 = 1000 * 60 * 60 * 3 (3 hours)
						session.getPlayer().getPlayer().sendMessage(ChatColor.RED + "Время ещё не истекло!");
						return;
					}

					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(264, 2));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(266, 4));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(263, 16));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(285));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(283));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(307));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(301));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(386));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(390));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(314));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(261));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(262, 16));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(269));
					session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(383, 1, (short) 0, (byte) 91));

					session.setGiftKitUsed(true);

					module.getKitConf().savePlayer(session);
				} else {
					sender.sendMessage(ChatColor.BLUE + "/kit " + ChatColor.GRAY + "...");
					sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "start");
					sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "gift");
				}
			} else {
				sender.sendMessage(ChatColor.BLUE + "/kit " + ChatColor.GRAY + "...");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "start");
				sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "gift");
			}
		} else {
			sender.sendMessage("Вы не игрок!");
		}
	}

	@Override
	public String getName() {
		return "kit";
	}

	@Override
	public int getModuleID() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "получение набора предметов";
	}
}
