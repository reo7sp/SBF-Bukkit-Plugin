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

import org.apache.commons.lang.Validate;

public class CraftParticle {
	private static String[] particles = new String[Particle.values().length];

	static {
		particles[Particle.HUGE_EXPLOSION.ordinal()] = "hugeexplosion";
		particles[Particle.LARGE_EXPLODE.ordinal()] = "largeexplode";
		particles[Particle.FIREWORKS_SPARK.ordinal()] = "fireworksSpark";
		particles[Particle.BUBBLE.ordinal()] = "bubble";
		particles[Particle.SUSPENDED.ordinal()] = "suspended";
		particles[Particle.DEPTH_SUSPEND.ordinal()] = "depthsuspend";
		particles[Particle.TOWNAURA.ordinal()] = "townaura";
		particles[Particle.CRIT.ordinal()] = "crit";
		particles[Particle.MAGIC_CRIT.ordinal()] = "magicCrit";
		particles[Particle.SMOKE.ordinal()] = "smoke";
		particles[Particle.MOB_SPELL.ordinal()] = "mobSpell";
		particles[Particle.MOB_SPELL_AMBIENT.ordinal()] = "mobSpellAmbient";
		particles[Particle.SPELL.ordinal()] = "spell";
		particles[Particle.INSTANT_SPELL.ordinal()] = "instantSpell";
		particles[Particle.WITCH_MAGIC.ordinal()] = "witchMagic";
		particles[Particle.NOTE.ordinal()] = "note";
		particles[Particle.PORTAL.ordinal()] = "portal";
		particles[Particle.ENCHANTMENT_TABLE.ordinal()] = "enchantmenttable";
		particles[Particle.EXPLODE.ordinal()] = "explode";
		particles[Particle.FLAME.ordinal()] = "flame";
		particles[Particle.LAVA.ordinal()] = "lava";
		particles[Particle.FOOTSTEP.ordinal()] = "footstep";
		particles[Particle.SPLASH.ordinal()] = "splash";
		particles[Particle.LARGE_SMOKE.ordinal()] = "largeSmoke";
		particles[Particle.CLOUD.ordinal()] = "cloud";
		particles[Particle.REDDUST.ordinal()] = "reddust";
		particles[Particle.SNOWBALL_POOF.ordinal()] = "snowballpoof";
		particles[Particle.DRIP_WATER.ordinal()] = "dripWater";
		particles[Particle.DRIP_LAVA.ordinal()] = "dripLava";
		particles[Particle.SNOW_SHOVEL.ordinal()] = "snowshovel";
		particles[Particle.SLIME.ordinal()] = "slime";
		particles[Particle.HEART.ordinal()] = "heart";
		particles[Particle.ANGRY_VILLAGER.ordinal()] = "angryVillager";
		particles[Particle.HAPPY_VILLAGER.ordinal()] = "happyVillager";
		particles[Particle.ICON_CRACK.ordinal()] = "iconcrack";
		particles[Particle.TILE_CRACK.ordinal()] = "tilecrack";
	}

	public static String getParticle(final Particle particle) {
		Validate.notNull(particle, "Particle cannot be null");
		return particles[particle.ordinal()];
	}

	private CraftParticle() {
	}
}
