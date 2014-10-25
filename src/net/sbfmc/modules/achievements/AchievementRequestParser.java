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

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.server.Connection;
import net.sbfmc.server.RequestParser;

import org.bukkit.Bukkit;

public class AchievementRequestParser extends RequestParser {
	private AchievementsModule module = (AchievementsModule) SBFPlugin.getModule(11);

	@Override
	public void parse(Connection connection, String key, String value) throws Exception {
		AchievementsPlayerSession session = (AchievementsPlayerSession) module.getPlayerSession(Bukkit.getOfflinePlayer(connection.getName()));
		if (key.equalsIgnoreCase("INCREMENT")) {
			session.incrementAchievementStat(value);
		}
	}

	@Override
	public String getHeader() {
		return "ACHIEVEMENTS";
	}

	@Override
	public int getClientSecret() {
		return 283795340;
	}

	@Override
	public int getServerSecret() {
		return 216233510;
	}
}
