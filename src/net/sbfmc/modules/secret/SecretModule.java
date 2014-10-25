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

package net.sbfmc.modules.secret;

import java.util.Calendar;
import java.util.TimeZone;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.logging.LogFilter;
import net.sbfmc.module.Module;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.secret.commands.AddCommand;
import net.sbfmc.modules.secret.commands.ListCommand;
import net.sbfmc.modules.secret.commands.RemoveCommand;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.virtualplayers.VirtualPlayer;
import net.sbfmc.virtualplayers.VirtualPlayersManager;
import net.sbfmc.virtualplayers.light.LightVirtualPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SecretModule extends Module {
	public static final String[] NICKS = {
			"losaric",
			"niniol",
			"icofolo",
			"qet",
			"kRoll",
			"weeeLOL",
			"XXXsuper",
			"Bro1134",
			"kirillPLUS",
			"yellowKitty",
			"Cava15",
			"Malino4ka",
			"Popugai100500",
			"Ololosh13",
			"Master009",
			"Kseniya",
			"Gera546",
			"Neo444",
			"4ikago",
			"Elena20",
			"anyaApple",
			"SovaMasya",
			"Orange485",
			"krytoipacan560",
			"Leshik555",
			"titili24",
			"Lesya",
			"nyasha",
			"god",
			"fad",
			"ddd192",
			"itiki",
			"werv",
			"Dedroid",
			"NagiBator666",
			"Nogano228",
			"Freelancer",
			"Ferry",
			"Prophet",
			"Flabber",
			"pusya",
			"LiLu",
			"Betty",
			"Kari",
			"Milana",
			"JaguaR",
			"V1taL1k",
			"flashforward",
			"GraFF",
			"Dron",
			"EVERLAST",
			"JustPro",
			"SexyBOY",
			"SkiiZz",
			"zloy",
			"Kisa",
			"Swaggagirl",
			"Meow",
			"Marmeladka",
			"Bandit",
			"BiGGieICE",
			"BradPit",
			"noFake",
			"Niggativ",
			"Greddy",
			"Lirika",
			"KapitoshkaHi",
			"fksslfhfhfhdksk",
			"iGUF",
			"Mimi",
			"andied",
			"kandida",
			"pro100psix",
			"CAHEK",
			"maXXX",
			"ANTIshkolniK",
			"odmin",
			"Girya",
			"Blik",
			"lom",
	};
	public static final String[] DEATH_MESSEAGES = {
			" died",
			" was squashed by a falling anvil",
			" was pricked to death",
			" walked into a cactus whilst trying to escape Zombie",
			" was shot by arrow",
			" drowned",
			" drowned whilst trying to escape Zombie",
			" blew up",
			" was blown up by Creeper",
			" hit the ground too hard",
			" fell off a ladder",
			" fell off some vines",
			" fell out of the water",
			" fell from a high place",
			" fell into a patch of fire",
			" fell into a patch of cacti",
			" was doomed to fall",
			" was shot off some vines by Skeleton",
			" was shot off a ladder by Skeleton",
			" was blown from a high place by Creeper",
			" went up in flames",
			" burned to death",
			" was burnt to a crisp whilst fighting Zombie",
			" walked into a fire whilst fighting Zombie",
			" was slain by Spider",
			" was shot by Spider",
			" was fireballed by Ghast",
			" got finished off by Spider",
			" tried to swim in lava",
			" tried to swim in lava while trying to escape Zombie",
			" was killed by magic",
			" starved to death",
			" suffocated in a wall",
			" was killed while trying to hurt Zombie",
			" withered away",
	};
	public static final int MAX_PLAYERS = 15;
	public static final int MAX_RANDOM_VALUE = 150;

	private SecretLogFilter secretLogFilter;
	private int taskID0 = -1;

	@Override
	public void enable() throws Exception {

		taskID0 = Bukkit.getScheduler().scheduleSyncRepeatingTask(
				SBFPlugin.getPlugin(),
				new Runnable() {
					@Override
					public void run() {
						int randomValue = GeneralUtils.getRandom().nextInt(MAX_RANDOM_VALUE);

						// adding new virtual player
						if (randomValue >= MAX_PLAYERS * 2 && randomValue < MAX_PLAYERS * 3 - 5) {
							if (VirtualPlayersManager.getPlayersCount() < MAX_PLAYERS) {
								int hour = Calendar.getInstance(TimeZone.getTimeZone("GMT+4")).get(Calendar.HOUR_OF_DAY);
								if (hour > 21 && hour < 10) {
									return;
								}
								randomValue = GeneralUtils.getRandom().nextInt(NICKS.length);
								if (!VirtualPlayersManager.contains(NICKS[randomValue])) {
									VirtualPlayersManager.addPlayer(new LightVirtualPlayer(NICKS[randomValue]));
								}
							}
							return;
						}

						// simulate death
						if (randomValue >= MAX_PLAYERS && randomValue < MAX_PLAYERS * 2) {
							if (GeneralUtils.getRandom().nextBoolean()) {
								int i = MAX_PLAYERS;
								for (VirtualPlayer player : VirtualPlayersManager.getPlayers()) {
									if (i++ == randomValue) {
										if (!player.getName().equals("nobody")) {
											GeneralUtils.sayToAll(player.getName() + DEATH_MESSEAGES[GeneralUtils.getRandom().nextInt(DEATH_MESSEAGES.length)]);
										}
										break;
									}
								}
							}
							return;
						}

						// removing virtual player
						if (randomValue < MAX_PLAYERS) {
							int i = 0;
							for (VirtualPlayer player : VirtualPlayersManager.getPlayers()) {
								if (i++ == randomValue) {
									if (!player.getName().equals("nobody")) {
										VirtualPlayersManager.removePlayer(player);
									}
									break;
								}
							}
							return;
						}
					}
				},
				200, // 200 = 10 * 20 (10 sec)
				200);

		secretLogFilter = new SecretLogFilter();
		LogFilter.registerFilter(secretLogFilter);
	}

	@Override
	public void disable() throws Exception {
		if (taskID0 != -1) {
			Bukkit.getScheduler().cancelTask(taskID0);
			taskID0 = -1;
		}

		LogFilter.unregisterFilter(secretLogFilter);
		secretLogFilter = null;
	}

	@Override
	protected void registerAllCommands() {
		commands = new Command[] {
				new AddCommand(),
				new RemoveCommand(),
				new ListCommand(),
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
		return 12;
	}

	@Override
	public String getName() {
		return "SECRET";
	}
}
