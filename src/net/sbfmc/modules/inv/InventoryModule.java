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

package net.sbfmc.modules.inv;

import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.Module;
import net.sbfmc.modules.inv.conf.InventoryConf;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InventoryModule extends Module {
	private InventoryConf classicInvConf;
	private InventoryConf mooncraftInvConf;
	private int taskID0 = -1;
	private int taskID1 = -1;

	@Override
	public void enable() throws Exception {
		// initing confs
		classicInvConf = new InventoryConf("Classic");
		mooncraftInvConf = new InventoryConf("Mooncraft");
		classicInvConf.initConf();
		mooncraftInvConf.initConf();

		// starting timers
		taskID0 = Bukkit.getScheduler().scheduleSyncRepeatingTask(
				SBFPlugin.getPlugin(),
				new Runnable() {
					@Override
					public void run() {
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (!GeneralUtils.checkAuth(player)) {
								return;
							}

							String worldName = player.getWorld().getName();

							try {
								if (worldName.startsWith("Classic")) {
									getClassicInvConf().savePlayer(player);
								} else if (worldName.startsWith("Mooncraft")) {
									getMooncraftInvConf().savePlayer(player);
								}
							} catch (IOException err) {
								DebugUtils.debugError("INV", "Can't save inventory of " + player.getName() + " in " + worldName, err);
							}
						}
					}
				},
				1200, // 1200 = 60 * 20 (1 min)
				1200);
		taskID1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(
				SBFPlugin.getPlugin(),
				new Runnable() {
					@Override
					public void run() {
						for (Player player : Bukkit.getOnlinePlayers()) {
							if (!GeneralUtils.checkAuth(player)) {
								return;
							}

							InventoryUtils.optimizeInventory(player);
						}
					}
				},
				200, // 200 = 20 * 10 (10 sec)
				200);

		// registering listener
		Bukkit.getPluginManager().registerEvents(new InventoryListener(), SBFPlugin.getPlugin());
	}

	@Override
	public void disable() throws Exception {
		// saving confs
		for (Player player : Bukkit.getOnlinePlayers()) {
			unregisterPlayer(player);
		}

		// deiniting confs
		classicInvConf.deinitConf();
		mooncraftInvConf.deinitConf();
		classicInvConf = null;
		mooncraftInvConf = null;

		// stopping timers
		if (taskID0 != -1) {
			Bukkit.getScheduler().cancelTask(taskID0);
			taskID0 = -1;
		}
		if (taskID1 != -1) {
			Bukkit.getScheduler().cancelTask(taskID1);
			taskID1 = -1;
		}
	}

	@Override
	protected void registerAllCommands() {
	}

	@Override
	public void registerPlayer(final Player player) throws Exception {
		Bukkit.getScheduler().scheduleSyncDelayedTask(SBFPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				String worldName = player.getWorld().getName();

				try {
					if (worldName.startsWith("Classic")) {
						getClassicInvConf().loadPlayer(player);
					} else if (worldName.startsWith("Mooncraft")) {
						getMooncraftInvConf().loadPlayer(player);
					}
				} catch (IOException err) {
					DebugUtils.debugError("INV", "Can't load inventory of " + player.getName() + " in " + worldName, err);
				}
			}
		});
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
		String worldName = player.getWorld().getName();

		if (worldName.startsWith("Classic")) {
			getClassicInvConf().savePlayer(player);
		} else if (worldName.startsWith("Mooncraft")) {
			getMooncraftInvConf().savePlayer(player);
		}

		playerSessions.remove(getPlayerSession(player));
	}

	@Override
	public int getID() {
		return 4;
	}

	@Override
	public String getName() {
		return "Inventory";
	}

	@Override
	public String getAlias() {
		return "inv";
	}

	public InventoryConf getClassicInvConf() {
		return classicInvConf;
	}

	public InventoryConf getMooncraftInvConf() {
		return mooncraftInvConf;
	}
}
