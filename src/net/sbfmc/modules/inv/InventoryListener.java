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

import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.ModuleIntegration;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener extends ModuleIntegration implements Listener {
	private InventoryModule module = (InventoryModule) getModule();

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		if (!GeneralUtils.checkAuth(event.getPlayer())) {
			return;
		}

		String fromWorldName = event.getFrom().getName();
		String toWorldName = event.getPlayer().getWorld().getName();

		// saving invs
		try {
			if (fromWorldName.startsWith("Classic")) {
				module.getClassicInvConf().savePlayer(event.getPlayer());
			}
			if (fromWorldName.startsWith("Mooncraft")) {
				module.getMooncraftInvConf().savePlayer(event.getPlayer());
			}
		} catch (IOException err) {
			DebugUtils.debugError("INV", "Can't save inventory of " + event.getPlayer().getName() + " in " + fromWorldName, err);
		}

		// loading invs
		try {
			if (toWorldName.startsWith("Classic")) {
				if (!fromWorldName.startsWith("Classic")) {
					module.getClassicInvConf().loadPlayer(event.getPlayer());
				}
			} else if (toWorldName.startsWith("Mooncraft")) {
				if (!fromWorldName.startsWith("Mooncraft")) {
					module.getMooncraftInvConf().loadPlayer(event.getPlayer());
				}
			} else {
				event.getPlayer().getInventory().setContents(new ItemStack[event.getPlayer().getInventory().getSize()]);
				event.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
				event.getPlayer().setHealth(20);
				event.getPlayer().setFoodLevel(20);
				event.getPlayer().setLevel(0);
				event.getPlayer().setExp(0);
			}
		} catch (IOException err) {
			DebugUtils.debugError("INV", "Can't load inventory of " + event.getPlayer().getName() + " in " + fromWorldName, err);
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (!GeneralUtils.checkAuth(event.getPlayer())) {
			return;
		}

		String worldName = event.getPlayer().getWorld().getName();

		event.getPlayer().getInventory().setContents(new ItemStack[event.getPlayer().getInventory().getSize()]);
		event.getPlayer().getInventory().setArmorContents(new ItemStack[4]);
		event.getPlayer().setHealth(20);
		event.getPlayer().setFoodLevel(20);
		event.getPlayer().setLevel(0);
		event.getPlayer().setExp(0);

		try {
			if (worldName.startsWith("Classic")) {
				module.getClassicInvConf().savePlayer(event.getPlayer());
			} else if (worldName.startsWith("Mooncraft")) {
				module.getMooncraftInvConf().savePlayer(event.getPlayer());
			}
		} catch (IOException err) {
			DebugUtils.debugError("INV", "Can't save inventory of " + event.getPlayer().getName() + " in " + worldName, err);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		GeneralUtils.repairItemNameEncoding(event.getCurrentItem());
	}

	@Override
	public int getModuleID() {
		return 4;
	}
}
