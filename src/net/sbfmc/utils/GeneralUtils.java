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

package net.sbfmc.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.sbfmc.logging.DebugUtils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import uk.org.whoami.authme.cache.auth.PlayerCache;

public class GeneralUtils {
	private static final Random RANDOM = new Random();

	public static String getHoursString(long time) {
		String string = "" + ((time / 1000 + 8) % 24);
		if (string.length() < 2) {
			string = "0" + string;
		}
		return string;
	}

	public static String getMinutesString(long time) {
		String string = "" + (60 * (time % 1000) / 1000);
		if (string.length() < 2) {
			string = "0" + string;
		}
		return string;
	}

	public static boolean recipesEquals(Recipe recipe0Raw, Recipe recipe1Raw) {
		if (recipe0Raw instanceof ShapedRecipe && recipe1Raw instanceof ShapedRecipe) {
			ShapedRecipe recipe0 = (ShapedRecipe) recipe0Raw;
			ShapedRecipe recipe1 = (ShapedRecipe) recipe1Raw;
			char[] ingredientChars0 = new char[9];
			char[] ingredientChars1 = new char[9];

			for (int i = 0; i < recipe0.getShape().length; i++) {
				String row = recipe0.getShape()[i];
				for (int j = 0; j < row.length(); j++) {
					ingredientChars0[i * 3 + j] = row.charAt(j);
				}
			}
			for (int i = 0; i < recipe1.getShape().length; i++) {
				String row = recipe1.getShape()[i];
				for (int j = 0; j < row.length(); j++) {
					ingredientChars1[i * 3 + j] = row.charAt(j);
				}
			}

			for (int i = 0; i < 9; i++) {
				ItemStack item0 = recipe0.getIngredientMap().get(ingredientChars0[i]);
				ItemStack item1 = recipe1.getIngredientMap().get(ingredientChars1[i]);

				if (item0 == null || item1 == null) {
					if (item0 == null && item1 == null) {
						continue;
					} else {
						return false;
					}
				}

				if (item0.getTypeId() != item1.getTypeId()) {
					return false;
				}
			}

			return true;
		} else if (recipe0Raw instanceof ShapelessRecipe && recipe1Raw instanceof ShapelessRecipe) {
			ShapelessRecipe recipe0 = (ShapelessRecipe) recipe0Raw;
			ShapelessRecipe recipe1 = (ShapelessRecipe) recipe1Raw;

			int emptyCells0 = 0;
			int emptyCells1 = 0;

			for (ItemStack itemStack : recipe0.getIngredientList()) {
				if (itemStack.getTypeId() == 0) {
					emptyCells0++;
				}
			}
			for (ItemStack itemStack : recipe1.getIngredientList()) {
				if (itemStack.getTypeId() == 0) {
					emptyCells1++;
				}
			}

			if (recipe0.getIngredientList().size() - emptyCells0 != recipe1.getIngredientList().size() - emptyCells1) {
				return false;
			}

			for (ItemStack item0 : recipe0.getIngredientList()) {
				boolean exists = false;

				for (ItemStack item1 : recipe1.getIngredientList()) {
					if (item0.getTypeId() == item1.getTypeId()) {
						exists = true;
						break;
					}
				}

				if (!exists) {
					return false;
				}
			}

			return true;
		} else {
			ShapedRecipe shapedRecipe = null;
			ShapelessRecipe shapelessRecipe = null;

			if (recipe0Raw instanceof ShapedRecipe && recipe1Raw instanceof ShapelessRecipe) {
				shapedRecipe = (ShapedRecipe) recipe0Raw;
				shapelessRecipe = (ShapelessRecipe) recipe1Raw;
			} else if (recipe0Raw instanceof ShapelessRecipe && recipe1Raw instanceof ShapedRecipe) {
				shapedRecipe = (ShapedRecipe) recipe1Raw;
				shapelessRecipe = (ShapelessRecipe) recipe0Raw;
			}

			char[] ingredientChars = new char[9];
			for (int i = 0; i < shapedRecipe.getShape().length; i++) {
				String row = shapedRecipe.getShape()[i];
				for (int j = 0; j < row.length(); j++) {
					ingredientChars[i * 3 + j] = row.charAt(j);
				}
			}

			for (int i = 0; i < 9; i++) {
				ItemStack item0 = shapedRecipe.getIngredientMap().get(ingredientChars[i]);

				if (item0 == null || item0.getTypeId() == 0) {
					continue;
				}

				for (ItemStack item1 : shapelessRecipe.getIngredientList()) {
					if (item1.getTypeId() == 0) {
						continue;
					}
					if (item0.getTypeId() != item1.getTypeId()) {
						return false;
					}
				}
			}
		}

		return false;
	}

	public static void repairItemNameEncoding(ItemStack item) {
		try {
			if (!item.hasItemMeta()) {
				return;
			}

			ItemMeta itemMeta = item.getItemMeta();

			// name
			itemMeta.setDisplayName(CharsetConventer.toUTF8(item.getItemMeta().getDisplayName()));

			// lore
			List<String> existinglore = itemMeta.getLore();
			List<String> newLore = new ArrayList<String>();
			for (String string : existinglore) {
				newLore.add(CharsetConventer.toUTF8(string));
			}
			itemMeta.setLore(newLore);

			item.setItemMeta(itemMeta);
		} catch (Exception err) {
		}
	}

	public static String[] listContentsOfZipFile(String file) {
		return listContentsOfZipFile(new File(file));
	}

	public static String[] listContentsOfZipFile(File file) {
		Collection<String> contents = new HashSet<String>();

		try {
			ZipFile zipFile = new ZipFile(file);

			Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();

			while (zipEntries.hasMoreElements()) {
				contents.add(zipEntries.nextElement().getName());
			}

			zipFile.close();
		} catch (IOException err) {
			DebugUtils.debugError("CORE", "Can't get list of contents of zip file", err);
		}

		return contents.toArray(new String[0]);
	}

	public static boolean startsWith(String str1Raw, String str2Raw) {
		String str1 = str1Raw.toLowerCase();
		String str2 = str2Raw.toLowerCase();

		for (int i = 0; i < str1.length() && i < str2.length(); i++) {
			if (str1.charAt(i) != str2.charAt(i)) {
				return false;
			}
		}

		return true;
	}

	public static void teleportToWorld(Player player, String world) {
		teleportToWorld(player, Bukkit.getWorld(world));
	}

	public static void teleportToWorld(Player player, World world) {
		player.teleport(world.getSpawnLocation());
	}

	public static boolean checkAuth(Player player) {
		return checkAuth(player.getName());
	}

	public static boolean checkAuth(String name) {
		return !isPluginEnabled("AuthMe") || PlayerCache.getInstance().isAuthenticated(name.toLowerCase());
	}

	public static void setField(Object object, String name, Object value) {
		try {
			Field field = object.getClass().getDeclaredField(name);
			boolean isPrivate = !field.isAccessible();
			if (isPrivate) {
				field.setAccessible(true);
			}
			field.set(object, value);
			if (isPrivate) {
				field.setAccessible(false);
			}
		} catch (Exception err) {
		}
	}

	public static void sayToAll(String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(message);
		}
	}

	public static boolean isPluginEnabled(String name) {
		return Bukkit.getPluginManager().isPluginEnabled(name);
	}

	public static Random getRandom() {
		return RANDOM;
	}
}
