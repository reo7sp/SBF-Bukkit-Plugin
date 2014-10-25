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

import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.commands.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ToggleDebugCommand extends Command {
	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		DebugUtils.setDebug(!DebugUtils.isDebug());
		sender.sendMessage(ChatColor.GREEN + "Режим отладки в" + (DebugUtils.isDebug() ? "" : "ы") + "ключен");
	}

	@Override
	public String getName() {
		return "toggledebug";
	}

	@Override
	public String getAlias() {
		return "chdbg";
	}

	@Override
	public int getModuleID() {
		return 3;
	}

	@Override
	public String getDescription() {
		return "переключение режима отладки";
	}
}
