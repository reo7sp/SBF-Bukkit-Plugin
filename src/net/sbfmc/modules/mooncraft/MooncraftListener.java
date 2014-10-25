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

import java.util.Iterator;
import java.util.Set;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.ModuleIntegration;
import net.sbfmc.utils.CheckDrinkOrEat;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.world.GenerationQueue;
import net.sbfmc.world.selection.CoordXYZ;
import net.sbfmc.world.selection.Region;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MooncraftListener extends ModuleIntegration implements Listener {
	private MooncraftModule module = (MooncraftModule) getModule();

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (!event.getPlayer().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
			return;
		}

		MooncraftPlayerSession session = (MooncraftPlayerSession) module.getPlayerSession(event.getPlayer());
		if (session == null) {
			return;
		}

		if (event.getPlayer().isSprinting()) {
			if (session.getPower() < 1) {
				event.getPlayer().setSprinting(false);
			} else {
				session.setPower(session.getPower() - 0.025f);
				if (GeneralUtils.getRandom().nextInt(100) < 1) {
					session.setWater(session.getWater() - 0.01f);
				}
				if (GeneralUtils.getRandom().nextInt(100) < 1) {
					session.setWater(session.getFood() - 0.005f);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if (!event.getPlayer().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		MooncraftPlayerSession session = (MooncraftPlayerSession) module.getPlayerSession(event.getPlayer());
		if (session == null) {
			return;
		}

		session.setBleeding(0);
		session.setBlood(99);
		session.setWater(99);
		session.setFood(99);
		session.setPower(99);
		session.setOxygen(99);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.getPlayer().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		ItemStack item = event.getPlayer().getItemInHand();

		if (item == null || !(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}

		MooncraftPlayerSession session = (MooncraftPlayerSession) module.getPlayerSession(event.getPlayer());
		if (session == null) {
			return;
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(
				SBFPlugin.getPlugin(),
				new CheckDrinkOrEat(session) {
					@Override
					protected void check(ItemStack item) {
						MooncraftPlayerSession session = (MooncraftPlayerSession) this.session;

						if (item.getTypeId() == 373 && item.getDurability() == 0) {
							session.setWater(session.getWater() + 20);
						} else if (item.getTypeId() == 260) {
							session.setFood(session.getFood() + 10);
						} else if (item.getTypeId() == 393) {
							session.setFood(session.getFood() + 15);
						} else if (item.getTypeId() == 297) {
							session.setFood(session.getFood() + 15);
						} else if (item.getTypeId() == 92) {
							session.setFood(session.getFood() + 10);
							session.setPower(session.getPower() + 10);
						} else if (item.getTypeId() == 391) {
							session.setFood(session.getFood() + 15);
						} else if (item.getTypeId() == 366) {
							session.setFood(session.getFood() + 15);
						} else if (item.getTypeId() == 350) {
							session.setFood(session.getFood() + 20);
						} else if (item.getTypeId() == 320) {
							session.setFood(session.getFood() + 20);
						} else if (item.getTypeId() == 357) {
							session.setFood(session.getFood() + 5);
							session.setPower(session.getPower() + 5);
						} else if (item.getTypeId() == 322 && item.getData().getData() == 0) {
							session.setFood(session.getFood() + 20);
						} else if (item.getTypeId() == 322 && item.getData().getData() == 1) {
							session.setFood(session.getFood() + 100);
						} else if (item.getTypeId() == 396) {
							session.setFood(session.getFood() + 15);
						} else if (item.getTypeId() == 360) {
							session.setFood(session.getFood() + 5);
						} else if (item.getTypeId() == 282) {
							session.setFood(session.getFood() + 15);
						} else if (item.getTypeId() == 394) {
							session.setFood(session.getFood() + 5);
						} else if (item.getTypeId() == 392) {
							session.setFood(session.getFood() + 5);
						} else if (item.getTypeId() == 400) {
							session.setFood(session.getFood() + 20);
						} else if (item.getTypeId() == 363) {
							session.setFood(session.getFood() + 10);
						} else if (item.getTypeId() == 365) {
							session.setFood(session.getFood() + 5);
						} else if (item.getTypeId() == 349) {
							session.setFood(session.getFood() + 5);
						} else if (item.getTypeId() == 319) {
							session.setFood(session.getFood() + 10);
						} else if (item.getTypeId() == 367) {
							session.setFood(session.getFood() + 5);
						} else if (item.getTypeId() == 375) {
							session.setFood(session.getFood() + 5);
						} else if (item.getTypeId() == 364) {
							session.setFood(session.getFood() + 20);
						}
					}
				},
				CheckDrinkOrEat.DELAY);

		if (item.getTypeId() == 351 && item.getData().getData() == 1) {
			session.setBlood(session.getBlood() + 20);

			if (item.getAmount() > 1) {
				item.setAmount(item.getAmount() - 1);
			} else {
				event.getPlayer().getInventory().removeItem(item);
			}
		} else if (item.getTypeId() == 339) {
			session.setBleeding(session.getBleeding() - 1);

			if (item.getAmount() > 1) {
				item.setAmount(item.getAmount() - 1);
			} else {
				event.getPlayer().getInventory().removeItem(item);
			}
		} else if (item.getTypeId() == 353) {
			session.setFood(session.getFood() + 5);
			session.setPower(session.getPower() + 10);

			if (item.getAmount() > 1) {
				item.setAmount(item.getAmount() - 1);
			} else {
				event.getPlayer().getInventory().removeItem(item);
			}
		} else if (item.getTypeId() == 369) {
			session.setPower(session.getPower() + 50);

			if (item.getAmount() > 1) {
				item.setAmount(item.getAmount() - 1);
			} else {
				event.getPlayer().getInventory().removeItem(item);
			}
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (!event.getEntity().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();

			if (player.getFoodLevel() != 18) {
				player.setFoodLevel(18);
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!event.getEntity().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		if (event.getEntity() instanceof Player) {
			MooncraftPlayerSession session = (MooncraftPlayerSession) module.getPlayerSession((Player) event.getEntity());
			if (session == null) {
				return;
			}

			if (GeneralUtils.getRandom().nextInt(100) < 1) {
				session.setBleeding(session.getBleeding() + 1);
			}
		} else if (event.getEntity() instanceof Chicken) {
			Chicken chicken = (Chicken) event.getEntity();
			if (chicken.getTarget() == null && event.getDamager() instanceof LivingEntity) {
				chicken.setTarget((LivingEntity) event.getDamager());
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChunkLoad(ChunkLoadEvent event) {
		if (event.getWorld().getName().equals(module.getName())) {
			GenerationQueue.add(event.getChunk(), module.getNormalGeneration(), !event.isNewChunk());
		} else if (event.getWorld().getName().equals(module.getName() + "_nether")) {
			GenerationQueue.add(event.getChunk(), module.getNetherGeneration(), !event.isNewChunk());
		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!event.getEntity().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		if (event.getEntity() instanceof Animals || event.getEntity() instanceof Villager) {
			if (event.getEntity() instanceof Chicken) {
				event.getEntity().setMaxHealth(30);
				event.getEntity().setHealth(30);
				return;
			}
			CoordXYZ coord = new CoordXYZ(
					event.getLocation().getBlockX(),
					event.getLocation().getBlockX(),
					event.getLocation().getBlockX());
			boolean isInAir = false;

			for (Region region : module.getAirRegions()) {
				if (!region.isInRegion(coord)) {
					isInAir = true;
				}
			}
			if (!isInAir) {
				event.setCancelled(true);
				return;
			}
		} else if (event.getEntity() instanceof Monster) {
			if (event.getSpawnReason().equals(SpawnReason.NATURAL) && !event.isCancelled()) {
				boolean isInAirRegion = false;
				CoordXYZ coord = CoordXYZ.parse(event.getLocation());
				for (Region region : module.getAirRegions()) {
					if (region.isInRegion(coord)) {
						isInAirRegion = true;
					}
				}
				if (!isInAirRegion && GeneralUtils.getRandom().nextInt(25) < 1) {
					event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.CHICKEN);
				}
			}
		}

		event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 28800, 9), true); // 28800 = 24 * 60 * 20 (1 day)
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.getBlock().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		if (event.getBlock().getTypeId() == 19) {
			module.getAirRegionNotifs().put(
					module.getAirRegionNotifs().size(),
					new CoordXYZ(event.getBlock().getX(), event.getBlock().getY(), event.getBlock().getZ()));
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (!event.getBlock().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		ItemStack item = event.getPlayer().getItemInHand();

		if (event.getBlock().getTypeId() == 19) {
			Set<Integer> ids = module.getAirRegionNotifs().keySet();
			for (int id : ids) {
				CoordXYZ coord = module.getAirRegionNotifs().get(id);
				if ((coord.getX() == event.getBlock().getX()) &&
						(coord.getY() == event.getBlock().getY()) &&
						(coord.getZ() == event.getBlock().getZ())) {
					module.getAirRegionNotifs().remove(id);
				}
			}
		} else if (event.getBlock().getTypeId() == 74 || event.getBlock().getTypeId() == 73 || event.getBlock().getTypeId() == 56 || event.getBlock().getTypeId() == 129 || event.getBlock().getTypeId() == 14) {
			if (item.getTypeId() == 274) {
				int dropItemID = 0;
				int amount = 1;

				// drop item id
				if (item.containsEnchantment(Enchantment.SILK_TOUCH)) {
					dropItemID = event.getBlock().getTypeId();
				} else {
					switch (event.getBlock().getTypeId()) {
						case 74:
						case 73:
							dropItemID = 331;
							amount += GeneralUtils.getRandom().nextInt(2) + 2;
							break;
						case 56:
							dropItemID = 264;
							break;
						case 129:
							dropItemID = 388;
							break;
						case 14:
							dropItemID = 266;
							break;
					}
				}

				// amount
				if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					amount += GeneralUtils.getRandom().nextInt(item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) + 1);
				}

				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(dropItemID, amount));
			}
		} else if (event.getBlock().getTypeId() == 15) {
			if (item.getTypeId() == 270) {
				int dropItemID = 0;
				int amount = 1;

				// drop item id
				if (item.containsEnchantment(Enchantment.SILK_TOUCH)) {
					dropItemID = event.getBlock().getTypeId();
				} else {
					switch (event.getBlock().getTypeId()) {
						case 15:
							dropItemID = 15;
							break;
					}
				}

				// amount
				if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
					amount += GeneralUtils.getRandom().nextInt(item.getEnchantments().get(Enchantment.LOOT_BONUS_BLOCKS) + 1);
				}

				event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(dropItemID, amount));
			}
		}
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		if (!event.getBlock().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		if (event.getItemInHand().getTypeId() == 274) {
			if (event.getBlock().getTypeId() == 74 || event.getBlock().getTypeId() == 73 || event.getBlock().getTypeId() == 56 || event.getBlock().getTypeId() == 129 || event.getBlock().getTypeId() == 14) {
				event.setInstaBreak(true);
			}
		} else if (event.getItemInHand().getTypeId() == 270) {
			if (event.getBlock().getTypeId() == 15) {
				event.setInstaBreak(true);
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (!event.getEntity().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		if (event.getCause().equals(DamageCause.FALL)) {
			event.setCancelled(true);
		}
		if (event.getCause().equals(DamageCause.DROWNING)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (!event.getWorld().getName().startsWith(module.getName())) {
			return;
		}

		event.setCancelled(event.toWeatherState());
	}

	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		Player player = null;

		// check if it is repair recipe
		if (event.getRecipe() instanceof ShapelessRecipe) {
			ShapelessRecipe recipe = (ShapelessRecipe) event.getRecipe();
			boolean hasDifference = false;
			for (ItemStack item : recipe.getIngredientList()) {
				if (item.getTypeId() == 0) {
					continue;
				}
				if (item.getTypeId() != recipe.getResult().getTypeId()) {
					hasDifference = true;
					break;
				}
			}
			if (!recipe.getIngredientList().isEmpty() && !hasDifference) {
				return;
			}
		}

		if (event.getInventory().getHolder() instanceof Player) {
			player = (Player) event.getInventory().getHolder();
		} else {
			return;
		}

		boolean cancelCraft = false;
		for (Recipe recipe : module.getRecipes()) {
			// hack
			if (event.getRecipe().getResult().getTypeId() == 265) {
				break;
			}

			if (recipe.getResult().getTypeId() == event.getRecipe().getResult().getTypeId()) {
				if (player.getWorld().getName().startsWith(module.getName())) {
					cancelCraft = !GeneralUtils.recipesEquals(recipe, event.getRecipe());
				} else {
					cancelCraft = GeneralUtils.recipesEquals(recipe, event.getRecipe());
				}
			}
		}
		if (cancelCraft) {
			player.sendMessage(ChatColor.RED + "Неправильный крафт!");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
		String fromWorldName = event.getFrom().getName();
		String toWorldName = event.getPlayer().getWorld().getName();

		if (toWorldName.startsWith(module.getName())) {
			if (!fromWorldName.startsWith(module.getName())) {
				Bukkit.getScheduler().scheduleSyncDelayedTask(
						SBFPlugin.getPlugin(),
						new Runnable() {
							@Override
							public void run() {
								try {
									module.registerPlayer(event.getPlayer());
								} catch (Exception err) {
									DebugUtils.debugError("MOON", "Module \"" + module.getName() + "\" has an error on player register!", err);
								}
							}
						},
						60); // 60 = 3 * 20 (3 sec)
			}
		} else {
			module.filterPlayers();
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		if (!event.getEntity().getWorld().getName().startsWith(module.getName())) {
			return;
		}

		if (event.getEntity() instanceof Chicken) {
			// removing all chicken meat from drops
			Iterator<ItemStack> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				ItemStack item = iterator.next();
				if (item.getTypeId() == 365) {
					iterator.remove();
				}
			}

			// and adding a little bit more iron nuggets
			event.getDrops().add(new ItemStack(288, 9 + GeneralUtils.getRandom().nextInt(9)));
		}
	}

	@Override
	public int getModuleID() {
		return 2;
	}
}
