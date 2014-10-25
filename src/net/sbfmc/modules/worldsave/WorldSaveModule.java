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

package net.sbfmc.modules.worldsave;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.Module;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldSaveModule extends Module {
	private int taskID0 = -1;

	@Override
	protected void enable() throws Exception {
		taskID0 = Bukkit.getScheduler().scheduleSyncRepeatingTask(
				SBFPlugin.getPlugin(),
				new Runnable() {
					@Override
					public void run() {
						DebugUtils.debug("WORLD-SAVE", "Starting world save...");
						Bukkit.savePlayers();
						DebugUtils.debug("WORLD-SAVE", "Saved " + Bukkit.getOnlinePlayers().length + " players");
						int worldCount = 0;
						for (World world : Bukkit.getWorlds()) {
							world.save();
							worldCount++;
						}
						DebugUtils.debug("WORLD-SAVE", "Saved " + worldCount + " worlds");
					}
				},
				18000, // 18000 = 15 * 60 * 20 (15 min)
				18000);
	}

	@Override
	protected void disable() throws Exception {
		if (taskID0 != -1) {
			Bukkit.getScheduler().cancelTask(taskID0);
			taskID0 = -1;
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
		return 13;
	}

	@Override
	public String getName() {
		return "WorldSave";
	}
}
