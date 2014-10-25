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

package net.sbfmc.modules.prefixer.conf;

import java.io.File;
import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.conf.PlayersConf;
import net.sbfmc.modules.prefixer.PrefixerPlayerSession;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class PrefixerConf extends PlayersConf {
	@Override
	public PlayerSession loadPlayer(OfflinePlayer player) throws IOException {
		return loadPlayer(new PrefixerPlayerSession(player));
	}

	@Override
	public PlayerSession loadPlayer(PlayerSession sessionRaw) throws IOException {
		PrefixerPlayerSession session = (PrefixerPlayerSession) sessionRaw;
		File file = new File(confFolder, session.getPlayer().getName());
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		session.setCustomPrefix(conf.getString("prefix"));

		return session;
	}

	@Override
	public void savePlayer(OfflinePlayer player) throws IOException {
	}

	@Override
	public void savePlayer(PlayerSession session) throws IOException {
	}

	@Override
	public void initConf() throws IOException {
		confFolder = new File(SBFPlugin.getPlugin().getDataFolder(), "prefixer");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFolder = null;
	}

	@Override
	public int getModuleID() {
		return 7;
	}
}
