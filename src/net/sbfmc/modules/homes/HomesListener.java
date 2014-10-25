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

package net.sbfmc.modules.homes;

import net.sbfmc.module.ModuleIntegration;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class HomesListener extends ModuleIntegration implements Listener {
	private HomesModule module = (HomesModule) getModule();

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		((HomesPlayerSession) module.getPlayerSession(event.getPlayer())).teleport();
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		HomesPlayerSession session = (HomesPlayerSession) module.getPlayerSession(event.getPlayer());

		if (session == null) {
			return;
		}

		Location location = session.getLocation(session.getPlayer().getPlayer().getWorld().getName());
		if (location != null) {
			session.getPlayer().getPlayer().setCompassTarget(location);
		} else {
			session.getPlayer().getPlayer().setCompassTarget(session.getPlayer().getPlayer().getWorld().getSpawnLocation());
		}
	}

	@Override
	public int getModuleID() {
		return 5;
	}
}
