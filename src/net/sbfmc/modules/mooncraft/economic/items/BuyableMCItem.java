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

package net.sbfmc.modules.mooncraft.economic.items;

import net.sbfmc.modules.mooncraft.MooncraftPlayerSession;

import org.bukkit.inventory.ItemStack;

public class BuyableMCItem extends BuyableItem {
	private int itemID;
	private int amount;
	private byte itemData;

	public BuyableMCItem(int cost, int itemID, byte itemData, int amount) {
		super(cost, "item");
		this.itemID = itemID;
		this.itemData = itemData;
		this.amount = amount;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void buyAction(MooncraftPlayerSession session) {
		session.getPlayer().getPlayer().getInventory().addItem(new ItemStack(itemID, amount, (short) 0, itemData));
	}

	public int getItemID() {
		return itemID;
	}

	public int getAmount() {
		return amount;
	}

	public byte getItemData() {
		return itemData;
	}
}
