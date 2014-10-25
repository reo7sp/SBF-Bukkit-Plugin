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

package net.sbfmc.modules.admin;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.Module;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.admin.commands.DisableModuleCommand;
import net.sbfmc.modules.admin.commands.EnableModuleCommand;
import net.sbfmc.modules.admin.commands.GenerationQueueCommand;
import net.sbfmc.modules.admin.commands.ToggleDebugCommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AdminModule extends Module {
	@Override
	public void enable() throws Exception {
		Bukkit.getPluginManager().registerEvents(new AdminListener(), SBFPlugin.getPlugin());
	}

	@Override
	public void disable() throws Exception {
	}

	@Override
	protected void registerAllCommands() {
		commands = new Command[] {
				new GenerationQueueCommand(),
				new ToggleDebugCommand(),
				new EnableModuleCommand(),
				new DisableModuleCommand(),
		};
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
	}

	@Override
	public int getID() {
		return 3;
	}

	@Override
	public String getName() {
		return "Admin";
	}
}
