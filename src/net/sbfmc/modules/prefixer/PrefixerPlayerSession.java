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
import net.sbfmc.module.PlayerSession;
import net.sbfmc.modules.achievements.AchievementsPlayerSession;
import net.sbfmc.modules.anticheat.AnticheatPlayerSession;

import org.bukkit.OfflinePlayer;

public class PrefixerPlayerSession extends PlayerSession {
	private PrefixerModule module = (PrefixerModule) getModule();
	private String prefix;

	public PrefixerPlayerSession(OfflinePlayer player) {
		super(player);
	}

	@Override
	public int getModuleID() {
		return 7;
	}

	public int getGroup() {
		if (module.checkPermisson(player.getPlayer(), "adminPrefix")) {
			return 3;
		} else if (module.checkPermisson(player.getPlayer(), "moderatorPrefix")) {
			return 2;
		} else if (module.checkPermisson(player.getPlayer(), "friendPrefix")) {
			return 1;
		} else {
			return 0;
		}
	}

	public String getCustomPrefix() {
		return prefix;
	}

	public void setCustomPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isDonater() {
		return false; // TODO integration
	}

	public boolean hasSBFClient() {
		try {
			return ((AnticheatPlayerSession) SBFPlugin.getModule(8).getPlayerSession(player)).isClientVerified();
		} catch (Exception err) {
			return false;
		}
	}

	public int getNumberOfAchievements() {
		try {
			return ((AchievementsPlayerSession) SBFPlugin.getModule(11).getPlayerSession(player)).getCompletedAchievements();
		} catch (Exception err) {
			return 0;
		}
	}
}
