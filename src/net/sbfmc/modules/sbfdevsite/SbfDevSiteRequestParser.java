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

package net.sbfmc.modules.sbfdevsite;

import main.java.com.webkonsept.minecraft.lagmeter.LagMeter;
import net.sbfmc.def.SBFPlugin;
import net.sbfmc.server.Connection;
import net.sbfmc.server.RequestParser;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class SbfDevSiteRequestParser extends RequestParser {
	private SbfDevSiteModule module = (SbfDevSiteModule) SBFPlugin.getModule(10);

	@Override
	public void parse(Connection connection, String key, String value) throws Exception {
		if (key.equalsIgnoreCase("STATUS")) {
			if (GeneralUtils.isPluginEnabled("LagMeter")) {
				int totalChunks = 0;
				int totalEntities = 0;

				for (World world : Bukkit.getServer().getWorlds()) {
					totalChunks += world.getLoadedChunks().length;
					totalEntities += world.getEntities().size();
				}

				connection.sendResponse("STATUS " + (int) LagMeter.p.getTPS() + " " + (int) LagMeter.p.getMemory()[3] + " " + totalChunks + " " + totalEntities);
			}
		} else if (key.equalsIgnoreCase("STOP")) {

		} else if (key.equalsIgnoreCase("SHOW-CONSOLE")) {
			for (String string : module.getLog()) {
				if (string == null) {
					continue;
				}
				connection.sendResponse("CONSOLE " + string);
			}
			connection.sendResponse("CONSOLE-END");
		}
	}

	@Override
	public String getHeader() {
		return "SBF-DEV-SITE";
	}

	@Override
	public int getClientSecret() {
		return 838823003;
	}

	@Override
	public int getServerSecret() {
		return 725162532;
	}
}
