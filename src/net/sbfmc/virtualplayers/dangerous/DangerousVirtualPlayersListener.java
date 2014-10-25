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

package net.sbfmc.virtualplayers.dangerous;

import net.sbfmc.virtualplayers.VirtualPlayersManager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEvent;

@SuppressWarnings("deprecation")
public class DangerousVirtualPlayersListener implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEvent(Event event) {
		try {
			if (event instanceof PlayerChatEvent || event instanceof AsyncPlayerChatEvent || event instanceof PlayerCommandPreprocessEvent) {
				return;
			}
			if (event instanceof Cancellable) {
				Player player = null;
				if (event instanceof EntityEvent && ((EntityEvent) event).getEntity() instanceof Player) {
					player = (Player) ((EntityEvent) event).getEntity();
				} else if (event instanceof PlayerEvent) {
					player = ((PlayerEvent) event).getPlayer();
				} else if (player == null) {
					return;
				}
				if (VirtualPlayersManager.contains(player)) {
					((Cancellable) event).setCancelled(true);
				}
			}
		} catch (Exception err) {
		}
	}
}
