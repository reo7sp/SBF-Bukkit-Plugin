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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sbfmc.def.SBFPlugin;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class MyHomeImporter {
	public static void importData() throws Exception {
		Class.forName("org.sqlite.JDBC");

		// init
		Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SBFPlugin.getPlugin().getDataFolder().getParent() + "/MyHome/homes.db");
		Statement statement = connection.createStatement();

		// import
		ResultSet resultSet = statement.executeQuery("SELECT * FROM hometable");
		while (resultSet.next()) {
			File file = new File(SBFPlugin.getPlugin().getDataFolder(), "homes/" + resultSet.getString("name") + ".yml");
			YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

			conf.set("invitedPlayers", resultSet.getString("permissions").split(","));
			conf.set("isPublic", resultSet.getInt("publicAll") == 1);

			if (!conf.isSet(resultSet.getString("world"))) {
				conf.createSection(resultSet.getString("world"));
			}
			ConfigurationSection section = conf.getConfigurationSection(resultSet.getString("world"));
			section.set("x", resultSet.getDouble("x"));
			section.set("y", resultSet.getDouble("y"));
			section.set("z", resultSet.getDouble("z"));
			section.set("yaw", resultSet.getFloat("yaw"));
			section.set("pitch", resultSet.getFloat("pitch"));

			conf.save(file);
		}
		resultSet.close();

		// deinit
		statement.close();
		connection.close();
	}
}
