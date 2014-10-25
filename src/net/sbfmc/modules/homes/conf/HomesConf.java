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

package net.sbfmc.modules.homes.conf;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.conf.PlayersConf;
import net.sbfmc.modules.homes.HomesModule;
import net.sbfmc.modules.homes.HomesPlayerSession;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class HomesConf extends PlayersConf {
	private HomesModule module = (HomesModule) getModule();

	@Override
	public PlayerSession loadPlayer(OfflinePlayer player) throws IOException {
		return loadPlayer(new HomesPlayerSession(player));
	}

	@Override
	public PlayerSession loadPlayer(PlayerSession sessionRaw) throws IOException {
		HomesPlayerSession session = (HomesPlayerSession) sessionRaw;
		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		session.setInvitedPlayers(conf.getStringList("invitedPlayers"));
		session.setPublic(conf.getBoolean("isPublic"));

		Set<String> worldNames = conf.getKeys(false);
		for (String worldName : worldNames) {
			ConfigurationSection section = conf.getConfigurationSection(worldName);

			if (section == null) {
				continue;
			}

			session.addLocation(new Location(
					Bukkit.getWorld(worldName),
					section.getDouble("x"),
					section.getDouble("y"),
					section.getDouble("z"),
					(float) section.getDouble("yaw"),
					(float) section.getDouble("pitch")));
		}

		if (session.getPlayer().getPlayer() != null) {
			Location location = session.getLocation(session.getPlayer().getPlayer().getWorld().getName());
			if (location != null) {
				session.getPlayer().getPlayer().setCompassTarget(location);
			} else {
				session.getPlayer().getPlayer().setCompassTarget(session.getPlayer().getPlayer().getWorld().getSpawnLocation());
			}
		}

		return session;
	}

	@Override
	public void savePlayer(OfflinePlayer player) throws IOException {
		savePlayer(module.getPlayerSession(player));
	}

	@Override
	public void savePlayer(PlayerSession sessionRaw) throws IOException {
		HomesPlayerSession session = (HomesPlayerSession) sessionRaw;
		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		conf.set("invitedPlayers", session.getInvitedPlayers());
		conf.set("isPublic", session.isPublic());

		for (Location location : session.getLocations()) {
			if (!conf.isSet(location.getWorld().getName())) {
				conf.createSection(location.getWorld().getName());
			}

			ConfigurationSection section = conf.getConfigurationSection(location.getWorld().getName());

			section.set("x", location.getX());
			section.set("y", location.getY());
			section.set("z", location.getZ());
			section.set("yaw", location.getYaw());
			section.set("pitch", location.getPitch());
		}

		conf.save(file);
	}

	@Override
	public void initConf() throws IOException {
		confFolder = new File(SBFPlugin.getPlugin().getDataFolder(), "homes");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFolder = null;
	}

	@Override
	public int getModuleID() {
		return 5;
	}
}
