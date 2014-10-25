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

package net.sbfmc.modules.mooncraft;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.Module;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.mooncraft.commands.AirRegionCommand;
import net.sbfmc.modules.mooncraft.commands.BuyCommand;
import net.sbfmc.modules.mooncraft.commands.GenChangeCommand;
import net.sbfmc.modules.mooncraft.commands.RegenCommand;
import net.sbfmc.modules.mooncraft.commands.SaveConfCommand;
import net.sbfmc.modules.mooncraft.commands.StartKitCommand;
import net.sbfmc.modules.mooncraft.conf.AirRegionConf;
import net.sbfmc.modules.mooncraft.conf.AirRegionNotifConf;
import net.sbfmc.modules.mooncraft.conf.MooncraftPlayersConf;
import net.sbfmc.modules.mooncraft.gen.NetherGeneration;
import net.sbfmc.modules.mooncraft.gen.NormalGeneration;
import net.sbfmc.world.selection.CoordXYZ;
import net.sbfmc.world.selection.Region;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

public class MooncraftModule extends Module {
	private MooncraftPlayersConf playersConf;
	private AirRegionConf airRegionConf;
	private AirRegionNotifConf airRegionNotifConf;
	private Collection<Region> airRegions = new HashSet<Region>();
	private HashMap<Integer, CoordXYZ> airRegionNotifs = new HashMap<Integer, CoordXYZ>();
	private NormalGeneration normalGeneration;
	private NetherGeneration netherGeneration;
	private Collection<Recipe> recipes;
	private int taskID0 = -1;
	private int taskID1 = -1;
	private int taskID2 = -1;
	private int taskID3 = -1;

