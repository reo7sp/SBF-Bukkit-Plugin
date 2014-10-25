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

package net.sbfmc.modules.admin.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.Module;
import net.sbfmc.module.commands.Command;
import net.sbfmc.world.GenerationQueue;
import net.sbfmc.world.GenerationQueue.GenerationQueueElement;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GenerationQueueCommand extends Command {
	@SuppressWarnings("deprecation")
	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.BLUE + "/sbf tools genQueue " + ChatColor.GRAY + "...");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "flush");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "flush ID_МОДА");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "status");
			return;
		}

		if (args[0].equalsIgnoreCase("flush")) {
			if (args.length > 1) {
				int modID;
				try {
					modID = Integer.parseInt(args[1]);

					Iterator<GenerationQueueElement> iterator = GenerationQueue.getChunks().iterator();
					while (iterator.hasNext()) {
						GenerationQueueElement element = iterator.next();
						if (element.getGenerator().getModuleID() == modID) {
							iterator.remove();
						}
					}

					sender.sendMessage(ChatColor.GREEN + "Очередь на генерацию модуля " + ChatColor.UNDERLINE + SBFPlugin.getModule(modID).getName() + ChatColor.RESET + " очищена!");
					return;
				} catch (Exception err) {
				}
			}
			GenerationQueue.getChunks().clear();
			sender.sendMessage(ChatColor.GREEN + "Очередь на генерацию очищена!");
		} else if (args[0].equalsIgnoreCase("status")) {
			sender.sendMessage(ChatColor.GOLD + "CPS: " + GenerationQueue.getCPS());
			sender.sendMessage(ChatColor.GOLD + "Перегенерировано " + GenerationQueue.getGCPS() + " чанков");
			sender.sendMessage(ChatColor.GOLD + "Загружено " + GenerationQueue.getLCPS() + " чанков");
			sender.sendMessage(ChatColor.GREEN + "Сейчас в очереди " + GenerationQueue.getChunks().size() + " чанков");

			HashMap<Module, Integer> queues = new HashMap<Module, Integer>();
			for (GenerationQueueElement element : GenerationQueue.getChunks()) {
				Module module = element.getGenerator().getModule();
				if (!queues.containsKey(module)) {
					queues.put(module, 0);
				}
				queues.put(module, queues.get(module) + 1);
			}

			Set<Module> modQueues = queues.keySet();
			for (Module module : modQueues) {
				sender.sendMessage(ChatColor.GREEN + "" + queues.get(module) + " чанков от модуля " + ChatColor.UNDERLINE + module.getName());
			}
		} else {
			sender.sendMessage(ChatColor.BLUE + "/sbf tools genQueue " + ChatColor.GRAY + "...");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "flush");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "flush ID_МОДУЛЯ");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "status");
		}
	}

	@Override
	public String getName() {
		return "genQueue";
	}

	@Override
	public String getAlias() {
		return "gq";
	}

	@Override
	public int getModuleID() {
		return 3;
	}

	@Override
	public String getDescription() {
		return "управление очередью генераций";
	}
}
