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

package net.sbfmc.modules.psycho.conf;

import java.io.File;
import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.conf.PlayersConf;
import net.sbfmc.modules.psycho.PsychoModule;
import net.sbfmc.modules.psycho.PsychoPlayerSession;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class PsychoConf extends PlayersConf {
	private PsychoModule module = (PsychoModule) getModule();

	@Override
	public PlayerSession loadPlayer(OfflinePlayer player) throws IOException {
		return loadPlayer(new PsychoPlayerSession(player));
	}

	@Override
	public PlayerSession loadPlayer(PlayerSession sessionRaw) throws IOException {
		PsychoPlayerSession session = (PsychoPlayerSession) sessionRaw;
		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		session.setInPsycho(conf.getBoolean("inPsycho"));
		session.setInPsychoTime(conf.getLong("inPsychoTime"));
		session.setPsychoTime(conf.getLong("psychoTime"));

		return session;
	}

	@Override
	public void savePlayer(OfflinePlayer player) throws IOException {
		savePlayer(module.getPlayerSession(player));
	}

	@Override
	public void savePlayer(PlayerSession sessionRaw) throws IOException {
		PsychoPlayerSession session = (PsychoPlayerSession) sessionRaw;
		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		conf.set("inPsycho", session.isInPsycho());
		conf.set("inPsychoTime", session.getInPsychoTime());
		conf.set("psychoTime", session.getPsychoTime());

		conf.save(file);
	}

	@Override
	public void initConf() throws IOException {
		confFolder = new File(SBFPlugin.getPlugin().getDataFolder(), "psycho");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFolder = null;
	}

	@Override
	public int getModuleID() {
		return 6;
	}
}
