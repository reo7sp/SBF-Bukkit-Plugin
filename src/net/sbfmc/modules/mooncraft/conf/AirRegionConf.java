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
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.conf.DefaultConf;
import net.sbfmc.modules.mooncraft.MooncraftModule;
import net.sbfmc.world.selection.CoordXYZ;
import net.sbfmc.world.selection.Region;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class AirRegionConf extends DefaultConf {
	private MooncraftModule module = (MooncraftModule) getModule();

	@Override
	public void initConf() throws IOException {
		confFile = new File(SBFPlugin.getPlugin().getDataFolder(), "airregions.yml");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFile = null;
	}

	@Override
	public void loadConf() throws IOException {
		module.getAirRegions().clear();

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(confFile);
		Set<String> idStrings = conf.getKeys(false);

		for (String idString : idStrings) {
			ConfigurationSection airRegionSection = conf.getConfigurationSection(idString);

			ConfigurationSection minPointSection = airRegionSection.getConfigurationSection("minPoint");
			int minX = minPointSection.getInt("x");
			int minY = minPointSection.getInt("y");
			int minZ = minPointSection.getInt("z");

			ConfigurationSection maxPointSection = airRegionSection.getConfigurationSection("maxPoint");
			int maxX = maxPointSection.getInt("x");
			int maxY = maxPointSection.getInt("y");
			int maxZ = maxPointSection.getInt("z");

			Region region = new Region(new CoordXYZ(minX, minY, minZ), new CoordXYZ(maxX, maxY, maxZ));
			module.getAirRegions().add(region);
			DebugUtils.debug("moon", "Loaded region with coords " + region);
		}
	}

	@Override
	public void saveConf() throws IOException {
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(confFile);

		int count = 0;
		for (Region region : module.getAirRegions()) {
			conf.set(count + "." + "minPoint.x", region.getMinPoint().getX());
			conf.set(count + "." + "minPoint.y", region.getMinPoint().getY());
			conf.set(count + "." + "minPoint.z", region.getMinPoint().getZ());

			conf.set(count + "." + "maxPoint.x", region.getMaxPoint().getX());
			conf.set(count + "." + "maxPoint.y", region.getMaxPoint().getY());
			conf.set(count + "." + "maxPoint.z", region.getMaxPoint().getZ());

			count++;
		}

		conf.save(confFile);
	}

	@Override
	public int getModuleID() {
		return 2;
	}
}
