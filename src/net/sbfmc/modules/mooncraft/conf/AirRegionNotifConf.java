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

package net.sbfmc.modules.mooncraft.conf;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.conf.DefaultConf;
import net.sbfmc.modules.mooncraft.MooncraftModule;
import net.sbfmc.world.selection.CoordXYZ;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class AirRegionNotifConf extends DefaultConf {
	private MooncraftModule module = (MooncraftModule) getModule();

	@Override
	public void initConf() throws IOException {
		confFile = new File(SBFPlugin.getPlugin().getDataFolder(), "airregionnotifs.yml");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFile = null;
	}

	@Override
	public void loadConf() throws IOException {
		module.getAirRegionNotifs().clear();

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(confFile);
		Set<String> idStrings = conf.getKeys(false);

		for (String idString : idStrings) {
			ConfigurationSection airRegionSection = conf.getConfigurationSection(idString);

			ConfigurationSection coordSection = airRegionSection.getConfigurationSection("coord");
			int x = coordSection.getInt("x");
			int y = coordSection.getInt("y");
			int z = coordSection.getInt("z");

			module.getAirRegionNotifs().put(Integer.parseInt(idString), new CoordXYZ(x, y, z));
		}
	}

	@Override
	public void saveConf() throws IOException {
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(confFile);
		Set<Integer> ids = module.getAirRegionNotifs().keySet();

		for (int id : ids) {
			CoordXYZ coord = module.getAirRegionNotifs().get(id);
			conf.set(id + "." + "coord.x", coord.getX());
			conf.set(id + "." + "coord.y", coord.getY());
			conf.set(id + "." + "coord.z", coord.getZ());
		}

		conf.save(confFile);
	}

	@Override
	public int getModuleID() {
		return 2;
	}
}
