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

package net.sbfmc.modules.mooncraft.commands;

import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.mooncraft.MooncraftModule;
import net.sbfmc.modules.mooncraft.MooncraftPlayerSession;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class StartKitCommand extends Command {
	private MooncraftModule module = (MooncraftModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		MooncraftPlayerSession session = null;

		for (PlayerSession sessionRaw : module.getPlayerSessions()) {
			if (args[0].equalsIgnoreCase(sessionRaw.getPlayer().getName())) {
				session = (MooncraftPlayerSession) sessionRaw;
				break;
			}
		}

		if (System.currentTimeMillis() - session.getLastStartKit() < 86400000) { // 86400000 = 1000 * 60 * 60 * 24
			session.getPlayer().getPlayer().sendMessage(ChatColor.RED + "Вы сегодня уже получали предметы!");
			return;
		}

		session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(17, 5));
		session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(299));
		session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(298));
		session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(270));
		session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(268));
		session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(269));
		session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(50, 8));
		session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(371, 8));
		session.getPlayer().getPlayer().sendMessage(ChatColor.RED + "Не забудь надеть броню!");

		session.setLastStartKit(System.currentTimeMillis());

	}

	@Override
	public String getName() {
		return "startkit";
	}

	@Override
	public int getModuleID() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "получение старт кита";
	}
}
