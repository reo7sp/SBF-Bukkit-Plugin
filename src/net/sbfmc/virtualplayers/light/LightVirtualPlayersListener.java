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

package net.sbfmc.virtualplayers.light;

import net.minecraft.server.v1_5_R3.Packet201PlayerInfo;
import net.sbfmc.def.SBFPlugin;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.virtualplayers.VirtualPlayer;
import net.sbfmc.virtualplayers.VirtualPlayersManager;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.injector.GamePhase;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.reflect.StructureModifier;

public class LightVirtualPlayersListener extends PacketAdapter implements Listener {
	public LightVirtualPlayersListener() {
		super(SBFPlugin.getPlugin(), ConnectionSide.SERVER_SIDE, ListenerPriority.HIGHEST, GamePhase.LOGIN, Packets.Server.KICK_DISCONNECT);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		for (VirtualPlayer player : VirtualPlayersManager.getPlayers()) {
			((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(new Packet201PlayerInfo(player.getName(), true, GeneralUtils.getRandom().nextInt(490) + 10));
		}
	}

	@Override
	public void onPacketSending(PacketEvent event) {
		try {
			final StructureModifier<String> stringModifier = event.getPacket().getSpecificModifier(String.class);
			final String replyString = stringModifier.read(0);
			int offset = 0;
			String splitter = String.valueOf(ChatColor.COLOR_CHAR); // 1.3 and earlier
			if (replyString.startsWith(splitter)) { // 1.4 and onward
				splitter = "\u0000";
				offset = 3;
			}
			final String[] split = replyString.split(splitter);
			if (split.length == (3 + offset)) {
				int online;
				try {
					online = Integer.parseInt(split[1 + offset]);
				} catch (final NumberFormatException e) {
					return;
				}
				online += VirtualPlayersManager.getPlayersCount();
				final StringBuilder builder = new StringBuilder();
				for (int x = 0; x < split.length; x++) {
					if (builder.length() > 0) {
						builder.append(splitter);
					}
					if (x == (1 + offset)) {
						builder.append(online);
						continue;
					}
					builder.append(split[x]);
				}
				stringModifier.write(0, builder.toString());
			}
		} catch (final FieldAccessException e) {
		}
	}
}
