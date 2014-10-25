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

package net.sbfmc.modules.tools.commands;

import net.sbfmc.module.commands.Command;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DrinkCommand extends Command {
	private String[] messages = {
			"Последняя кружка похоже была лишней...",
			"Да я пьянь!",
			"Что-то мне нехорошо...",
			"Почему блоки кривые?",
			"Пора завязывать...",
			"Буээээ...",
			"Завтра ни грамма не выпью!",
			"Где моя машина?",
			"Перебрал...",
			"Аля-ля-ля влатыаваь",
			"Может криперов тоже угостить?"
	};

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		Player player = null;

		if (args.length < 1) {
			if (sender instanceof Player) {
				player = (Player) sender;
			} else {
				sender.sendMessage(ChatColor.RED + "Вы не игрок!");
			}
		} else {
			player = Bukkit.getPlayer(args[0]);
		}

		if (player != null) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 4), true); // 1200 = 60 * 20 (1 min)
			player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "" + ChatColor.STRIKETHROUGH +
					messages[GeneralUtils.getRandom().nextInt(messages.length)]);
		}
	}

	@Override
	public String getName() {
		return "drink";
	}

	@Override
	public int getModuleID() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "выпить";
	}
}
