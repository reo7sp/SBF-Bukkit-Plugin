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

import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.Module;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.modules.achievements.achievements.DiamondOreBreak1Achievement;
import net.sbfmc.modules.achievements.achievements.DiamondOreBreak2Achievement;
import net.sbfmc.modules.achievements.achievements.DiamondOreBreak3Achievement;
import net.sbfmc.modules.achievements.conf.AchievementStatsConf;
import net.sbfmc.server.SBFServer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class AchievementsModule extends Module {
	private Achievement[] achievements;
	private AchievementStatsConf achievementStatsConf;
	private AchievementRequestParser achievementRequestParser;

	@Override
	public void enable() throws Exception {
		registerAllAchievements();

		achievementStatsConf = new AchievementStatsConf();
		achievementStatsConf.initConf();

		achievementRequestParser = new AchievementRequestParser();
		SBFServer.getInstance().registerRequestParser(achievementRequestParser);

		Bukkit.getPluginManager().registerEvents(new AchievementsListener(), SBFPlugin.getPlugin());
	}

	@Override
	public void disable() throws Exception {
		achievements = null;

		SBFServer.getInstance().unregisterRequestParser(achievementRequestParser);
		achievementRequestParser = null;
	}

	private void registerAllAchievements() {
		achievements = new Achievement[] {
				new DiamondOreBreak1Achievement(),
				new DiamondOreBreak2Achievement(),
				new DiamondOreBreak3Achievement(),
		};
	}

	@Override
	protected void registerAllCommands() {
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
		playerSessions.add(achievementStatsConf.loadPlayer(player));
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
		playerSessions.remove(getPlayerSession(player));
	}

	@Override
	public int getID() {
		return 11;
	}

	@Override
	public String getName() {
		return "Achievements";
	}

	public Achievement getAchievement(String name) {
		for (Achievement achievement : achievements) {
			if (achievement.getName().equalsIgnoreCase(name)) {
				return achievement;
			}
		}
		return null;
	}

	public Achievement[] getAchievements() {
		return achievements;
	}

	public AchievementStatsConf getAchievementStatsConf() {
		return achievementStatsConf;
	}

	@Override
	public PlayerSession getPlayerSession(OfflinePlayer player) {
		PlayerSession session = super.getPlayerSession(player);
		if (session == null) {
			try {
				achievementStatsConf.loadPlayer(player);
			} catch (IOException err) {
			}
		}
		return session;
	}
}
