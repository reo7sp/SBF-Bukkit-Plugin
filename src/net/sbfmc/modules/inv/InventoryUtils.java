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

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
	public static void optimizeInventory(Player player) {
		optimizeInventory(player.getInventory());
	}

	public static void optimizeInventory(Inventory inventory) {
		for (int i = 0; i < inventory.getContents().length; i++) {
			ItemStack item = inventory.getContents()[i];
			if (item == null) {
				continue;
			}
			if (item.getMaxStackSize() == -1 || item.getMaxStackSize() == 1) {
				continue;
			}

			for (int j = i + 1; j < inventory.getContents().length; j++) {
				ItemStack item0 = inventory.getContents()[j];
				if (item0 == null) {
					continue;
				}

				if (item.getTypeId() == item0.getTypeId() && item.getData().getData() == item0.getData().getData()) {
					int nextAmount = item0.getAmount() + item.getAmount();
					if (nextAmount > item.getMaxStackSize()) {
						item0.setAmount(nextAmount - item.getMaxStackSize());
						nextAmount = item.getMaxStackSize();
					} else {
						inventory.setItem(j, null);
					}
					item.setAmount(nextAmount);
					break;
				}
			}
		}
	}
}
