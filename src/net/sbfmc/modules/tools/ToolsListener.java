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

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.ModuleIntegration;
import net.sbfmc.utils.CharsetConventer;
import net.sbfmc.utils.CheckDrinkOrEat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("deprecation")
public class ToolsListener extends ModuleIntegration implements Listener {
	private ToolsModule module = (ToolsModule) getModule();

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		// repair encoding
		event.setMessage(CharsetConventer.toUTF8(event.getMessage()));
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChat(PlayerChatEvent event) {
		// repair encoding
		event.setMessage(CharsetConventer.toUTF8(event.getMessage()));

		// block bad words
		if (module.isBadWordsOff()) {
			return;
		}
		String message = " " + event.getMessage().toLowerCase() + " ";
		for (String word : module.getBadWords()) {
			if (message.contains(" " + word + " ")) {
				module.setReportMessage(event.getPlayer().getName() + " сказал " + event.getMessage() + " (" + word + ")");
				event.setMessage("I love SBF Minecraft Server!");
				event.getPlayer().sendMessage(ChatColor.RED + "Ваше сообщение заблокированно за слово \"" + word + "\"");
				return;
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		// fancy torches
		/*
		if (event.getBlock().getTypeId() == 50 && event.getBlock().getData() != 5) {
			if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE) && event.getPlayer().isSneaking()) {
				int direction = 0;
				switch (event.getBlock().getData()) {
					case 1:
						direction = 3;
						break;
					case 2:
						direction = 1;
						break;
					case 3:
						direction = 0;
						break;
					case 4:
						direction = 2;
						break;
				}
				EntityItemFrame itemFrame = new EntityItemFrame(
						((CraftWorld) event.getBlock().getWorld()).getHandle(),
						event.getBlock().getX(),
						event.getBlock().getY(),
						event.getBlock().getZ(),
						direction);
				((ItemFrame) itemFrame.getBukkitEntity()).setItem(new ItemStack(145));
				itemFrame.spawnIn(((CraftWorld) event.getBlock().getWorld()).getHandle());
				event.getPlayer().sendMessage("" + event.getBlock().getData());
			}
		}
		*/

		// tnt
		if (event.getBlock().getTypeId() == 46 && (module.isNoTntForAll() || !module.checkPermisson(event.getPlayer(), "tntPlace"))) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.RED + "Вам нельзя это делать!");
		} else {
			event.setBuild(true);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();

		if (item == null || !(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			return;
		}

		if (item.getTypeId() == 40) {
			if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 99), true);
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 4), true);
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1200, 4), true);
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1200, 4), true);
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, 4), true);
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1200, 4), true);

				if (item.getAmount() > 1) {
					item.setAmount(item.getAmount() - 1);
				} else {
					event.getPlayer().getInventory().removeItem(item);
				}
			}
		} else if (item.getTypeId() == 260) {
			event.getPlayer().setFoodLevel(event.getPlayer().getFoodLevel() - 1);
			Bukkit.getScheduler().scheduleSyncDelayedTask(
					SBFPlugin.getPlugin(),
					new CheckDrinkOrEat(module.getPlayerSession(event.getPlayer())) {
						@Override
						protected void check(ItemStack item) {
							ToolsPlayerSession csession = (ToolsPlayerSession) this.session;

							csession.setEatTomatoTime(System.currentTimeMillis());
						}
					},
					CheckDrinkOrEat.DELAY);
		} else if (item.getTypeId() == 335) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(
					SBFPlugin.getPlugin(),
					new CheckDrinkOrEat(module.getPlayerSession(event.getPlayer())) {
						@Override
						protected void check(ItemStack item) {
							final ToolsPlayerSession csession = (ToolsPlayerSession) this.session;

							if (System.currentTimeMillis() - csession.getEatTomatoTime() < 60000) {
								csession.setEatTomatoTime(0);

								// making shit tower
								for (int i = 0; i < 25; i++) {
									final int count = i;
									Bukkit.getScheduler().scheduleSyncDelayedTask(
											SBFPlugin.getPlugin(),
											new Runnable() {
												@Override
												public void run() {
													Location location = csession.getPlayer().getPlayer().getLocation();
													location.getWorld().dropItemNaturally(
															new Location(
																	location.getWorld(),
																	location.getX(),
																	location.getY() + count,
																	location.getZ()),
															new ItemStack(351, count, (short) 0, (byte) 3));
												}
											},
											i * 2);
								}

								// giving some potion effects
								csession.getPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 99), true);
								csession.getPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 4), true);
								csession.getPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, 4), true);
							}
						}
					},
					CheckDrinkOrEat.DELAY);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPortal(PlayerPortalEvent event) {
		if (event.getTo().getWorld().equals(event.getFrom().getWorld())) {
			return;
		}
		if (event.getTo().getWorld().getName().endsWith("_the_end")) {
			Location loc = event.getTo();
			for (int x = loc.getBlockX() - 10; x < loc.getBlockX() + 10; x++) {
				for (int z = loc.getBlockZ() - 10; z < loc.getBlockZ() + 10; z++) {
					loc.getWorld().getBlockAt(x, loc.getBlockY() - 2, z).setTypeId(49);
				}
			}
		} else if (event.getTo().getWorld().getName().endsWith("_nether") || event.getFrom().getWorld().getName().endsWith("_nether")) {
			event.getPortalTravelAgent().setCanCreatePortal(true);
			event.setTo(event.getPortalTravelAgent().findOrCreate(event.getTo()));
		}
	}

	@Override
	public int getModuleID() {
		return 1;
	}
}
