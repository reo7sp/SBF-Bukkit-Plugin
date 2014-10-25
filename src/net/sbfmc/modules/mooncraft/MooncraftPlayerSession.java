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

import net.sbfmc.module.PlayerSession;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MooncraftPlayerSession extends PlayerSession {
	private float water = 99;
	private float food = 99;
	private float power = 99;
	private float oxygen = 99;
	private float blood = 99;
	private int bleeding;
	private int notifications;
	private long lastStartKit;

	public MooncraftPlayerSession(OfflinePlayer player) {
		super(player);

		if (!player.getPlayer().getWorld().getName().startsWith("Mooncraft")) {
			throw new IllegalArgumentException("Player isn't in mooncraft world");
		}
	}

	public MooncraftPlayerSession(Player player, float water, float food, float power, float oxygen, float blood, int bleeding, long lastStartKit) {
		super(player);

		if (!player.getWorld().getName().startsWith("Mooncraft")) {
			throw new IllegalArgumentException("Player isn't in mooncraft world");
		}

		this.lastStartKit = lastStartKit;

		setWater(water);
		setFood(food);
		setPower(power);
		setOxygen(oxygen);
		setBlood(blood);
		setBleeding(bleeding);
	}

	@Override
	public int getModuleID() {
		return 2;
	}

	public float getWater() {
		return water;
	}

	public void setWater(float water) {
		if (water < 0) {
			this.water = 0;
		} else if (water > 99) {
			this.water = 99;
		} else {
			this.water = water;
		}
	}

	public float getFood() {
		return food;
	}

	public void setFood(float food) {
		if (food < 0) {
			this.food = 0;
		} else if (food > 99) {
			this.food = 99;
		} else {
			this.food = food;
		}
	}

	public float getPower() {
		return power;
	}

	public void setPower(float power) {
		if (power < 0) {
			this.power = 0;
		} else if (power > 99) {
			this.power = 99;
		} else {
			this.power = power;
		}
	}

	public float getBlood() {
		return blood;
	}

	public void setBlood(float blood) {
		if (blood < 0) {
			this.blood = 0;
		} else if (blood > 99) {
			this.blood = 99;
		} else {
			this.blood = blood;
		}
	}

	public int getBleeding() {
		return bleeding;
	}

	public void setBleeding(int bleeding) {
		if (bleeding < 0) {
			this.bleeding = 0;
		} else if (bleeding > 4) {
			this.bleeding = 4;
		} else {
			this.bleeding = bleeding;
		}
	}

	public int getNotifications() {
		return notifications;
	}

	public void setNotifications(int notifications) {
		if (notifications < 0) {
			this.notifications = 0;
		} else if (notifications > 9) {
			this.notifications = 9;
		} else {
			this.notifications = notifications;
		}
	}

	public float getOxygen() {
		return oxygen;
	}

	public void setOxygen(float oxygen) {
		this.oxygen = oxygen;
	}

	public long getLastStartKit() {
		return lastStartKit;
	}

	public void setLastStartKit(long lastStartKit) {
		this.lastStartKit = lastStartKit;
	}
}
