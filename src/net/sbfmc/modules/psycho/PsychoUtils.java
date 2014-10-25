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

import java.io.File;
import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.logging.DebugUtils;
import net.sbfmc.modules.inv.InventoryModule;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PsychoUtils {
	private static PsychoModule module;

	public static void sendToPsycho(PsychoPlayerSession session) {
		sendToPsycho(session, "Пациент " + session.getPlayer().getName() + " отправлен в психушку на месяц! Пожелаем ему удачи!", 30);
	}

	public static void sendToPsycho(PsychoPlayerSession session, int days) {
		sendToPsycho(session, "Пациент " + session.getPlayer().getName() + " отправлен в психушку на месяц! Пожелаем ему удачи!", 30);
	}

	public static void sendToPsycho(PsychoPlayerSession session, String message) {
		sendToPsycho(session, message, 30);
	}

	public static void sendToPsycho(PsychoPlayerSession session, String message, int days) {
		if (module == null) {
			module = (PsychoModule) SBFPlugin.getModule(6);
		}

		session.setInPsycho(true);
		session.setInPsychoTime(System.currentTimeMillis());
		session.setPsychoTime(1000 * 60 * 24 * days);

		if (session.getPlayer().getPlayer() != null) {
			session.getPlayer().getPlayer().getInventory().clear();
			session.getPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 4)); // 600 = 20 * 30 (30 sec)
			GeneralUtils.teleportToWorld(session.getPlayer().getPlayer(), "psycho");
		}

		InventoryModule inventoryModule = (InventoryModule) SBFPlugin.getModule(6);
		new File(inventoryModule.getClassicInvConf().getConfFolder(), session.getPlayer().getName() + ".yml").delete();
		new File(inventoryModule.getMooncraftInvConf().getConfFolder(), session.getPlayer().getName() + ".yml").delete();

		try {
			module.getPsychoConf().savePlayer(session);
		} catch (IOException err) {
			DebugUtils.debugError("PSYCHO", "Can't save data of " + session.getPlayer().getName(), err);
		}

		for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
			loopPlayer.sendMessage(ChatColor.GREEN + message);
		}
	}

	public static void returnFromPsycho(PsychoPlayerSession session) {
		returnFromPsycho(session, "Пациент " + session.getPlayer().getName() + " всё осознал и вернулся в нормальный мир!");
	}

	public static void returnFromPsycho(PsychoPlayerSession session, String message) {
		session.setInPsycho(false);
		session.setInPsychoTime(0);
		session.setPsychoTime(0);

		if (session.getPlayer().getPlayer() != null) {
			session.getPlayer().getPlayer().getInventory().clear();
			GeneralUtils.teleportToWorld(session.getPlayer().getPlayer(), "start");
		}
		try {
			module.getPsychoConf().savePlayer(session);
		} catch (IOException err) {
			DebugUtils.debugError("PSYCHO", "Can't save data of " + session.getPlayer().getName(), err);
		}

		for (Player loopPlayer : Bukkit.getOnlinePlayers()) {
			loopPlayer.sendMessage(ChatColor.GREEN + message);
		}
	}
}
