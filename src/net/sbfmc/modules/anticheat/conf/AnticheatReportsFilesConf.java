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

package net.sbfmc.modules.anticheat.conf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.conf.PlayersConf;
import net.sbfmc.modules.anticheat.AnticheatModule;
import net.sbfmc.modules.anticheat.AnticheatPlayerSession;

import org.bukkit.OfflinePlayer;

public class AnticheatReportsFilesConf extends PlayersConf {
	private AnticheatModule module = (AnticheatModule) getModule();

	@Override
	public PlayerSession loadPlayer(OfflinePlayer player) throws IOException {
		return null;
	}

	@Override
	public PlayerSession loadPlayer(PlayerSession session) throws IOException {
		return null;
	}

	@Override
	public void savePlayer(OfflinePlayer player) throws IOException {
		savePlayer(module.getPlayerSession(player));
	}

	@Override
	public void savePlayer(PlayerSession sessionRaw) throws IOException {
		AnticheatPlayerSession session = (AnticheatPlayerSession) sessionRaw;

		File file = new File(confFolder, session.getPlayer().getName() + ".txt");
		BufferedWriter buffW = new BufferedWriter(new FileWriter(file, true));

		buffW.write("-----\n");
		buffW.write("ReportMode=0\n");
		buffW.write("Name=" + session.getPlayer().getName() + "\n");
		buffW.write("IP=" + session.getPlayer().getPlayer().getAddress().getAddress().getHostAddress() + "\n");
		buffW.write("Date=" + new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date()) + "\n");
		for (String s : session.getDeniedFiles()) {
			buffW.write("!!! " + s + "\n");
		}
		for (String s : session.getUnknownFiles()) {
			buffW.write(s + "\n");
		}

		buffW.close();
	}

	@Override
	public void initConf() throws IOException {
		confFolder = new File(SBFPlugin.getPlugin().getDataFolder(), "anticheat_reports");

		if (confFolder.exists() && confFolder.length() / 1024 / 1024 > 256) {
			for (File file : confFolder.listFiles()) {
				file.delete();
			}
			confFolder.delete();
		}

		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFolder = null;
	}

	@Override
	public int getModuleID() {
		return 8;
	}
}
