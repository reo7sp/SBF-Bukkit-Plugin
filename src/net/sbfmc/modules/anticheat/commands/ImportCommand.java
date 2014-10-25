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

package net.sbfmc.modules.anticheat.commands;

import java.io.File;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.anticheat.AnticheatModule;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ImportCommand extends Command {
	private AnticheatModule module = (AnticheatModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		File allowedArchivesDir = new File(SBFPlugin.getPlugin().getDataFolder(), "anticheat_import" + File.separator + "allowed");
		File deniedArchivesDir = new File(SBFPlugin.getPlugin().getDataFolder(), "anticheat_import" + File.separator + "denied");

		allowedArchivesDir.mkdirs();
		deniedArchivesDir.mkdirs();

		module.getAllowedFiles().clear();
		for (File file : allowedArchivesDir.listFiles()) {
			if (file.getName().endsWith(".zip") || file.getName().endsWith(".jar")) {
				for (String fileName : GeneralUtils.listContentsOfZipFile(file)) {
					module.getAllowedFiles().add(fileName);
				}
			}
		}
		module.getAllowedFilesConf().saveConf();

		module.getDeniedFiles().clear();
		for (File file : deniedArchivesDir.listFiles()) {
			if (file.getName().endsWith(".zip") || file.getName().endsWith(".jar")) {
				for (String fileName : GeneralUtils.listContentsOfZipFile(file)) {
					module.getDeniedFiles().add(fileName);
				}
			}
		}
		module.getDeniedFilesConf().saveConf();

		sender.sendMessage(ChatColor.RED + "Старые данные удалены!");
		sender.sendMessage(ChatColor.GREEN + "Новые данные импортированы!");
	}

	@Override
	public String getName() {
		return "import";
	}

	@Override
	public int getModuleID() {
		return 8;
	}

	@Override
	public boolean isDangerous() {
		return true;
	}
}
