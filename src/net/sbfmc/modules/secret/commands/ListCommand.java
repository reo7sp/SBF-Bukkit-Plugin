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

package net.sbfmc.modules.secret.commands;

import net.sbfmc.module.commands.Command;
import net.sbfmc.virtualplayers.VirtualPlayer;
import net.sbfmc.virtualplayers.VirtualPlayersManager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ListCommand extends Command {
	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		sender.sendMessage(ChatColor.GOLD + "" + VirtualPlayersManager.getPlayersCount());
		for (VirtualPlayer player : VirtualPlayersManager.getPlayers()) {
			sender.sendMessage(ChatColor.GREEN + player.getName());
		}
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public String getAlias() {
		return "ls";
	}

	@Override
	public int getModuleID() {
		return 12;
	}
}
