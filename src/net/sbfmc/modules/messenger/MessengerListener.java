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

package net.sbfmc.modules.messenger;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.PlayerSession;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

public class MessengerListener implements PacketListener {
	@Override
	public Plugin getPlugin() {
		return SBFPlugin.getPlugin();
	}

	@Override
	public ListeningWhitelist getReceivingWhitelist() {
		return ListeningWhitelist.EMPTY_WHITELIST;
	}

	@Override
	public ListeningWhitelist getSendingWhitelist() {
		return ListeningWhitelist.EMPTY_WHITELIST;
	}

	@Override
	public void onPacketReceiving(PacketEvent event) {
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		if (event.getPacketID() == 3) {
			if (event.getPlayer().getName().equals("nobody")) {
				for (PlayerSession sessionRaw : SBFPlugin.getModule(9).getPlayerSessions()) {
					MessengerPlayerSession session = (MessengerPlayerSession) sessionRaw;
					session.getConnection().sendResponse("CHAT " + event.getPacket().getStrings().read(0));
				}
			}
		}
	}
}
