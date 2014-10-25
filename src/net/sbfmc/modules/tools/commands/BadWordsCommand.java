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
import net.sbfmc.modules.tools.ToolsModule;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class BadWordsCommand extends Command {
	private ToolsModule module = (ToolsModule) getModule();

	@Override
	public void exec(CommandSender sender, String[] args) throws Exception {
		if (args.length < 1) {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE + "/sbf tools badwords " + ChatColor.DARK_BLUE + "(bw) " + ChatColor.GRAY + "...");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "add СЛОВО " + ChatColor.DARK_BLUE + "(a)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "delete СЛОВО " + ChatColor.DARK_BLUE + "(d)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "list " + ChatColor.DARK_BLUE + "(l)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "seeLast " + ChatColor.DARK_BLUE + "(s)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "reload " + ChatColor.DARK_BLUE + "(r)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "on " + ChatColor.DARK_BLUE + "(n)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "off " + ChatColor.DARK_BLUE + "(f)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "status " + ChatColor.DARK_BLUE + "(st)");
			return;
		}
		if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
			if (!module.checkPermisson(sender, getName() + ".add", false)) {
				return;
			}

			String word = args[1];
			for (int i = 2; i < args.length; i++) {
				word += " " + args[i];
			}
			word = word.toLowerCase().trim();

			if (!word.equals("")) {
				module.getBadWords().add(word);

				module.getBadWordsConf().saveConf();

				sender.sendMessage(ChatColor.GREEN + "Готово! Это слово плохое");
			} else {
				sender.sendMessage(ChatColor.RED + "Синтаксис: /sbf tools badwords (bw) add СЛОВО");
			}
		} else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("d")) {
			if (!module.checkPermisson(sender, getName() + ".delete", false)) {
				return;
			}

			String word = args[1];
			for (int i = 2; i < args.length; i++) {
				word += " " + args[i];
			}
			word = word.toLowerCase().trim();

			if (!word.equals("")) {
				module.getBadWords().remove(word);

				module.getBadWordsConf().saveConf();

				sender.sendMessage(ChatColor.GREEN + "Готово! Это слово хорошее");
			} else {
				sender.sendMessage(ChatColor.RED + "Синтаксис: /sbf tools badwords (bw) remove СЛОВО");
			}
		} else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("l")) {
			if (!module.checkPermisson(sender, getName() + ".list", false)) {
				return;
			}

			String message = "";
			for (String word : module.getBadWords()) {
				message += ChatColor.BLUE + word + ChatColor.WHITE + ", ";
			}

			if (message.isEmpty()) {
				sender.sendMessage(ChatColor.RED + "Их нет :(");
			} else {
				sender.sendMessage(message.substring(message.length() - 3));
			}
		} else if (args[0].equalsIgnoreCase("seeLast") || args[0].equalsIgnoreCase("s")) {
			if (!module.checkPermisson(sender, getName() + ".seeLast", false)) {
				return;
			}

			sender.sendMessage(ChatColor.BLUE + module.getReportMessage());
		} else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
			if (!module.checkPermisson(sender, getName() + ".reload", false)) {
				return;
			}

			sender.sendMessage(ChatColor.BLUE + "Загрузка слов из файла...");

			module.getBadWordsConf().loadConf();

			sender.sendMessage(ChatColor.GREEN + "Готово!");
		} else if (args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("n")) {
			if (!module.checkPermisson(sender, getName() + ".on", false)) {
				return;
			}

			module.setBadWordsOff(false);

			sender.sendMessage(ChatColor.GREEN + "Плагин включен!");
			return;
		} else if (args[0].equalsIgnoreCase("off") || args[0].equalsIgnoreCase("f")) {
			if (!module.checkPermisson(sender, getName() + ".off", false)) {
				return;
			}

			module.setBadWordsOff(true);

			sender.sendMessage(ChatColor.RED + "Плагин выключен!");
		} else if (args[0].equalsIgnoreCase("status") || args[0].equalsIgnoreCase("st")) {
			if (!module.checkPermisson(sender, getName() + ".status", false)) {
				return;
			}

			if (module.isBadWordsOff()) {
				sender.sendMessage(ChatColor.RED + "Плагин выключен!");
			} else {
				sender.sendMessage(ChatColor.GREEN + "Плагин включен!");
			}
		} else {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE + "/sbf tools badwords " + ChatColor.DARK_BLUE + "(bw) " + ChatColor.GRAY + "...");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "add СЛОВО " + ChatColor.DARK_BLUE + "(a)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "delete СЛОВО " + ChatColor.DARK_BLUE + "(d)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "list " + ChatColor.DARK_BLUE + "(l)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "seeLast " + ChatColor.DARK_BLUE + "(s)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "reload " + ChatColor.DARK_BLUE + "(r)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "on " + ChatColor.DARK_BLUE + "(n)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "off " + ChatColor.DARK_BLUE + "(f)");
			sender.sendMessage(ChatColor.GRAY + "     ... " + ChatColor.BLUE + "status " + ChatColor.DARK_BLUE + "(st)");
		}
	}

	@Override
	public String getName() {
		return "badwords";
	}

	@Override
	public String getAlias() {
		return "bw";
	}

	@Override
	public int getModuleID() {
		return 1;
	}

	@Override
	public String getDescription() {
		return "управление антиматом";
	}
}
