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

package net.sbfmc.modules.achievements;

import net.sbfmc.module.ModuleIntegration;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

public class AchievementsListener extends ModuleIntegration implements Listener {
	private AchievementsModule module = (AchievementsModule) getModule();

	@EventHandler
	public void onEvent(Event event) {
		if (event instanceof EntityEvent || event instanceof PlayerEvent) {
			if (event instanceof EntityEvent && ((EntityEvent) event).getEntity() instanceof Player == false) {
				return;
			}

			AchievementsPlayerSession session = (AchievementsPlayerSession) module.getPlayerSession(
					event instanceof EntityEvent ? (Player) ((EntityEvent) event).getEntity() : ((PlayerEvent) event).getPlayer());

			for (Achievement achievement : module.getAchievements()) {
				if (!session.isCompletedAchievement(achievement)) {
					achievement.fireBukkitEvent(session, event);
				}
			}
		}
	}

	@Override
	public int getModuleID() {
		return 11;
	}
}
