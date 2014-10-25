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

package net.sbfmc.modules.prefixer;

import net.sbfmc.module.ModuleIntegration;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

@SuppressWarnings("deprecation")
public class PrefixerListener extends ModuleIntegration implements Listener {
	private PrefixerModule module = (PrefixerModule) getModule();

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(PlayerChatEvent event) {
		PrefixerPlayerSession session = (PrefixerPlayerSession) module.getPlayerSession(event.getPlayer());

		String partGroupInside = "";
		if (session != null && session.getCustomPrefix() != null && !session.getCustomPrefix().isEmpty()) {
			partGroupInside = session.getCustomPrefix();
		} else {
			int group = session == null ? 0 : session.getGroup();
			if (group == 0) {
				partGroupInside = ChatColor.WHITE + "Player";
			} else if (group == 1) {
				partGroupInside = ChatColor.GREEN + "Friend";
			} else if (group == 2) {
				partGroupInside = ChatColor.DARK_RED + "Moderator";
			} else if (group == 3) {
				partGroupInside = ChatColor.AQUA + "Admin";
			}
		}

		String partGroup = ChatColor.DARK_GREEN + "[" + partGroupInside + ChatColor.DARK_GREEN + "] ";

		// String partClient = session.hasSBFClient() ? "" : ChatColor.DARK_GREEN + "[" + ChatColor.RED + "!" + ChatColor.DARK_GREEN + "] ";

		// String partDonate = session.isDonater() ? ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "D" + ChatColor.DARK_GREEN + "]" : "";

		// String partAchievements = ChatColor.DARK_GREEN + "[" + ChatColor.GOLD + session.getNumberOfAchievements() + ChatColor.DARK_GREEN + "]";

		String partPrefixes = partGroup /* + partClient + partDonate + partAchievements + " " */;

		String partName = ChatColor.GOLD + event.getPlayer().getDisplayName() + " ";

		String partMessage = ChatColor.WHITE + event.getMessage();

		event.setFormat(partPrefixes + partName + partMessage);
	}

	@Override
	public int getModuleID() {
		return 7;
	}
}
