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

import net.sbfmc.module.Module;
import net.sbfmc.server.SBFServer;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;

public class MessengerModule extends Module {
	private MessengerRequestParser messengerRequestParser;
	private MessengerListener messengerListener;

	@Override
	public void enable() throws Exception {
		if (GeneralUtils.isPluginEnabled("ProtocolLib")) {
			messengerRequestParser = new MessengerRequestParser();
			SBFServer.getInstance().registerRequestParser(messengerRequestParser);

			messengerListener = new MessengerListener();
			ProtocolLibrary.getProtocolManager().addPacketListener(messengerListener);
		}
	}

	@Override
	public void disable() throws Exception {
		if (GeneralUtils.isPluginEnabled("ProtocolLib")) {
			SBFServer.getInstance().unregisterRequestParser(messengerRequestParser);
			messengerRequestParser = null;

			ProtocolLibrary.getProtocolManager().removePacketListener(messengerListener);
			messengerListener = null;
		}
	}

	@Override
	protected void registerAllCommands() {
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
	}

	@Override
	public int getID() {
		return 9;
	}

	@Override
	public String getName() {
		return "Messenger";
	}
}
