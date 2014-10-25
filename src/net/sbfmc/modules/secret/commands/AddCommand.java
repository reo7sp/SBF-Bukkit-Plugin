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
import net.sbfmc.virtualplayers.VirtualPlayersManager;
import net.sbfmc.virtualplayers.light.LightVirtualPlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AddCommand extends Command {
	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		VirtualPlayersManager.addPlayer(new LightVirtualPlayer(args[0]));
		sender.sendMessage(ChatColor.GREEN + "Added!");
	}

	@Override
	public String getName() {
		return "add";
	}

	@Override
	public String getAlias() {
		return "a";
	}

	@Override
	public int getModuleID() {
		return 12;
	}
}