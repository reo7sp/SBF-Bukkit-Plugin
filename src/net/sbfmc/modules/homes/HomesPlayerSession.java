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

package net.sbfmc.modules.homes;

import java.util.Collection;
import java.util.HashSet;

import net.sbfmc.module.PlayerSession;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

public class HomesPlayerSession extends PlayerSession {
	private Collection<Location> locations = new HashSet<Location>();
	private Collection<String> invitedPlayers = new HashSet<String>();
	private boolean isPublic;

	public HomesPlayerSession(OfflinePlayer player) {
		super(player);
	}

	@Override
	public int getModuleID() {
		return 5;
	}

	public void addLocation(Location location) {
		if (location.getWorld() == null) {
			return;
		}
		Location existingLocation = null;

		// finding location with same world
		for (Location loopLocation : locations) {
			if (loopLocation.getWorld().getName().equalsIgnoreCase(location.getWorld().getName())) {
				existingLocation = loopLocation;
				break;
			}
		}

		// removing location with same world
		if (existingLocation != null) {
			locations.remove(existingLocation);
		}

		// adding new location
		locations.add(location);
	}

	public void removeLocation(String worldName) {
		Location location = null;

		// finding location
		for (Location loopLocation : locations) {
			if (loopLocation.getWorld().getName().equalsIgnoreCase(worldName)) {
				location = loopLocation;
				break;
			}
		}

		// removing location
		locations.remove(location);
	}

	public boolean teleport() {
		return teleport(player.getPlayer().getWorld().getName());
	}

	public boolean teleport(String worldName) {
		Location location = null;

		// finding location
		for (Location loopLocation : locations) {
			if (loopLocation.getWorld().getName().equalsIgnoreCase(worldName)) {
				location = loopLocation;
				break;
			}
		}

		// teleporting!
		if (location != null) {
			player.getPlayer().teleport(location);
			return true;
		}
		return false;
	}

	public Location getLocation(String worldName) {
		// finding location
		for (Location location : locations) {
			if (location.getWorld().getName().equalsIgnoreCase(worldName)) {
				return location;
			}
		}
		return null;
	}

	public Collection<Location> getLocations() {
		return locations;
	}

	public Collection<String> getInvitedPlayers() {
		return invitedPlayers;
	}

	public void setInvitedPlayers(Collection<String> invitedPlayers) {
		this.invitedPlayers = invitedPlayers;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}
}
