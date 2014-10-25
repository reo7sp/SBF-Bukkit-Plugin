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

package net.sbfmc.modules.admin;

import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.ModuleIntegration;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class AdminListener extends ModuleIntegration implements Listener {
	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!event.getMessage().startsWith("/login ") &&
				!event.getMessage().startsWith("/register ") &&
				!event.getMessage().startsWith("/changepassword ") &&
				!event.getMessage().startsWith("/l ") &&
				!event.getMessage().startsWith("/reg ")) {
			DebugUtils.debug("core", event.getPlayer().getName() + " performed command \"" + event.getMessage() + "\"!", false);
		}
	}

	@Override
	public int getModuleID() {
		return 3;
	}
}
