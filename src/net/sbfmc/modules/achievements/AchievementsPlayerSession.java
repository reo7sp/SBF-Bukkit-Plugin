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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.PlayerSession;

import org.bukkit.OfflinePlayer;

public class AchievementsPlayerSession extends PlayerSession {
	private AchievementsModule module = (AchievementsModule) getModule();

	private Map<String, Integer> data = new HashMap<String, Integer>();
	private Collection<String> completedAchievements = new HashSet<String>();

	public AchievementsPlayerSession(OfflinePlayer player) {
		super(player);
	}

	public void achievementUnlock(Achievement achievement) {
		achievementUnlock(achievement.getName());
	}

	public void achievementUnlock(String name) {
		try {
			completedAchievements.add(name);
			module.getAchievementStatsConf().savePlayer(this);
		} catch (Exception err) {
			DebugUtils.debugError("ACHIEVEMENTS", player.getName() + "can't unlock achievement \"" + name + "\"", err);
		}
	}

	public void clearAchievementStat(String name) {
		data.remove(name);
	}

	public void incrementAchievementStat(String name) {
		data.put(name, data.containsKey(name) ? data.get(name) + 1 : 1);
		try {
			if (data.get(name) > module.getAchievement(name).getMaxPoints()) {
				achievementUnlock(name);
			}
		} catch (NullPointerException err) {
		}
	}

	public int getAchievementStat(String name) {
		return data.get(name);
	}

	@Override
	public int getModuleID() {
		return 11;
	}

	public Collection<String> getCompletedAchievementNames() {
		return completedAchievements;
	}

	public int getCompletedAchievements() {
		return completedAchievements.size();
	}

	public boolean isCompletedAchievement(Achievement achievement) {
		return isCompletedAchievement(achievement.getName());
	}

	public boolean isCompletedAchievement(String name) {
		return completedAchievements.contains(name);
	}

	public Map<String, Integer> getData() {
		return data;
	}
}
