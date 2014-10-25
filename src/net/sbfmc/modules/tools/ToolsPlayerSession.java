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

package net.sbfmc.modules.tools;

import net.sbfmc.module.PlayerSession;

import org.bukkit.OfflinePlayer;

public class ToolsPlayerSession extends PlayerSession {
	private String ip;
	private long eatTomatoTime;
	private boolean startKitUsed;
	private boolean giftKitUsed;

	public ToolsPlayerSession(OfflinePlayer player) {
		super(player);
	}

	public ToolsPlayerSession(OfflinePlayer player, String ip) {
		super(player);
		this.ip = ip;
	}

	@Override
	public int getModuleID() {
		return 1;
	}

	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public long getEatTomatoTime() {
		return eatTomatoTime;
	}

	public void setEatTomatoTime(long eatTomatoTime) {
		this.eatTomatoTime = eatTomatoTime;
	}

	public boolean isStartKitUsed() {
		return startKitUsed;
	}

	public void setStartKitUsed(boolean startKitUsed) {
		this.startKitUsed = startKitUsed;
	}

	public boolean isGiftKitUsed() {
		return giftKitUsed;
	}

	public void setGiftKitUsed(boolean giftKitUsed) {
		this.giftKitUsed = giftKitUsed;
	}
}
