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

package net.sbfmc.modules.prefixer;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.Module;
import net.sbfmc.modules.prefixer.conf.PrefixerConf;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PrefixerModule extends Module {
	private PrefixerConf prefixerConf;

	@Override
	public void enable() throws Exception {
		prefixerConf = new PrefixerConf();
		Bukkit.getPluginManager().registerEvents(new PrefixerListener(), SBFPlugin.getPlugin());
	}

	@Override
	public void disable() throws Exception {
		prefixerConf = null;
	}

	@Override
	protected void registerAllCommands() {
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
		playerSessions.add(prefixerConf.loadPlayer(player));
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
		playerSessions.remove(getPlayerSession(player));
	}

	@Override
	public int getID() {
		return 7;
	}

	@Override
	public String getName() {
		return "Prefixer";
	}
}