	@SuppressWarnings("deprecation")
	@Override
	public void enable() throws Exception {
		// initializing confs
		playersConf = new MooncraftPlayersConf();
		airRegionConf = new AirRegionConf();
		airRegionNotifConf = new AirRegionNotifConf();
		playersConf.initConf();
		airRegionConf.initConf();
		airRegionNotifConf.initConf();
		airRegionConf.loadConf();
		airRegionNotifConf.loadConf();

		// initializing generators
		normalGeneration = new NormalGeneration();
		netherGeneration = new NetherGeneration();
		normalGeneration.initGeneration();
		netherGeneration.initGeneration();

		// starting timers
		taskID0 = Bukkit.getScheduler().scheduleSyncRepeatingTask(SBFPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				MooncraftUtils.updateTime();
				MooncraftUtils.calculateChickenTerminators();

				filterPlayers();

				for (PlayerSession sessionRaw : playerSessions) {
					MooncraftPlayerSession session = (MooncraftPlayerSession) sessionRaw;

					if (session.getPlayer().getPlayer().isDead()) {
						continue;
					}

					MooncraftUtils.addItemEffect(session);
				}
			}
		}, 1, 20); // 1 sec
		taskID1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(SBFPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				MooncraftUtils.checkMobAir();

				filterPlayers();

				for (PlayerSession sessionRaw : playerSessions) {
					MooncraftPlayerSession session = (MooncraftPlayerSession) sessionRaw;

					if (session.getPlayer().getPlayer().isDead()) {
						continue;
					}

					MooncraftUtils.checkPlayerAir(session);
				}
			}
		}, 1, 150); // 150 = 5 * 60 * 20 (5 sec)
		taskID2 = Bukkit.getScheduler().scheduleAsyncRepeatingTask(SBFPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				filterPlayers();

				for (PlayerSession sessionRaw : playerSessions) {
					MooncraftPlayerSession session = (MooncraftPlayerSession) sessionRaw;

					if (session.getPlayer().getPlayer().isDead()) {
						continue;
					}

					MooncraftUtils.addPotion(session);
					MooncraftUtils.updatePlayersStats(session);
					MooncraftUtils.updatePDA(session);
					MooncraftUtils.repairItemNames(session);
				}
			}
		}, 1, 200); // 200 = 10 * 20 (10 sec)
		taskID3 = Bukkit.getScheduler().scheduleAsyncRepeatingTask(SBFPlugin.getPlugin(), new Runnable() {
			@Override
			public void run() {
				filterPlayers();

				for (PlayerSession session : playerSessions) {
					try {
						playersConf.savePlayer(session);
					} catch (IOException err) {
						DebugUtils.debugError("MOON", "Can't save data of " + session.getPlayer().getName(), err);
					}
				}
			}
		}, 1, 12000); // 12000 = 10 * 60 * 20 (10 min)

		// registering listener
		Bukkit.getPluginManager().registerEvents(new MooncraftListener(), SBFPlugin.getPlugin());

		// registering custom recipes
		recipes = MooncraftRecipes.generateRecipes();
		for (Recipe recipe : recipes) {
			Bukkit.addRecipe(recipe);
		}
	}

	@Override
	public void disable() throws Exception {
		// deinitializing confs
		for (PlayerSession session : playerSessions) {
			playersConf.savePlayer(session);
		}
		airRegionConf.saveConf();
		airRegionNotifConf.saveConf();
		playersConf.deinitConf();
		airRegionConf.deinitConf();
		airRegionNotifConf.deinitConf();
		playersConf = null;
		airRegionConf = null;
		airRegionNotifConf = null;

		// deinitializing generators
		normalGeneration.deinitGeneration();
		netherGeneration.deinitGeneration();
		normalGeneration = null;
		netherGeneration = null;

		// clearing lists
		recipes.clear();
		airRegions.clear();

		// canceling tasks
		if (taskID0 != -1) {
			Bukkit.getScheduler().cancelTask(taskID0);
			taskID0 = -1;
		}
		if (taskID1 != -1) {
			Bukkit.getScheduler().cancelTask(taskID1);
			taskID1 = -1;
		}
		if (taskID2 != -1) {
			Bukkit.getScheduler().cancelTask(taskID2);
			taskID2 = -1;
		}
		if (taskID3 != -1) {
			Bukkit.getScheduler().cancelTask(taskID3);
			taskID3 = -1;
		}
	}

	@Override
	protected void registerAllCommands() {
		commands = new Command[] {
				new AirRegionCommand(),
				new BuyCommand(),
				new GenChangeCommand(),
				new SaveConfCommand(),
				new RegenCommand(),
				new StartKitCommand(),
		};
	}

	@Override
	public PlayerSession getPlayerSession(Player player) {
		filterPlayers();
		return super.getPlayerSession(player);
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
		if (player.getWorld().getName().startsWith(getName())) {
			player.setTexturePack("http://sbfmc.net/files/mooncraft_textures.zip");
			playerSessions.add(playersConf.loadPlayer(player));
		} else {
			player.setTexturePack("http://sbfmc.net/files/default_textures.zip");
		}

		if (checkPermisson(player, "notif")) {
			fireCommand(player, "airregion", new String[] { "lsnotif" });
		}
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
		PlayerSession session = getPlayerSession(player);

		if (session == null) {
			return;
		}

		playersConf.savePlayer(session);
		session.getPlayer().getPlayer().setTexturePack("http://sbfmc.net/files/default_textures.zip");
		playerSessions.remove(session);
	}

	public void filterPlayers() {
		try {
			Iterator<PlayerSession> iterator = playerSessions.iterator();

			while (iterator.hasNext()) {
				PlayerSession session = iterator.next();
				if (!session.getPlayer().getPlayer().getWorld().getName().startsWith(getName())) {
					playersConf.savePlayer(session);
					session.getPlayer().getPlayer().setTexturePack("http://sbfmc.net/files/default_textures.zip");
					iterator.remove();
				}
			}
		} catch (Exception err) {
			DebugUtils.debugError("MOON", "Can't filter player sessions", err);
		}
	}

	@Override
	public int getID() {
		return 2;
	}

	@Override
	public String getName() {
		return "Mooncraft";
	}

	@Override
	public String getAlias() {
		return "moon";
	}

	public MooncraftPlayersConf getPlayersConf() {
		return playersConf;
	}

	public AirRegionConf getAirRegionConf() {
		return airRegionConf;
	}

	public Collection<Region> getAirRegions() {
		return airRegions;
	}

	public NetherGeneration getNetherGeneration() {
		return netherGeneration;
	}

	public NormalGeneration getNormalGeneration() {
		return normalGeneration;
	}

	public HashMap<Integer, CoordXYZ> getAirRegionNotifs() {
		return airRegionNotifs;
	}

	public AirRegionNotifConf getAirRegionNotifConf() {
		return airRegionNotifConf;
	}

	public Collection<Recipe> getRecipes() {
		return recipes;
	}

	@Override
	public PlayerSession getPlayerSession(OfflinePlayer player) {
		PlayerSession session = super.getPlayerSession(player);
		if (session == null) {
			try {
				session = playersConf.loadPlayer(player);
			} catch (IOException err) {
			}
		}
		return session;
	}
}
