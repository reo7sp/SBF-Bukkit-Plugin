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

package net.sbfmc.modules.vanilla;

import net.sbfmc.module.ModuleIntegration;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class VanillaListener extends ModuleIntegration implements Listener {
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (event.getPlayer().getWorld().getName().startsWith("Vanilla") && !event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			String command = event.getMessage().substring(event.getMessage().startsWith("/") ? 1 : 0, event.getMessage().indexOf(" "));
			for (String allowedCommand : VanillaModule.ALLOWED_COMMANDS) {
				if (command.equals(allowedCommand)) {
					return;
				}
			}
			event.getPlayer().sendMessage(ChatColor.RED + "Ты на ванильном сервере. Тут нет таких комманд!");
			event.setCancelled(true);
		}
	}

	@Override
	public int getModuleID() {
		return 14;
	}
}
