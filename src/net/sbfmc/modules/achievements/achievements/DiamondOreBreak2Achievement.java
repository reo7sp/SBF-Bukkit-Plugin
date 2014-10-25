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

package net.sbfmc.modules.achievements.achievements;

import java.util.Map;

import net.sbfmc.modules.achievements.Achievement;
import net.sbfmc.modules.achievements.AchievementsPlayerSession;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class DiamondOreBreak2Achievement extends Achievement {
	@Override
	public void fireBukkitEvent(AchievementsPlayerSession session, Event eventRaw) {
		if (eventRaw instanceof BlockBreakEvent) {
			BlockBreakEvent event = (BlockBreakEvent) eventRaw;
			if (event.getBlock().getTypeId() == 56) {
				fireEvent(session, "diamond_ore_break", null);
			}
		}
	}

	@Override
	public void fireEvent(AchievementsPlayerSession session, String name, Map<String, Object> args) {
		if (name.equals("diamond_ore_break")) {
			session.incrementAchievementStat(getName());
		}
	}

	@Override
	public String getHumanName() {
		return "Алмазный магнат";
	}

	@Override
	public String getDescription() {
		return "Добыто было 500 алмазов";
	}

	@Override
	public int getMaxPoints() {
		return 500;
	}
}
