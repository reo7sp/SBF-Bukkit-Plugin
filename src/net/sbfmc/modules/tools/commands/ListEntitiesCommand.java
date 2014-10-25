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

import java.util.HashMap;

import net.sbfmc.module.commands.Command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class ListEntitiesCommand extends Command {
	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		HashMap<String, Integer> data = new HashMap<String, Integer>();
		for (World world : Bukkit.getWorlds()) {

			for (Entity entity : world.getEntities()) {
				Integer count = data.get(entity.getClass().getSimpleName());
				data.put(entity.getClass().getSimpleName(), count == null ? 1 : count + 1);
			}

			if (args.length > 0) {
				sender.sendMessage(ChatColor.GOLD + world.getName() + ":");
				for (String key : data.keySet()) {
					sender.sendMessage(ChatColor.GREEN + "   " + key + ": " + data.get(key));
				}
				data.clear();
			}
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.GOLD + "All worlds:");
			for (String key : data.keySet()) {
				sender.sendMessage(ChatColor.GREEN + "   " + key + ": " + data.get(key));
			}
		}
	}

	@Override
	public String getName() {
		return "lsent";
	}

	@Override
	public int getModuleID() {
		return 1;
	}
}
