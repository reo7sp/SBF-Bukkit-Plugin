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

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class AirRegionFinder {
	private World world;
	private int maxHeight;
	private int locationX;
	private int locationZ;
	private Collection<Collection<Block>> layers;

	public static void findNewAirRegions(Location location) {
		AirRegionFinder airRegionFinder = new AirRegionFinder();

		airRegionFinder.layers = new HashSet<Collection<Block>>();
		airRegionFinder.world = location.getWorld();
		airRegionFinder.maxHeight = airRegionFinder.world.getMaxHeight();
		airRegionFinder.locationX = location.getBlockX();
		airRegionFinder.locationZ = location.getBlockZ();

		airRegionFinder.start();
	}

	private void start() {
		for (int y = 0; y < maxHeight; y++) {
			Collection<Block> layer = new HashSet<Block>();

			// building layer
			buildLayer(layer);

			while (!layer.isEmpty()) {
				boolean isSingleBlocksHere = false;

				// filtering layer
				for (Block block : layer) {
					if (checkConnections(block, layer)) {
						isSingleBlocksHere = true;

						layer.remove(block);
					}
				}

				if (!isSingleBlocksHere) {
					layers.add(layer);
				}
			}
		}

		// TODO continue
	}

	private void buildLayer(Collection<Block> layer) {
		for (int y = 0; y < maxHeight; y++) {
			for (int x = locationX - 64; x < locationX + 64; x++) {
				for (int z = locationZ - 64; z < locationZ + 64; z++) {
					Block block = world.getBlockAt(x, y, z);

					if (block.getTypeId() != 0) {
						layer.add(block);
					}
				}
			}
		}
	}

	private boolean checkConnections(Block block, Collection<Block> layer) {
		int connections = 0;
		if (layer.contains(world.getBlockAt(block.getX() + 1, block.getY(), block.getZ() + 0))) {
			connections++;
		}
		if (layer.contains(world.getBlockAt(block.getX() - 1, block.getY(), block.getZ() + 0))) {
			connections++;
		}
		if (layer.contains(world.getBlockAt(block.getX() + 0, block.getY(), block.getZ() + 1))) {
			connections++;
		}
		if (layer.contains(world.getBlockAt(block.getX() + 0, block.getY(), block.getZ() - 1))) {
			connections++;
		}
		if (layer.contains(world.getBlockAt(block.getX() + 1, block.getY(), block.getZ() + 1))) {
			connections++;
		}
		if (layer.contains(world.getBlockAt(block.getX() - 1, block.getY(), block.getZ() + 1))) {
			connections++;
		}
		if (layer.contains(world.getBlockAt(block.getX() + 1, block.getY(), block.getZ() - 1))) {
			connections++;
		}
		if (layer.contains(world.getBlockAt(block.getX() - 1, block.getY(), block.getZ() - 1))) {
			connections++;
		}

		return connections > 1;
	}
}
