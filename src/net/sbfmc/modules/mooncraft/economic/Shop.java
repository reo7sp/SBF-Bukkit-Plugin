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

package net.sbfmc.modules.mooncraft.economic;

import net.sbfmc.modules.mooncraft.MooncraftPlayerSession;
import net.sbfmc.modules.mooncraft.economic.items.BuyableItem;
import net.sbfmc.modules.mooncraft.economic.items.BuyableMCItem;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public class Shop {
	public static void buy(MooncraftPlayerSession session, String name) {
		buy(session, BuyableItemFactory.getBuyableItem(name));
	}

	public static void buyItem(MooncraftPlayerSession session, int id, byte data, int amount, int cost) {
		buy(session, new BuyableMCItem(cost, id, data, amount));
	}

	public static void buy(MooncraftPlayerSession session, BuyableItem item) {
		if (item == null) {
			session.getPlayer().getPlayer().sendMessage(ChatColor.RED + "Ошибка при покупке!");
			return;
		}

		if (session.getPlayer().getPlayer().getInventory().contains(371, item.getCost())) {
			item.buyAction(session);

			session.getPlayer().getPlayer().getInventory().removeItem(new ItemStack(371, item.getCost()));

			session.getPlayer().getPlayer().sendMessage(ChatColor.GREEN + "Спасибо за покупку!");
		} else {
			session.getPlayer().getPlayer().sendMessage(ChatColor.RED + "У вас не достаточно денег!");
			session.getPlayer().getPlayer().sendMessage(ChatColor.RED + "Нужно " + item.getCost() + " золотых самородков!");
		}
	}
}
