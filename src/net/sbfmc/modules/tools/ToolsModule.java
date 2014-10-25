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

package net.sbfmc.modules.tools;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.Module;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.tools.commands.BadWordsCommand;
import net.sbfmc.modules.tools.commands.ChangeSeedCommand;
import net.sbfmc.modules.tools.commands.DrinkCommand;
import net.sbfmc.modules.tools.commands.FreeCommand;
import net.sbfmc.modules.tools.commands.GCCommand;
import net.sbfmc.modules.tools.commands.GetIPCommand;
import net.sbfmc.modules.tools.commands.KitCommand;
import net.sbfmc.modules.tools.commands.ListEntitiesCommand;
import net.sbfmc.modules.tools.commands.MSKTimeCommand;
import net.sbfmc.modules.tools.commands.TNTCommand;
import net.sbfmc.modules.tools.commands.ToSpawnCommand;
import net.sbfmc.modules.tools.conf.BadWordsConf;
import net.sbfmc.modules.tools.conf.KitConf;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class ToolsModule extends Module {
	private boolean noTntForAll, badWordsOff;
	private Collection<String> badWords = new HashSet<String>();
	private String reportMessage;
	private BadWordsConf badWordsConf;
	private KitConf kitConf;
	private int taskID0 = -1;

	@SuppressWarnings("deprecation")
	@Override
	public void enable() throws Exception {
		// registering commands
		registerAllCommands();

		// initializing conf
		badWordsConf = new BadWordsConf();
		kitConf = new KitConf();
		badWordsConf.initConf();
		kitConf.initConf();
		badWordsConf.loadConf();

		taskID0 = Bukkit.getScheduler().scheduleAsyncRepeatingTask(
				SBFPlugin.getPlugin(),
				new Runnable() {
					@Override
					public void run() {
						// cleaning up worlds
						int removedArrows = 0;
						for (World world : Bukkit.getWorlds()) {
							for (Entity entity : world.getEntities()) {
								if (entity instanceof Arrow) {
									entity.remove();
									removedArrows++;
								}
							}
						}
						DebugUtils.debug("TOOLS", "All worlds have been cleared from " + removedArrows + " arrows");

						// deleting expired sessions
						Iterator<PlayerSession> iterator = playerSessions.iterator();
						while (iterator.hasNext()) {
							ToolsPlayerSession session = (ToolsPlayerSession) iterator.next();

							if (System.currentTimeMillis() - session.getCreateTime() > 86400000) { // 86400000 = 1000 * 60 * 60 * 24 (24 hours)
								iterator.remove();
							}
						}
					}
				},
				3600000, // 3600000 = 1000 * 60 * 60 (1 hour)
				3600000);

		// registering listener
		Bukkit.getPluginManager().registerEvents(new ToolsListener(), SBFPlugin.getPlugin());
	}

	@Override
	public void disable() throws Exception {
		// deiniting confs
		badWordsConf.saveConf();
		badWordsConf.deinitConf();
		kitConf.deinitConf();
		badWordsConf = null;
		kitConf = null;

		// cancelling tasks
		if (taskID0 != -1) {
			Bukkit.getScheduler().cancelTask(taskID0);
			taskID0 = -1;
		}
	}

	@Override
	protected void registerAllCommands() {
		commands = new Command[] {
				new BadWordsCommand(),
				new ChangeSeedCommand(),
				new GCCommand(),
				new GetIPCommand(),
				new TNTCommand(),
				new DrinkCommand(),
				new KitCommand(),
				new ToSpawnCommand(),
				new ListEntitiesCommand(),
				new MSKTimeCommand(),
				new FreeCommand(),
		};
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
		if (getPlayerSession(player) == null) {
			playerSessions.add(kitConf.loadPlayer(new ToolsPlayerSession(player, player.getPlayer().getAddress().getAddress().getHostAddress())));
		} else {
			((ToolsPlayerSession) getPlayerSession(player)).setIP(player.getAddress().getAddress().getHostAddress());
		}
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
	}

	@Override
	public int getID() {
		return 1;
	}

	@Override
	public String getName() {
		return "Tools";
	}

	public boolean isNoTntForAll() {
		return noTntForAll;
	}

	public void setNoTntForAll(boolean noTntForAll) {
		this.noTntForAll = noTntForAll;
	}

	public boolean isBadWordsOff() {
		return badWordsOff;
	}

	public void setBadWordsOff(boolean badWordsOff) {
		this.badWordsOff = badWordsOff;
	}

	public String getReportMessage() {
		return reportMessage;
	}

	public void setReportMessage(String reportMessage) {
		this.reportMessage = reportMessage;
	}

	public Collection<String> getBadWords() {
		return badWords;
	}

	public BadWordsConf getBadWordsConf() {
		return badWordsConf;
	}

	public KitConf getKitConf() {
		return kitConf;
	}
}
