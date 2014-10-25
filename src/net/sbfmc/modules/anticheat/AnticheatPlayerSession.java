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

import java.util.Collection;
import java.util.HashSet;

import net.sbfmc.module.PlayerSession;

import org.bukkit.OfflinePlayer;

public class AnticheatPlayerSession extends PlayerSession {
	private boolean clientVerified;
	private Collection<String> deniedFiles = new HashSet<String>();
	private Collection<String> unknownFiles = new HashSet<String>();
	private Collection<String> otherNicks = new HashSet<String>();

	public AnticheatPlayerSession(OfflinePlayer player) {
		super(player);
	}

	@Override
	public int getModuleID() {
		return 8;
	}

	public boolean isClientVerified() {
		return clientVerified;
	}

	public void setClientVerified(boolean clientVerified) {
		this.clientVerified = clientVerified;
	}

	public Collection<String> getDeniedFiles() {
		return deniedFiles;
	}

	public Collection<String> getUnknownFiles() {
		return unknownFiles;
	}

	public Collection<String> getOtherNicks() {
		return otherNicks;
	}
}
