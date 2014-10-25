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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TNTCommand extends Command {
	private ToolsModule module = (ToolsModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (args.length < 1) {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE + "/sbf tools tnt " + ChatColor.GRAY + "...");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "noTntForAll " + ChatColor.DARK_BLUE + "(n)");
			return;
		}
		if (args[0].equalsIgnoreCase("noTntForAll") || args[0].equalsIgnoreCase("n")) {
			if (!module.checkPermisson(sender, getName() + ".noTntForAll", false)) {
				return;
			}

			if (module.isNoTntForAll()) {
				module.setNoTntForAll(false);
				sender.sendMessage(ChatColor.GREEN + "Только админы и модераторы могут использовать TNT!");
			} else {
				module.setNoTntForAll(true);
				sender.sendMessage(ChatColor.RED + "Никто не может использовать TNT!");
			}
		} else {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE + "/sbf tools tnt " + ChatColor.GRAY + "...");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "noTntForAll " + ChatColor.DARK_BLUE + "(n)");
		}
	}

	@Override
	public String getName() {
		return "tnt";
	}

	@Override
	public int getModuleID() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "управление TNT";
	}
}
