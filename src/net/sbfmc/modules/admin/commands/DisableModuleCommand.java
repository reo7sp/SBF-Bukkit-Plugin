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

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.Module;
import net.sbfmc.module.commands.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DisableModuleCommand extends Command {
	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (args.length > 0) {
			Module module = null;
			try {
				module = SBFPlugin.getModule(Integer.parseInt(args[0]));
			} catch (Exception err) {
				module = SBFPlugin.getModule(args[0]);
			}
			if (module == null) {
				sender.sendMessage(ChatColor.RED + "Модуль не найден");
				return;
			}
			module.setEnabled(false);
		} else {
			sender.sendMessage(ChatColor.RED + "Нужно больше аргументов!");
			sender.sendMessage(ChatColor.RED + "/sbf admin disablemodule (modoff) ID_МОДУЛЯ");
			sender.sendMessage(ChatColor.RED + "/sbf admin disablemodule (modoff) ИМЯ_МОДУЛЯ");
		}
	}

	@Override
	public String getName() {
		return "disablemodule";
	}

	@Override
	public String getAlias() {
		return "modoff";
	}

	@Override
	public int getModuleID() {
		return 3;
	}

	@Override
	public boolean isDangerous() {
		return true;
	}

	@Override
	public String getDescription() {
		return "отключение модулей";
	}
}
