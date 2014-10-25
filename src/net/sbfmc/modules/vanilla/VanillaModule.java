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

package net.sbfmc.modules.vanilla;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.Module;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.griefcraft.lwc.LWC;

public class VanillaModule extends Module {
	public static final String[] ALLOWED_COMMANDS = {
			"sbf",
			"tospawn",
			"me",
			"tell",
			"w",
			"kill",
			"seed",
	};

	@Override
	protected void enable() throws Exception {
		Bukkit.getPluginManager().registerEvents(new VanillaListener(), SBFPlugin.getPlugin());

		if (GeneralUtils.isPluginEnabled("LWC")) {
			LWC.getInstance().getModuleLoader().registerModule(SBFPlugin.getPlugin(), new VanillaLWCModule());
		}
	}

	@Override
	protected void disable() throws Exception {
		if (GeneralUtils.isPluginEnabled("LWC")) {
			LWC.getInstance().getModuleLoader().removeModules(SBFPlugin.getPlugin());
		}
	}

	@Override
	protected void registerAllCommands() {
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
	}

	@Override
	public int getID() {
		return 14;
	}

	@Override
	public String getName() {
		return "Vanilla";
	}
}
