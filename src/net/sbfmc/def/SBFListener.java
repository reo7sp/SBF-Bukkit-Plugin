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

package net.sbfmc.def;

import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.Module;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class SBFListener implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		new Thread("login-" + event.getPlayer().getName()) {
			@Override
			public void run() {
				for (int i = 0; i < 70; i++) {
					if (!event.getPlayer().isOnline()) {
						return;
					}
					if (GeneralUtils.checkAuth(event.getPlayer())) {
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException err) {
						return;
					}
				}

				if (!GeneralUtils.checkAuth(event.getPlayer())) {
					return;
				}

				DebugUtils.debug("core", event.getPlayer().getName() +
						" joined with IP " + event.getPlayer().getAddress().getAddress().getHostAddress() +
						" in world \"" + event.getPlayer().getWorld().getName() + "\"");

				for (Module module : SBFPlugin.getModules()) {
					if (module.isEnabled()) {
						try {
							module.registerPlayer(event.getPlayer());
						} catch (Exception err) {
							DebugUtils.debugError("CORE", "Module \"" + module.getName() + "\" has an error on player join!", err);
						}
					}
				}
			}
		}.start();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		DebugUtils.debug("core", event.getPlayer().getName() +
				" left with IP " + event.getPlayer().getAddress().getAddress().getHostAddress() +
				" in world " + event.getPlayer().getWorld().getName());
		if (GeneralUtils.checkAuth(event.getPlayer())) {
			for (Module module : SBFPlugin.getModules()) {
				if (module.isEnabled()) {
					try {
						module.unregisterPlayer(event.getPlayer());
					} catch (Exception err) {
						DebugUtils.debugError("CORE", "Module \"" + module.getName() + "\" has an error on player quit!", err);
					}
				}
			}
		} else {
			DebugUtils.debug("core", event.getPlayer().getName() +
					" didn't auth!");
		}
	}
}
