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

package net.sbfmc.modules.mooncraft.commands;

import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.mooncraft.MooncraftModule;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.world.GenerationQueue;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;

public class RegenCommand extends Command {
	private MooncraftModule module = (MooncraftModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (!GeneralUtils.isPluginEnabled("WorldEdit")) {
			sender.sendMessage(ChatColor.DARK_RED + "WorldEdit не загружен!");
		}
		if (sender instanceof Player) {
			Player bukkitPlayer = (Player) sender;

			LocalSession session = WorldEdit.getInstance().getSession(bukkitPlayer.getName());
			Region region = session.getSelection(session.getSelectionWorld());
			World world = Bukkit.getWorld(region.getWorld().getName());

			for (Vector2D vector2D : region.getChunks()) {
				if (world.getName().equals("Mooncraft")) {
					GenerationQueue.add(
							world.getChunkAt(vector2D.getBlockX(), vector2D.getBlockZ()),
							module.getNormalGeneration(),
							false);
				} else if (world.getName().equals("Mooncraft_nether")) {
					GenerationQueue.add(
							world.getChunkAt(vector2D.getBlockX(), vector2D.getBlockZ()),
							module.getNetherGeneration(),
							false);
				}
			}

			sender.sendMessage(ChatColor.GREEN + "Все чанки в регионе добавлены в очередь генерирования!");
		} else {
			sender.sendMessage(ChatColor.RED + "Вы не игрок!");
		}
	}

	@Override
	public String getName() {
		return "regen";
	}

	@Override
	public int getModuleID() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "перегенерация чанков в WE регионе";
	}
}
