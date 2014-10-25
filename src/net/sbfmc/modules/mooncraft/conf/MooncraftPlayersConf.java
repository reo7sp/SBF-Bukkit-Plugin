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

package net.sbfmc.modules.mooncraft.conf;

import java.io.File;
import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.conf.PlayersConf;
import net.sbfmc.modules.mooncraft.MooncraftModule;
import net.sbfmc.modules.mooncraft.MooncraftPlayerSession;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class MooncraftPlayersConf extends PlayersConf {
	private MooncraftModule module = (MooncraftModule) getModule();

	@Override
	public PlayerSession loadPlayer(OfflinePlayer player) throws IOException {
		MooncraftPlayerSession session = new MooncraftPlayerSession(player);
		return loadPlayer(session);
	}

	@Override
	public PlayerSession loadPlayer(PlayerSession sessionRaw) throws IOException {
		MooncraftPlayerSession session = (MooncraftPlayerSession) sessionRaw;
		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		session.setWater((float) conf.getDouble("water", 99));
		session.setFood((float) conf.getDouble("food", 99));
		session.setPower((float) conf.getDouble("power", 99));
		session.setOxygen((float) conf.getDouble("oxygen", 99));
		session.setBlood((float) conf.getDouble("blood", 99));
		session.setBleeding(conf.getInt("bleeding"));
		session.setLastStartKit(conf.getLong("lastStartKit"));

		return session;
	}

	@Override
	public void savePlayer(OfflinePlayer player) throws IOException {
		savePlayer(module.getPlayerSession(player));
	}

	@Override
	public void savePlayer(PlayerSession sessionRaw) throws IOException {
		MooncraftPlayerSession session = (MooncraftPlayerSession) sessionRaw;
		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		conf.set("water", session.getWater());
		conf.set("food", session.getFood());
		conf.set("power", session.getPower());
		conf.set("oxygen", session.getOxygen());
		conf.set("blood", session.getBlood());
		conf.set("bleeding", session.getBleeding());
		conf.set("lastStartKit", session.getLastStartKit());

		conf.save(file);
	}

	@Override
	public void initConf() throws IOException {
		confFolder = new File(SBFPlugin.getPlugin().getDataFolder(), "mooncraft_players");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFolder = null;
	}

	@Override
	public int getModuleID() {
		return 2;
	}
}
