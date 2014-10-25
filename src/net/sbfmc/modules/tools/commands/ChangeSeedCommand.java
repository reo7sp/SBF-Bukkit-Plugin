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

import java.lang.reflect.Field;

import net.minecraft.server.v1_5_R3.WorldData;
import net.sbfmc.module.commands.Command;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_5_R3.CraftWorld;
import org.bukkit.entity.Player;

public class ChangeSeedCommand extends Command {
	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Нужно больше аргументов!");
			sender.sendMessage(ChatColor.RED + "/sbf tools changeSeed (chseed) seed");
		}
		if (sender instanceof Player) {
			World world = ((Player) sender).getWorld();
			long newSeed = Long.parseLong(args[0]);

			WorldData worldData = ((CraftWorld) world).getHandle().worldData; // get the world data (notch code)
			Field f = worldData.getClass().getDeclaredField("seed"); // get the private field named seed
			f.setAccessible(true); // set it accessible so we can modify it
			f.setLong(worldData, newSeed); // set the new seed to it
			f.setAccessible(false); // set if non-accessible again

			world.save(); // save the world with his new seed

			sender.sendMessage(ChatColor.BLUE + "Готово!");
		}
	}

	@Override
	public String getName() {
		return "changeseed";
	}

	@Override
	public String getAlias() {
		return "chseed";
	}

	@Override
	public int getModuleID() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "изменение сида мира";
	}

	@Override
	public boolean isDangerous() {
		return true;
	}
}
