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

package net.sbfmc.module;

import org.bukkit.OfflinePlayer;

public abstract class PlayerSession extends ModuleIntegration {
	protected final long CREATE_TIME = System.currentTimeMillis();
	protected OfflinePlayer player;

	public PlayerSession(OfflinePlayer player) {
		this.player = player;
	}

	public OfflinePlayer getPlayer() {
		return player;
	}

	public long getCreateTime() {
		return CREATE_TIME;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		result = (int) (prime * result + CREATE_TIME);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PlayerSession other = (PlayerSession) obj;
		if (player == null) {
			if (other.player != null) {
				return false;
			}
		} else if (!player.equals(other.player)) {
			return false;
		} else if (other.getCreateTime() != CREATE_TIME) {
			return false;
		} else if (other.getModuleID() != getModuleID()) {
			return false;
		}
		return true;
	}
}
