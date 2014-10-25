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

package net.sbfmc.module.conf;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import net.sbfmc.module.PlayerSession;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public abstract class PlayersConf extends Conf {
	protected File confFolder;

	public PlayerSession loadPlayer(String name) throws IOException {
		return loadPlayer(Bukkit.getOfflinePlayer(name));
	}

	public abstract PlayerSession loadPlayer(OfflinePlayer player) throws IOException;

	public abstract PlayerSession loadPlayer(PlayerSession session) throws IOException;

	public void savePlayer(String name) throws IOException {
		savePlayer(Bukkit.getOfflinePlayer(name));
	}

	public abstract void savePlayer(OfflinePlayer player) throws IOException;

	public abstract void savePlayer(PlayerSession session) throws IOException;

	public Collection<String> getPlayerList() {
		Collection<String> playerNames = new HashSet<String>();
		String[] fileNames = confFolder.list();

		for (String fileName : fileNames) {
			if (fileName.endsWith(".yml")) {
				playerNames.add(fileName);
			}
		}

		return playerNames;
	}

	public File getConfFolder() {
		return confFolder;
	}

	@Override
	protected void createConf() throws IOException {
		if (!confFolder.exists()) {
			confFolder.mkdirs();
		}
	}
}
