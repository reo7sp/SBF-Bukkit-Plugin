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

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class MooncraftRecipes {
	static Collection<Recipe> generateRecipes() {
		Collection<Recipe> recipes = new HashSet<Recipe>();

		// leather helmet
		{
			ItemStack item = new ItemStack(298);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("###", "#H#");
			recipe.setIngredient('#', Material.LEATHER);
			recipe.setIngredient('H', Material.THIN_GLASS);
			recipes.add(recipe);
		}

		// leather chestplate
		{
			ItemStack item = new ItemStack(299);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("# #", "#H#", "###");
			recipe.setIngredient('#', Material.LEATHER);
			recipe.setIngredient('H', Material.GLASS);
			recipes.add(recipe);
		}

		// metal helmet
		{
			ItemStack item = new ItemStack(306);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("###", "#H#");
			recipe.setIngredient('#', Material.IRON_INGOT);
			recipe.setIngredient('H', Material.THIN_GLASS);
			recipes.add(recipe);
		}

		// metal chestplate
		{
			ItemStack item = new ItemStack(307);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("# #", "#H#", "###");
			recipe.setIngredient('#', Material.IRON_INGOT);
			recipe.setIngredient('H', Material.GLASS);
			recipes.add(recipe);
		}

		// gold helmet
		{
			ItemStack item = new ItemStack(314);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("###", "#H#");
			recipe.setIngredient('#', Material.GOLD_INGOT);
			recipe.setIngredient('H', Material.THIN_GLASS);
			recipes.add(recipe);
		}

		// gold chestplate
		{
			ItemStack item = new ItemStack(315);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("# #", "#H#", "###");
			recipe.setIngredient('#', Material.GOLD_INGOT);
			recipe.setIngredient('H', Material.GLASS);
			recipes.add(recipe);
		}

		// diamond helmet
		{
			ItemStack item = new ItemStack(310);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("###", "#H#");
			recipe.setIngredient('#', Material.DIAMOND);
			recipe.setIngredient('H', Material.THIN_GLASS);
			recipes.add(recipe);
		}

		// diamond chestplate
		{
			ItemStack item = new ItemStack(311);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("# #", "#H#", "###");
			recipe.setIngredient('#', Material.DIAMOND);
			recipe.setIngredient('H', Material.GLASS);
			recipes.add(recipe);
		}

		// air creator
		{
			ItemStack item = new ItemStack(19);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Воздухогенератор");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("@", "H", "#");
			recipe.setIngredient('@', Material.REDSTONE);
			recipe.setIngredient('H', Material.NETHER_STAR);
			recipe.setIngredient('#', Material.ENDER_CHEST);
			recipes.add(recipe);
		}

		// arrow
		{
			ItemStack item = new ItemStack(262, 16);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Пуля");
			item.setItemMeta(itemMeta);
			ShapelessRecipe recipe = new ShapelessRecipe(item);
			recipe.addIngredient(Material.IRON_INGOT);
			recipe.addIngredient(Material.SULPHUR);
			recipes.add(recipe);
		}

		// bow
		{
			ItemStack item = new ItemStack(261);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Пистолет");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("###", " @#");
			recipe.setIngredient('@', Material.SULPHUR);
			recipe.setIngredient('#', Material.IRON_INGOT);
			recipes.add(recipe);
		}

		// emerald sword
		{
			ItemStack item = new ItemStack(272);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Изумрудный меч");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("#", "#", "@");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.EMERALD);
			recipes.add(recipe);
		}

		// emerald shovel
		{
			ItemStack item = new ItemStack(273);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Изумрудная лопата");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("#", "@", "@");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.EMERALD);
			recipes.add(recipe);
		}

		// emerald pickaxe
		{
			ItemStack item = new ItemStack(274);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Изумрудная кирка");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("###", " @ ", " @ ");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.EMERALD);
			recipes.add(recipe);
		}

		// emerald axe
		{
			ItemStack item = new ItemStack(275);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Изумрудный топор");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("##", "#@", " @");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.EMERALD);
			recipes.add(recipe);
		}

		// emerald hoe
		{
			ItemStack item = new ItemStack(291);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Изумрудная мотыга");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("##", " @", " @");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.EMERALD);
			recipes.add(recipe);
		}

		// enderstone sword
		{
			ItemStack item = new ItemStack(268);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковый меч");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("#", "#", "@");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.ENDER_STONE);
			recipes.add(recipe);
		}

		// enderstone shovel
		{
			ItemStack item = new ItemStack(269);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковая лопата");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("#", "@", "@");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.ENDER_STONE);
			recipes.add(recipe);
		}

		// enderstone pickaxe
		{
			ItemStack item = new ItemStack(270);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковая кирка");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("###", " @ ", " @ ");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.ENDER_STONE);
			recipes.add(recipe);
		}

		// enderstone axe
		{
			ItemStack item = new ItemStack(271);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковый топор");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("##", "#@", " @");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.ENDER_STONE);
			recipes.add(recipe);
		}

		// enderstone hoe
		{
			ItemStack item = new ItemStack(290);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковая мотыга");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("##", " @", " @");
			recipe.setIngredient('@', Material.STICK);
			recipe.setIngredient('#', Material.ENDER_STONE);
			recipes.add(recipe);
		}

		// gunpowder
		{
			ItemStack item = new ItemStack(289, 3);
			ShapelessRecipe recipe = new ShapelessRecipe(item);
			recipe.addIngredient(Material.SUGAR);
			recipe.addIngredient(Material.COAL);
			recipe.addIngredient(Material.GLOWSTONE_DUST);
			recipes.add(recipe);
		}

		// battery
		{
			ItemStack item = new ItemStack(369, 3);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Батарейка");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("###", "#@#", "###");
			recipe.setIngredient('@', Material.REDSTONE_BLOCK);
			recipe.setIngredient('#', Material.FEATHER);
			recipes.add(recipe);
		}

		// iron nugget
		{
			ItemStack item = new ItemStack(288, 9);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Железный самородок");
			item.setItemMeta(itemMeta);
			ShapelessRecipe recipe = new ShapelessRecipe(item);
			recipe.addIngredient(Material.IRON_INGOT);
			recipes.add(recipe);
		}

		// iron ingot
		{
			ItemStack item = new ItemStack(265);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("###", "###", "###");
			recipe.setIngredient('#', Material.FEATHER);
			recipes.add(recipe);
		}

		// iron stick
		{
			ItemStack item = new ItemStack(280);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RESET + "Железная палка");
			item.setItemMeta(itemMeta);
			ShapedRecipe recipe = new ShapedRecipe(item);
			recipe.shape("#", "#");
			recipe.setIngredient('#', Material.FEATHER);
			recipes.add(recipe);
		}

		return recipes;
	}
}
