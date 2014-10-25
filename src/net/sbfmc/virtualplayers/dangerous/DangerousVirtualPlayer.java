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

package net.sbfmc.virtualplayers.dangerous;

import java.net.Socket;

import net.minecraft.server.v1_5_R3.EntityPlayer;
import net.minecraft.server.v1_5_R3.Packet201PlayerInfo;
import net.minecraft.server.v1_5_R3.PendingConnection;
import net.minecraft.server.v1_5_R3.PlayerConnection;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.virtualplayers.VirtualPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class DangerousVirtualPlayer extends VirtualPlayer {
	public DangerousVirtualPlayer(String name) {
		super(name);
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean spawn() {
		if (isInPlayerList()) {
			return false;
		}
		DebugUtils.debug("VIRT-PL", "Spawning dangerous virtual player " + name);

		try {
			CraftServer server = (CraftServer) Bukkit.getServer();
			Socket socket = new Socket("localhost", server.getPort());
			PendingConnection connection = new PendingConnection(server.getServer(), socket, name);
			EntityPlayer entity = server.getHandle().attemptLogin(connection, name, "localhost");
			player = entity.getBukkitEntity();
			server.getHandle().processLogin(entity);
			entity.playerConnection = new PlayerConnection(server.getServer(), connection.networkManager, entity);
			synchronized (server.getHandle().players) {
				server.getHandle().players.add(entity);
			}
			GeneralUtils.sayToAll(ChatColor.YELLOW + name + " joined the game.");
			for (Player player : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(new Packet201PlayerInfo(name, true, GeneralUtils.getRandom().nextInt(490) + 10));
			}
		} catch (Exception err) {
			despawn();
		}

		return true;
	}

	@Override
	public boolean despawn() {
		if (!isInPlayerList()) {
			return false;
		}

		DebugUtils.debug("VIRT-PL", "Despawning dangerous virtual player " + name);

		CraftServer server = ((CraftServer) Bukkit.getServer());
		synchronized (server.getHandle().players) {
			for (Object entityPlayerRaw : server.getHandle().players) {
				if (entityPlayerRaw instanceof EntityPlayer) {
					EntityPlayer entityPlayer = (EntityPlayer) entityPlayerRaw;
					if (entityPlayer.name.equals(name)) {
						server.getHandle().players.remove(entityPlayerRaw);
						GeneralUtils.sayToAll(ChatColor.YELLOW + name + " left the game.");
						for (Player player : Bukkit.getOnlinePlayers()) {
							((CraftPlayer) player).getHandle().playerConnection.sendPacket(new Packet201PlayerInfo(name, false, 10));
						}
						break;
					}
				}
			}
		}

		return true;
	}

	public static boolean isInPlayerList(String name) {
		CraftServer server = (CraftServer) Bukkit.getServer();

		for (Object playerRaw : server.getHandle().players) {
			EntityPlayer player = (EntityPlayer) playerRaw;
			if (player.name.equals(name)) {
				return true;
			}
		}

		return false;
	}

	public boolean isInPlayerList() {
		return isInPlayerList(name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public CraftPlayer getPlayer() {
		return player;
	}
}
