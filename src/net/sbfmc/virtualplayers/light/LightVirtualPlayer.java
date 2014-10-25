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

import net.minecraft.server.v1_5_R3.EntityPlayer;
import net.minecraft.server.v1_5_R3.Packet201PlayerInfo;
import net.minecraft.server.v1_5_R3.PlayerInteractManager;
import net.minecraft.server.v1_5_R3.WorldServer;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.virtualplayers.VirtualPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class LightVirtualPlayer extends VirtualPlayer {
	public LightVirtualPlayer(String name) {
		super(name);
	}

	@Override
	public boolean spawn() {
		DebugUtils.debug("VIRT-PL", "Spawning light virtual player " + name);

		try {
			CraftServer server = (CraftServer) Bukkit.getServer();
			WorldServer world = server.getServer().worlds.get(0);
			new EntityPlayer(server.getServer(), world, name, new PlayerInteractManager(world));
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
		DebugUtils.debug("VIRT-PL", "Despawning light virtual player " + name);

		GeneralUtils.sayToAll(ChatColor.YELLOW + name + " left the game.");
		for (Player player : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(new Packet201PlayerInfo(name, false, 10));
		}

		return true;
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
