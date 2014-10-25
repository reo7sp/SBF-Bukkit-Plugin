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

package net.sbfmc.modules.homes;

import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.Module;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.homes.commands.HomeCommand;
import net.sbfmc.modules.homes.commands.ImportCommand;
import net.sbfmc.modules.homes.conf.HomesConf;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class HomesModule extends Module {
	private HomesConf homesConf;

	@Override
	public void enable() throws Exception {
		// initing confs
		homesConf = new HomesConf();
		homesConf.initConf();

		// registering events
		Bukkit.getPluginManager().registerEvents(new HomesListener(), SBFPlugin.getPlugin());
	}

	@Override
	public void disable() throws Exception {
		// deiniting confs
		homesConf.deinitConf();
		homesConf = null;
	}

	@Override
	protected void registerAllCommands() {
		commands = new Command[] {
				new HomeCommand(),
				new ImportCommand(),
		};
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
		playerSessions.add(homesConf.loadPlayer(player));
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
		homesConf.savePlayer(player);
		playerSessions.remove(getPlayerSession(player));
	}

	@Override
	public int getID() {
		return 5;
	}

	@Override
	public String getName() {
		return "Homes";
	}

	public HomesConf getHomesConf() {
		return homesConf;
	}

	@Override
	public PlayerSession getPlayerSession(OfflinePlayer player) {
		PlayerSession session = super.getPlayerSession(player);
		if (session == null) {
			try {
				session = homesConf.loadPlayer(player);
			} catch (IOException err) {
			}
		}
		return session;
	}
}
