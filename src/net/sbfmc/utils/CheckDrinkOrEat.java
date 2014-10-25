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

package net.sbfmc.utils;

import net.sbfmc.module.PlayerSession;

import org.bukkit.inventory.ItemStack;

public abstract class CheckDrinkOrEat implements Runnable {
	protected PlayerSession session;
	protected int slot;
	protected ItemStack firstItem;

	public static final long DELAY = 33L;

	public CheckDrinkOrEat(PlayerSession session) {
		this.session = session;
		this.firstItem = session.getPlayer().getPlayer().getInventory().getItemInHand().clone();
		this.slot = session.getPlayer().getPlayer().getInventory().getHeldItemSlot();
	}

	@Override
	public void run() {
		ItemStack item = session.getPlayer().getPlayer().getItemInHand();

		if (session.getPlayer().getPlayer().getInventory().getHeldItemSlot() == slot &&
				(item == null || item.getTypeId() == 0 || item.getTypeId() == 374 || item.getTypeId() == 281 || item.getTypeId() == 325 || item.getAmount() == firstItem.getAmount() - 1)) {
			check(firstItem);
		}
	}

	protected abstract void check(ItemStack item);
}
