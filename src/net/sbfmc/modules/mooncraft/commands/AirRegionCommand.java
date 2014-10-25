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

import java.util.Set;

import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.mooncraft.MooncraftModule;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.world.selection.CoordXYZ;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;

public class AirRegionCommand extends Command {
	private MooncraftModule module = (MooncraftModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (args.length < 1) {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE + "/sbf mooncraft " + ChatColor.DARK_BLUE + "(moon) " + ChatColor.BLUE + "airregion " + ChatColor.DARK_BLUE + "(ar) " + ChatColor.GRAY + "...");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "add");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "rmnotif ID");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "lsnotif");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "reload");
			return;
		}
		if (!GeneralUtils.isPluginEnabled("WorldEdit")) {
			sender.sendMessage(ChatColor.DARK_RED + "WorldEdit не загружен!");
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "Вы не игрок!");
			return;
		}

		Player bukkitPlayer = (Player) sender;

		if (args[0].equalsIgnoreCase("add")) {
			if (!module.checkPermisson(sender, getName() + ".add", false)) {
				return;
			}

			LocalSession session = WorldEdit.getInstance().getSession(bukkitPlayer.getName());
			Region region = session.getSelection(session.getSelectionWorld());
			module.getAirRegions().add(new net.sbfmc.world.selection.Region(
					new CoordXYZ(region.getMinimumPoint().getBlockX(), region.getMinimumPoint().getBlockY(), region.getMinimumPoint().getBlockZ()),
					new CoordXYZ(region.getMaximumPoint().getBlockX(), region.getMaximumPoint().getBlockY(), region.getMaximumPoint().getBlockZ())));
			module.getAirRegionConf().saveConf();

			sender.sendMessage(ChatColor.GREEN + "Тут может быть воздух!");
		} else if (args[0].equalsIgnoreCase("rmnotif")) {
			if (!module.checkPermisson(sender, getName() + ".rmnotif", false)) {
				return;
			}
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "Нужно больше аргументов!");
				sender.sendMessage(ChatColor.RED + "/sbf mooncraft (moon) airregion (ar) rmnotif ID");
				return;
			}

			module.getAirRegionNotifs().remove(Integer.parseInt(args[1]));
			module.getAirRegionNotifConf().saveConf();

			sender.sendMessage(ChatColor.GREEN + "Уведомление " + args[1] + " удалено!");
		} else if (args[0].equalsIgnoreCase("lsnotif")) {
			if (!module.checkPermisson(sender, getName() + ".lsnotif", false)) {
				return;
			}

			sender.sendMessage(ChatColor.BLUE + "Air region notifs:");

			Set<Integer> ids = module.getAirRegionNotifs().keySet();
			for (int id : ids) {
				CoordXYZ coord = module.getAirRegionNotifs().get(id);
				sender.sendMessage(ChatColor.BLUE + "" + id + " " + coord);
			}

			if (ids.isEmpty()) {
				sender.sendMessage(ChatColor.RED + "Нет :(");
			}
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (!module.checkPermisson(sender, getName() + ".reload", false)) {
				return;
			}

			module.getAirRegionConf().loadConf();
			module.getAirRegionNotifConf().loadConf();

			sender.sendMessage(ChatColor.GREEN + "Air regions conf перезагружен!");
		} else {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE + "/sbf mooncraft " + ChatColor.DARK_BLUE + "(moon) " + ChatColor.BLUE + "airregion " + ChatColor.DARK_BLUE + "(ar) " + ChatColor.GRAY + "...");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "add");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "rmnotif ID");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "lsnotif");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "reload");
		}
	}

	@Override
	public String getName() {
		return "airRegion";
	}

	@Override
	public String getAlias() {
		return "ar";
	}

	@Override
	public int getModuleID() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "управление куполами";
	}
}
