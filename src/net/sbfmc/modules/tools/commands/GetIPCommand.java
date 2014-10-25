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

import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.tools.ToolsModule;
import net.sbfmc.modules.tools.ToolsPlayerSession;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class GetIPCommand extends Command {
	private ToolsModule module = (ToolsModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: /sbf tools getIP ИГРОК");
			return;
		}

		for (PlayerSession sessionRaw : module.getPlayerSessions()) {
			ToolsPlayerSession session = (ToolsPlayerSession) sessionRaw;
			if (sessionRaw.getPlayer().getName().equalsIgnoreCase(args[0])) {
				sender.sendMessage(ChatColor.BLUE + "IP " + args[0] + " " + session.getIP());
				return;
			}
		}

		sender.sendMessage(ChatColor.RED + "IP " + args[0] + " не найден");
	}

	@Override
	public String getName() {
		return "getip";
	}

	@Override
	public String getAlias() {
		return "ip";
	}

	@Override
	public int getModuleID() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "получение IP игрока";
	}
}
