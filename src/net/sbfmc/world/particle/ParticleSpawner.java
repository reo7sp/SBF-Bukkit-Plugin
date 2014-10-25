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

package net.sbfmc.world.particle;

import net.minecraft.server.v1_5_R3.Packet63WorldParticles;
import net.sbfmc.utils.GeneralUtils;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ParticleSpawner {
	public static void spawnParticle(World world, Location location, Particle particle) {
		spawnParticle(world, location, particle, 0, 0, 0, 0, 0, 1, 1, 64);
	}

	public static void spawnParticle(World world, Location location, Particle particle, int id, int data) {
		spawnParticle(world, location, particle, id, data, 0, 0, 0, 1, 1, 64);
	}

	public static void spawnParticle(World world, Location location, Particle particle, float speed) {
		spawnParticle(world, location, particle, 0, 0, 0, 0, 0, speed, 1, 64);
	}

	public static void spawnParticle(World world, Location location, Particle particle, float speed, int particleCount) {
		spawnParticle(world, location, particle, 0, 0, 0, 0, 0, speed, particleCount, 64);
	}

	public static void spawnParticle(World world, Location location, Particle particle, int id, int data, float offsetX, float offsetY, float offsetZ, float speed, int particleCount, int radius) {
		Validate.notNull(location, "Location cannot be null");
		Validate.notNull(location.getWorld(), "World cannot be null");

		String particleName = CraftParticle.getParticle(particle);

		StringBuilder particleFullName = new StringBuilder();
		particleFullName.append(particleName);

		if (particle.hasID()) {
			particleFullName.append('_').append(id);
		}

		if (particle.hasData()) {
			particleFullName.append('_').append(data);
		}

		Packet63WorldParticles packet = new Packet63WorldParticles();

		// "constuctor"
		GeneralUtils.setField(packet, "a", particleFullName.toString());
		GeneralUtils.setField(packet, "b", (float) location.getX());
		GeneralUtils.setField(packet, "c", (float) location.getY());
		GeneralUtils.setField(packet, "d", (float) location.getZ());
		GeneralUtils.setField(packet, "e", offsetX);
		GeneralUtils.setField(packet, "f", offsetY);
		GeneralUtils.setField(packet, "g", offsetZ);
		GeneralUtils.setField(packet, "h", speed);
		GeneralUtils.setField(packet, "i", particleCount);

		int distance;
		radius *= radius;

		for (Player player : world.getPlayers()) {
			if (((CraftPlayer) player).getHandle().playerConnection == null) {
				continue;
			}
			if (!location.getWorld().equals(player.getWorld())) {
				continue;
			}

			distance = (int) player.getLocation().distanceSquared(location);
			if (distance <= radius) {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}
}
