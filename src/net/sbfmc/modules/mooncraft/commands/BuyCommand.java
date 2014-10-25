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
import net.sbfmc.modules.mooncraft.economic.Shop;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class BuyCommand extends Command {
	private MooncraftModule module = (MooncraftModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (args.length > 1) {
			MooncraftPlayerSession session = null;

			for (PlayerSession sessionRaw : module.getPlayerSessions()) {
				if (args[0].equalsIgnoreCase(sessionRaw.getPlayer().getName())) {
					session = (MooncraftPlayerSession) sessionRaw;
					break;
				}
			}

			if (session == null) {
				sender.sendMessage(ChatColor.RED + "Игрок не в сети!");
				return;
			}

			if (args.length > 4) {
				Shop.buyItem(
						session,
						Integer.parseInt(args[1]),
						Byte.parseByte(args[2]),
						Integer.parseInt(args[3]),
						Integer.parseInt(args[4]));
			} else {
				Shop.buy(session, args[1]);
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Нужно больше аргументов!");
			sender.sendMessage(ChatColor.RED + "/sbf mooncraft (moon) buy ИГРОК ПРЕДМЕТ");
			sender.sendMessage(ChatColor.RED + "/sbf mooncraft (moon) buy ИГРОК ID DATA КОЛ-ВО ЦЕНА");
		}
	}

	@Override
	public String getName() {
		return "buy";
	}

	@Override
	public int getModuleID() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "покупка";
	}
}
