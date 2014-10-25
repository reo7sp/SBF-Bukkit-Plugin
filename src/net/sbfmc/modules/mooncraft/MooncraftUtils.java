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

import net.minecraft.server.v1_5_R3.EntityChicken;
import net.minecraft.server.v1_5_R3.EntityLiving;
import net.minecraft.server.v1_5_R3.PathEntity;
import net.sbfmc.def.SBFPlugin;
import net.sbfmc.utils.BookItem;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.world.particle.Particle;
import net.sbfmc.world.particle.ParticleSpawner;
import net.sbfmc.world.selection.CoordXYZ;
import net.sbfmc.world.selection.Region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftChicken;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MooncraftUtils {
	private static MooncraftModule module;

	public static void updatePlayersStats(MooncraftPlayerSession session) {
		if (module == null) {
			module = (MooncraftModule) SBFPlugin.getModule(2);
		}
		if (module == null) {
			return;
		}

		if (session.getPlayer().getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}

		if (!GeneralUtils.checkAuth(session.getPlayer().getPlayer())) {
			return;
		}

		if (session.getPlayer().getPlayer().getFoodLevel() != 18) {
			session.getPlayer().getPlayer().setFoodLevel(18);
		}

		session.setFood(session.getFood() - 0.1f);
		session.setWater(session.getWater() - 0.1f);
		if (GeneralUtils.getRandom().nextInt(10) < 1) {
			session.setPower(session.getPower() - 0.1f);
		}
		if (session.getBleeding() != 0) {
			for (int j = 0; j < session.getBleeding(); j++) {
				session.getPlayer().getPlayer().playEffect(session.getPlayer().getPlayer().getLocation(), Effect.POTION_BREAK, 0x5);
			}
		}
		if (session.getBleeding() == 1) {
			session.setBlood(session.getBlood() - 0.1f);
		} else if (session.getBleeding() == 2) {
			session.setBlood(session.getBlood() - 0.5f);
		} else if (session.getBleeding() == 3) {
			session.setBlood(session.getBlood() - 2);
		} else if (session.getBleeding() == 4) {
			session.setBlood(session.getBlood() - 10);
		}

		if (session.getFood() < 1 || session.getWater() < 1 || session.getBlood() < 1) {
			session.getPlayer().getPlayer().damage(5);
		}
	}

	private final static String finalPage0 = "" +
			"                     " + ChatColor.MAGIC + "%H:%M\n" +
			ChatColor.RESET + "-------------------\n" +
			"\n" +
			"\n" +
			"\n" +
			ChatColor.RED + "  БАТАРЕЯ РАЗРЯЖЕНА\n";
	private final static String finalPage1 = "" +
			"                     %H:%M\n" +
			"-------------------\n" +
			" БЛОКИРОВКА       " + ChatColor.GRAY + "гла\n" +
			ChatColor.BLACK + "\n" +
			"\n" +
			"\n" +
			" ____\n" +
			" |     |\n" +
			" |---|\n" +
			" | o  |   " + ChatColor.RED + "Заблокирован\n" +
			ChatColor.BLACK + " ----\n" +
			"\n" +
			" разблокировать ->\n";
	private final static String finalPage2 = "" +
			"                     %H:%M\n" +
			"-------------------\n" +
			" ГЛАВНЫЙ ЭКРАН   " + ChatColor.GRAY + "сос\n" +
			ChatColor.BLACK + "\n" +
			" %N уведомлений\n" +
			"\n" +
			"               \\ _ /   \n" +
			"       /\\    - (_) - \n" +
			"      /  \\     / | \\    \n" +
			"     /    \\ /\\        /\n" +
			"    /      /   \\     /  \n" +
			"__/      /      \\_/       \n" +
			"\n";
	private final static String finalPage3 = "" +
			"                     %H:%M\n" +
			"-------------------\n" +
			" СОСТОЯНИЕ\n" +
			"\n" +
			" вода=%W\n" +
			" еда=%F\n" +
			" энергия=%P\n" +
			" кислород=%O\n" +
			" кровь=%B\n" +
			" кровотечение=%b\n";

	public static void updatePDA(MooncraftPlayerSession session) {
		if (module == null) {
			module = (MooncraftModule) SBFPlugin.getModule(2);
		}
		if (module == null) {
			return;
		}

		if (!GeneralUtils.checkAuth(session.getPlayer().getPlayer())) {
			return;
		}

		String page1 = null, page2 = null, page3 = null;
		Inventory inventory = session.getPlayer().getPlayer().getInventory();
		String hours = GeneralUtils.getHoursString(session.getPlayer().getPlayer().getWorld().getTime());
		String minutes = GeneralUtils.getMinutesString(session.getPlayer().getPlayer().getWorld().getTime());
		int pos = -1;

		// finding PDA
		for (int j = 0; j < inventory.getContents().length; j++) {
			if (inventory.getContents()[j] != null &&
					inventory.getContents()[j].getTypeId() == 387 &&
					new BookItem(inventory.getContents()[j]).getTitle().equals("PDA-0589D")) {
				inventory.removeItem(inventory.getContents()[j]);

				if (pos == -1) {
					pos = j;
				}
			}
		}

		if (session.getPower() > 1) {
			// replacing tags
			page1 = finalPage1.
					replace("%H", hours).
					replace("%M", minutes);
			page2 = finalPage2.
					replace("%H", hours).
					replace("%M", minutes).
					replace("%N", "" + session.getNotifications());
			page3 = finalPage3.
					replace("%H", hours).
					replace("%M", minutes).
					replace("%W", "" + (int) session.getWater()).
					replace("%F", "" + (int) session.getFood()).
					replace("%P", "" + (int) session.getPower()).
					replace("%O", "" + (int) session.getOxygen()).
					replace("%B", "" + (int) session.getBlood()).
					replace("%b", "" + session.getBleeding());
		}

		// creating PDA
		BookItem bookItem = new BookItem(new ItemStack(387));
		bookItem.setTitle("PDA-0589D");
		bookItem.setAuthor("Nanosoft Corporation");
		if (session.getPower() > 1) {
			bookItem.setPages(new String[] {
					page1, page2, page3
			});
		} else {
			bookItem.setPages(new String[] {
					finalPage0
			});
		}

		// giving PDA
		if (pos == -1) {
			inventory.addItem(bookItem.getItemStack());
		} else {
			inventory.setItem(pos, bookItem.getItemStack());
		}
	}

	public static void updateTime() {
		if (module == null) {
			module = (MooncraftModule) SBFPlugin.getModule(2);
		}
		if (module == null) {
			return;
		}

		try {
			Bukkit.getWorld(module.getName()).setTime(15000);
		} catch (NullPointerException err) {
		}
	}

	public static void calculateChickenTerminators() {
		if (module == null) {
			module = (MooncraftModule) SBFPlugin.getModule(2);
		}
		if (module == null) {
			return;
		}

		for (World world : Bukkit.getWorlds()) {
			if (!world.getName().startsWith(module.getName())) {
				continue;
			}

			for (Entity entityRaw : world.getEntities()) {
				if (entityRaw instanceof Chicken) {
					Chicken entity = (Chicken) entityRaw;

					// if target is far, lose it
					if (entity.getTarget() != null) {
						if (!entity.getNearbyEntities(128, 128, 128).contains(entity.getTarget())) {
							entity.setTarget(null);
						}
					}

					// finding a target, else removing entity
					if (entity.getTarget() == null) {
						for (Entity entity2 : entity.getNearbyEntities(128, 128, 128)) {
							// if it is a player or a player with no creative
							if (entity2 instanceof Player && !((Player) entity2).getGameMode().equals(GameMode.CREATIVE)) {
								entity.setTarget((LivingEntity) entity2);
								return;
							}
						}
						entity.remove();
					}

					if (entity.getTarget() != null) {
						// if target is not a player or a player with no creative
						if (entity.getTarget() instanceof Player == false || !((Player) entity.getTarget()).getGameMode().equals(GameMode.CREATIVE)) {
							// going to the target
							EntityLiving targetHandle = ((CraftLivingEntity) entity.getTarget()).getHandle();
							EntityChicken entityHandle = ((CraftChicken) entity).getHandle();
							if (!entityHandle.getNavigation().a(targetHandle, 0.3F)) {
								PathEntity path = entityHandle.world.findPath(entityHandle, targetHandle, 20, true, false, false, true);
								entityHandle.setPathEntity(path);
							}

							// damaging the target
							if (entity.getNearbyEntities(1, 1, 1).contains(entity.getTarget())) {
								float damage = 5;
								for (ItemStack item : entity.getTarget().getEquipment().getArmorContents()) {
									if (item != null && item.getTypeId() != 0) {
										damage -= 0.5;
									}
								}
								entity.getTarget().damage((int) damage, entity);
							}

							// throwing an arrow
							if (entity.hasLineOfSight(entity.getTarget()) && GeneralUtils.getRandom().nextInt(16) < 1) {
								entity.launchProjectile(Arrow.class);
							}
						}
					}
				}
			}
		}
	}

	public static void checkMobAir() {
		if (module == null) {
			module = (MooncraftModule) SBFPlugin.getModule(2);
		}
		if (module == null) {
			return;
		}

		for (World world : Bukkit.getWorlds()) {
			if (!world.getName().startsWith(module.getName())) {
				continue;
			}

			for (Entity entityRaw : world.getEntities()) {
				if (entityRaw instanceof Animals || entityRaw instanceof Villager) {
					if (entityRaw instanceof Chicken) {
						continue;
					}

					LivingEntity entity = (LivingEntity) entityRaw;
					Location location = entity.getLocation();
					CoordXYZ coord = new CoordXYZ(location.getBlockX(),
							location.getBlockY(), location.getBlockZ());
					boolean isInAir = false;

					for (Region region : module.getAirRegions()) {
						if (region.isInRegion(coord)) {
							isInAir = true;
							break;
						}
					}
					if (!isInAir) {
						entity.damage(5);
					}
				}
			}
		}
	}

	public static void checkPlayerAir(MooncraftPlayerSession session) {
		if (module == null) {
			module = (MooncraftModule) SBFPlugin.getModule(2);
		}
		if (module == null) {
			return;
		}

		if (session.getPlayer().getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}

		if (!GeneralUtils.checkAuth(session.getPlayer().getPlayer())) {
			return;
		}

		if (session.getOxygen() > 1 &&
				session.getPlayer().getPlayer().getInventory().getHelmet() != null &&
				session.getPlayer().getPlayer().getInventory().getChestplate() != null) {
			return;
		}

		Location location = session.getPlayer().getPlayer().getLocation();
		CoordXYZ coord = new CoordXYZ(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		boolean isInAir = false;

		for (Region region : module.getAirRegions()) {
			if (region.isInRegion(coord)) {
				isInAir = true;
				break;
			}
		}

		if (isInAir) {
			session.setOxygen(session.getOxygen() + 0.015f);
		} else {
			session.setOxygen(session.getOxygen() - 0.025f);
		}

		if (!isInAir) {
			session.getPlayer().getPlayer().damage(5);
		}
	}

	public static void addPotion(MooncraftPlayerSession session) {
		if (module == null) {
			module = (MooncraftModule) SBFPlugin.getModule(2);
		}
		if (module == null) {
			return;
		}

		if (!GeneralUtils.checkAuth(session.getPlayer().getPlayer())) {
			return;
		}

		Player player = session.getPlayer().getPlayer();

		// anti-gravity
		if (player.getLocation().getBlockY() > 50) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 9), true); // 300 = 10 * 20 (15 sec)
		} else if (player.getLocation().getBlockY() > 40) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 7), true);
		} else if (player.getLocation().getBlockY() > 30) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 6), true);
		} else if (player.getLocation().getBlockY() > 20) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 5), true);
		} else if (player.getLocation().getBlockY() > 10) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 300, 4), true);
		}

		// effect when blood are few
		if (session.getBlood() < 15) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 300, 2), true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 4), true);
		} else if (session.getBlood() < 30) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 300, 2), true);
		} else if (session.getBlood() < 50) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 300, 2), true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300, 2), true);
		}
	}

	public static void addItemEffect(MooncraftPlayerSession session) {
		if (module == null) {
			module = (MooncraftModule) SBFPlugin.getModule(2);
		}
		if (module == null) {
			return;
		}

		Player player = session.getPlayer().getPlayer();

		ItemStack[] inventory = player.getInventory().getContents();
		for (ItemStack item : inventory) {
			if (item == null) {
				continue;
			}

			// durability effects
			switch (item.getTypeId()) {
				case 272:
				case 273:
				case 274:
				case 275:
				case 291: {
					// emerald items
					if (item.containsEnchantment(Enchantment.DURABILITY) && item.getEnchantments().get(Enchantment.DURABILITY) == 4) {
						item.removeEnchantment(Enchantment.DURABILITY);
					}
					break;
				}
				case 268:
				case 269:
				case 270:
				case 271:
				case 290: {
					// enderstone items
					if (item.containsEnchantment(Enchantment.DURABILITY) && item.getEnchantments().get(Enchantment.DURABILITY) == 2) {
						item.removeEnchantment(Enchantment.DURABILITY);
					}
					break;
				}
			}

			// damage and break speed effects
			switch (item.getTypeId()) {
				case 267:
				case 276:
				case 283:
				case 272:
				case 268: {
					// swords
					if (item.containsEnchantment(Enchantment.DAMAGE_ALL) && item.getEnchantments().get(Enchantment.DAMAGE_ALL) == 1) {
						item.removeEnchantment(Enchantment.DAMAGE_ALL);
					}
					break;
				}

				case 273:
				case 274:
				case 275:
				case 291:
				case 269:
				case 270:
				case 271:
				case 290: {
					// instruments
					if (item.containsEnchantment(Enchantment.DIG_SPEED) && item.getEnchantments().get(Enchantment.DIG_SPEED) == 2) {
						item.removeEnchantment(Enchantment.DIG_SPEED);
					}
					break;
				}
			}
		}

		ItemStack item = player.getItemInHand();
		switch (item.getTypeId()) {
			case 272:
			case 273:
			case 274:
			case 275:
			case 291: {
				// emerald items
				if (!item.containsEnchantment(Enchantment.DURABILITY) || item.getEnchantments().get(Enchantment.DURABILITY) < 4) {
					item.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
				}
				break;
			}
			case 268:
			case 269:
			case 270:
			case 271:
			case 290: {
				// enderstone items
				if (!item.containsEnchantment(Enchantment.DURABILITY) || item.getEnchantments().get(Enchantment.DURABILITY) < 2) {
					item.addEnchantment(Enchantment.DURABILITY, 2);
				}
				break;
			}
		}
		switch (item.getTypeId()) {
			case 267:
			case 276:
			case 283:
			case 272:
			case 268: {
				// swords

				// adding enchant effect
				if (!item.containsEnchantment(Enchantment.DAMAGE_ALL) || item.getEnchantments().get(Enchantment.DAMAGE_ALL) < 1) {
					item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				}

				// adding particles around player
				Location location = player.getLocation();
				location.setY(location.getY() + 1);
				switch ((int) location.getYaw() / 45) {
					case 8:
					case 7:
					case 0:
						location.setX(location.getX() - 0.1);
						location.setZ(location.getZ() + 0.5);
						break;
					case 1:
						location.setX(location.getX() - 0.5);
						location.setZ(location.getZ() + 0.5);
						break;
					case 2:
						location.setX(location.getX() - 0.5);
						location.setZ(location.getZ() - 0.1);
						break;
					case 3:
						location.setX(location.getX() - 0.5);
						location.setZ(location.getZ() - 0.5);
						break;
					case 4:
						location.setX(location.getX() + 0.1);
						location.setZ(location.getZ() - 0.5);
						break;
					case 5:
						location.setX(location.getX() + 0.5);
						location.setZ(location.getZ() - 0.5);
						break;
					case 6:
						location.setX(location.getX() + 0.5);
						location.setZ(location.getZ() + 0.1);
						break;
				}
				for (int i = 0; i < 10; i++) {
					ParticleSpawner.spawnParticle(player.getWorld(), location, Particle.PORTAL);
				}
				break;
			}

			case 273:
			case 274:
			case 275:
			case 291:
			case 269:
			case 270:
			case 271:
			case 290: {
				// instruments
				if (!item.containsEnchantment(Enchantment.DIG_SPEED) || item.getEnchantments().get(Enchantment.DIG_SPEED) < 2) {
					item.addEnchantment(Enchantment.DIG_SPEED, 2);
				}
				break;
			}
		}
	}

	public static void repairItemNames(MooncraftPlayerSession session) {
		if (module == null) {
			module = (MooncraftModule) SBFPlugin.getModule(2);
		}
		if (module == null) {
			return;
		}

		Player player = session.getPlayer().getPlayer();
		ItemStack[] inventory = player.getInventory().getContents();

		for (ItemStack item : inventory) {
			if (item == null) {
				continue;
			}
			int id = item.getTypeId();
			ItemMeta itemMeta = item.getItemMeta();
			if (!itemMeta.hasDisplayName()) {
				if (id == 369) {
					itemMeta.setDisplayName(ChatColor.RESET + "Батарейка");
				} else if (id == 19) {
					itemMeta.setDisplayName(ChatColor.RESET + "Воздухогенератор");
				} else if (id == 262) {
					itemMeta.setDisplayName(ChatColor.RESET + "Пуля");
				} else if (id == 261) {
					itemMeta.setDisplayName(ChatColor.RESET + "Пистолет");
				} else if (id == 272) {
					itemMeta.setDisplayName(ChatColor.RESET + "Изумрудный меч");
				} else if (id == 273) {
					itemMeta.setDisplayName(ChatColor.RESET + "Изумрудная лопата");
				} else if (id == 274) {
					itemMeta.setDisplayName(ChatColor.RESET + "Изумрудная кирка");
				} else if (id == 275) {
					itemMeta.setDisplayName(ChatColor.RESET + "Изумрудный топор");
				} else if (id == 268) {
					itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковый меч");
				} else if (id == 269) {
					itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковая лопата");
				} else if (id == 270) {
					itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковая кирка");
				} else if (id == 271) {
					itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковый топор");
				} else if (id == 288) {
					itemMeta.setDisplayName(ChatColor.RESET + "Железный самородок");
				} else if (id == 280) {
					itemMeta.setDisplayName(ChatColor.RESET + "Железная палка");
				} else if (id == 290) {
					itemMeta.setDisplayName(ChatColor.RESET + "Эндерняковая мотыга");
				} else if (id == 291) {
					itemMeta.setDisplayName(ChatColor.RESET + "Изумрудная мотыга");
				}
				item.setItemMeta(itemMeta);
			}
		}
	}
}
