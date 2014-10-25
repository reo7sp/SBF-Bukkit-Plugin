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

package net.sbfmc.modules.anticheat;

import java.util.logging.Level;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.server.Connection;
import net.sbfmc.server.RequestParser;

import org.bukkit.Bukkit;

public class AnticheatRequestParser extends RequestParser {
	private AnticheatModule module = (AnticheatModule) SBFPlugin.getModule(8);

	@Override
	public void parse(final Connection connection, final String key, final String value) throws Exception {
		new Thread("SBF-Anticheat") {
			@Override
			public void run() {
				// wait until player session starts
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException err) {
						break;
					}

					if (module.getPlayerSession(Bukkit.getPlayer(connection.getPlayerName())) != null) {
						break;
					}
					if (i == 24) {
						return;
					}
				}

				AnticheatPlayerSession session = (AnticheatPlayerSession) module.getPlayerSession(Bukkit.getPlayer(connection.getPlayerName()));

				if (key.equalsIgnoreCase("FILE")) {
					if (module.getAllowedFiles().contains(value)) {
					} else if (module.getDeniedFiles().contains(value)) {
						session.getDeniedFiles().add(value);
					} else {
						session.getUnknownFiles().add(value);
					}
				} else if (key.equalsIgnoreCase("FILES-END")) {
					session.setClientVerified(true);

					try {
						module.getReportsFilesConf().savePlayer(session);
					} catch (Exception err) {
						DebugUtils.debugError("ANTICHEAT", "Can't save files report of " + session.getPlayer().getName(), err);
					}

					DebugUtils.debug(
							"anticheat",
							session.getPlayer().getName() + " has " + session.getUnknownFiles().size() + " unknown files and " + session.getDeniedFiles().size() + " denied files",
							Level.INFO,
							true,
							true);
				} else if (key.equalsIgnoreCase("NICK")) {
					session.getOtherNicks().add(value);
				} else if (key.equalsIgnoreCase("NICKS-END")) {
					session.setClientVerified(true);

					try {
						module.getReportsNicksConf().savePlayer(session);
					} catch (Exception err) {
						DebugUtils.debugError("ANTICHEAT", "Can't save nicks report of " + session.getPlayer().getName(), err);
					}

					String nicks = "";
					for (String nick : session.getOtherNicks()) {
						nicks = nick + ", ";
					}
					if (nicks.endsWith(", ")) {
						nicks = nicks.substring(nicks.length() - 2, nicks.length());
					}
					DebugUtils.debug(
							"anticheat",
							session.getPlayer().getName() + " has " + session.getOtherNicks().size() + " other nicks: " + nicks,
							Level.INFO,
							true,
							true);
				}
			}
		}.start();
	}

	@Override
	public String getHeader() {
		return "ANTICHEAT";
	}

	@Override
	public int getClientSecret() {
		return 32387704;
	}

	@Override
	public int getServerSecret() {
		return 987746395;
	}
}
