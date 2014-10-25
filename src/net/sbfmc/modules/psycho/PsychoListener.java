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

package net.sbfmc.modules.psycho;

import net.sbfmc.logging.DebugUtils;
import net.sbfmc.module.ModuleIntegration;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PsychoListener extends ModuleIntegration implements Listener {
	private PsychoModule module = (PsychoModule) getModule();

	@EventHandler
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		if (module.getPlayerSession(event.getPlayer()) == null) {
			return;
		}
		if (((PsychoPlayerSession) module.getPlayerSession(event.getPlayer())).isInPsycho()) {
			DebugUtils.debug(module.getName(), ChatColor.DARK_PURPLE + event.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + event.getMessage());
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (module.getPlayerSession(event.getPlayer()) == null) {
			return;
		}
		if (!event.getMessage().startsWith("/login") && !event.getMessage().startsWith("/register") && !event.getMessage().startsWith("/changepassword")) {
			if (((PsychoPlayerSession) module.getPlayerSession(event.getPlayer())).isInPsycho()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (module.getPlayerSession(event.getPlayer()) == null) {
			return;
		}
		if (!event.getAction().equals(Action.PHYSICAL) && ((PsychoPlayerSession) module.getPlayerSession(event.getPlayer())).isInPsycho()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (module.getPlayerSession((Player) event.getEntity()) == null) {
				return;
			}
			Player player = (Player) event.getEntity();
			if (((PsychoPlayerSession) module.getPlayerSession(player)).isInPsycho()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (event.getEntity() instanceof Player) {
			if (module.getPlayerSession((Player) event.getEntity()) == null) {
				return;
			}
			Player player = (Player) event.getEntity();
			if (((PsychoPlayerSession) module.getPlayerSession(player)).isInPsycho()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event) {
		PsychoPlayerSession session = (PsychoPlayerSession) module.getPlayerSession(event.getPlayer());
		if (session != null && !event.getPlayer().getWorld().getName().equalsIgnoreCase("psycho") && session.isInPsycho()) {
			PsychoUtils.sendToPsycho(session, "Пацент " + event.getPlayer().getName() + " как-то пропал из психушки. Возвращаем его обратно!");
		}
	}

	@Override
	public int getModuleID() {
		return 6;
	}
}
