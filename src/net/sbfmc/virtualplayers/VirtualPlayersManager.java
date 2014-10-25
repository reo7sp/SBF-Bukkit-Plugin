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

package net.sbfmc.virtualplayers;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.bukkit.entity.Player;

public class VirtualPlayersManager {
	private static Collection<VirtualPlayer> players = Collections.synchronizedCollection(new HashSet<VirtualPlayer>());

	public static void addPlayer(VirtualPlayer player) {
		players.add(player);
		player.spawn();
	}

	public static void removePlayer(String name) {
		removePlayer(getPlayer(name));
	}

	public static void removePlayer(VirtualPlayer player) {
		player.despawn();
		players.remove(player);
	}

	public static void removeAll() {
		for (VirtualPlayer player : players) {
			player.despawn();
		}
		players.clear();
	}

	public static void removeAll(Class<? extends VirtualPlayer> c) {
		for (VirtualPlayer player : players) {
			if (player.getClass().equals(c)) {
				player.despawn();
			}
		}
		players.clear();
	}

	public static boolean contains(Player player) {
		return contains(player.getName());
	}

	public static boolean contains(String name) {
		return getPlayer(name) != null;
	}

	public static Collection<VirtualPlayer> getPlayers() {
		return Collections.unmodifiableCollection(players);
	}

	public static VirtualPlayer getPlayer(String name) {
		for (VirtualPlayer player : players) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	public static int getPlayersCount() {
		return players.size();
	}
}
