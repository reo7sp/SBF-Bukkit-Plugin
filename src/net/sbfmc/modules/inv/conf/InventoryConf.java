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

package net.sbfmc.modules.inv.conf;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import net.minecraft.server.v1_5_R3.MinecraftServer;
import net.minecraft.server.v1_5_R3.WorldServer;
import net.sbfmc.def.SBFPlugin;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.conf.PlayersConf;
import net.sbfmc.utils.CharsetConventer;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_5_R3.CraftServer;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;

public class InventoryConf extends PlayersConf {
	private String worldName;

	public InventoryConf(String worldName) {
		this.worldName = worldName;
	}

	public InventoryConf(World world) {
		this(world.getName());
	}

	@Override
	public PlayerSession loadPlayer(OfflinePlayer player) throws IOException {
		DebugUtils.debug("INV", "Loading inventory of \"" + player.getName() + "\" in \"" + worldName + "\"");

		File file = new File(confFolder, player.getName() + ".yml");
		CharsetConventer.fileToUTF8(file);
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
		boolean invExists;

		// checking if inv exists
		if (file.exists()) {
			invExists = conf.isSet("items") && conf.isSet("armor");
		} else {
			invExists = false;
		}

		// loading inv
		if (invExists) {
			ItemStack[] items = new ItemStack[player.getPlayer().getInventory().getSize()];
			ItemStack[] armor = new ItemStack[4];

			// getting items
			{
				ConfigurationSection itemsSection = conf.getConfigurationSection("items");
				Set<String> idStrings = itemsSection.getKeys(false);
				for (String idString : idStrings) {
					items[Integer.parseInt(idString)] = itemsSection.getItemStack(idString);
				}
			}

			// getting armor
			{
				ConfigurationSection armorSection = conf.getConfigurationSection("armor");
				Set<String> idStrings = armorSection.getKeys(false);
				for (String idString : idStrings) {
					armor[Integer.parseInt(idString)] = armorSection.getItemStack(idString);
				}
			}

			// setting inventory
			player.getPlayer().getInventory().setContents(items);
			player.getPlayer().getInventory().setArmorContents(armor);

			// setting other (health, food, xp...)
			player.getPlayer().setHealth(conf.getInt("health", 20));
			player.getPlayer().setFoodLevel(conf.getInt("food", 20));
			player.getPlayer().setLevel(conf.getInt("level", 0));
			player.getPlayer().setExp((float) conf.getDouble("exp", 0));
		} else {
			CraftServer server = (CraftServer) Bukkit.getServer();
			MinecraftServer mcServer = server.getServer();

			// getting inventory from world data
			for (WorldServer worldServer : mcServer.worlds) {
				if (worldServer.worldData.getName().equalsIgnoreCase(worldName)) {
					worldServer.getDataManager().getPlayerFileData().load(((CraftPlayer) player).getHandle());
				}
			}

			// saving inventory
			savePlayer(player);
		}

		return null;
	}

	@Override
	public PlayerSession loadPlayer(PlayerSession session) throws IOException {
		loadPlayer(session.getPlayer());

		return null;
	}

	@Override
	public void savePlayer(OfflinePlayer player) throws IOException {
		DebugUtils.debug("INV", "Saving inventory of \"" + player.getName() + "\" in \"" + worldName + "\"");

		File file = new File(confFolder, player.getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);
		ItemStack[] items = player.getPlayer().getInventory().getContents();
		ItemStack[] armor = player.getPlayer().getInventory().getArmorContents();

		// saving items
		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];

			// using chance repairing item name encoding
			GeneralUtils.repairItemNameEncoding(item);

			conf.set("items." + i, item);
		}

		// saving armor
		for (int i = 0; i < armor.length; i++) {
			ItemStack item = armor[i];

			// using chance repairing item name encoding
			GeneralUtils.repairItemNameEncoding(item);

			conf.set("armor." + i, item);
		}

		// saving other (health, food, xp...)
		conf.set("health", player.getPlayer().getHealth());
		conf.set("food", player.getPlayer().getFoodLevel());
		conf.set("level", player.getPlayer().getLevel());
		conf.set("exp", player.getPlayer().getExp());

		// saving conf
		conf.save(file);
	}

	@Override
	public void savePlayer(PlayerSession session) throws IOException {
		savePlayer(session.getPlayer());
	}

	@Override
	public void initConf() throws IOException {
		confFolder = new File(SBFPlugin.getPlugin().getDataFolder(), worldName + "_invs");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFolder = null;
	}

	@Override
	public int getModuleID() {
		return 4;
	}

	public String getWorldName() {
		return worldName;
	}
}
