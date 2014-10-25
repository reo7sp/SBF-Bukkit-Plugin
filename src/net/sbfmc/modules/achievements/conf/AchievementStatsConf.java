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

package net.sbfmc.modules.achievements.conf;

import java.io.File;
import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.conf.PlayersConf;
import net.sbfmc.modules.achievements.AchievementsModule;
import net.sbfmc.modules.achievements.AchievementsPlayerSession;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class AchievementStatsConf extends PlayersConf {
	private AchievementsModule module = (AchievementsModule) getModule();

	@Override
	public PlayerSession loadPlayer(OfflinePlayer player) throws IOException {
		return loadPlayer(new AchievementsPlayerSession(player));
	}

	@Override
	public PlayerSession loadPlayer(PlayerSession sessionRaw) throws IOException {
		AchievementsPlayerSession session = (AchievementsPlayerSession) sessionRaw;
		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		ConfigurationSection dataSection = conf.getConfigurationSection("data");
		for (String key : dataSection.getKeys(false)) {
			session.getData().put(key, dataSection.getInt(key));
		}

		for (String string : conf.getStringList("completed")) {
			session.getCompletedAchievementNames().add(string);
		}

		return session;
	}

	@Override
	public void savePlayer(OfflinePlayer player) throws IOException {
		savePlayer(module.getPlayerSession(player));
	}

	@Override
	public void savePlayer(PlayerSession sessionRaw) throws IOException {
		AchievementsPlayerSession session = (AchievementsPlayerSession) sessionRaw;
		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		for (String key : session.getData().keySet()) {
			conf.set("data." + key, session.getData().get(key));
		}

		conf.set("completed", session.getCompletedAchievementNames());

		conf.save(file);
	}

	@Override
	public void initConf() throws IOException {
		confFolder = new File(SBFPlugin.getPlugin().getDataFolder(), "achievement_stats");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFolder = null;
	}

	@Override
	public int getModuleID() {
		return 11;
	}
}
