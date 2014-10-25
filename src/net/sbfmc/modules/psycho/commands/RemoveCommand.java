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

package net.sbfmc.modules.psycho.commands;

import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.psycho.PsychoModule;
import net.sbfmc.modules.psycho.PsychoPlayerSession;
import net.sbfmc.modules.psycho.PsychoUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RemoveCommand extends Command {
	private PsychoModule module = (PsychoModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Нужно больше аргументов!");
			sender.sendMessage(ChatColor.RED + "/sbf psycho remove НИК");
			return;
		}

		PsychoPlayerSession session = (PsychoPlayerSession) module.getPlayerSession(Bukkit.getOfflinePlayer(args[0]));
		if (session == null) {
			sender.sendMessage(ChatColor.RED + "Такого игрока не существует!");
			return;
		}

		PsychoUtils.returnFromPsycho(session);
	}

	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public int getModuleID() {
		return 6;
	}

	@Override
	public String getDescription() {
		return "удаление игрока из психушки";
	}
}
