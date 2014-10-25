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

package net.sbfmc.modules.mooncraft.gen;

import java.util.Random;

import net.sbfmc.module.gen.WorldGeneration;
import net.sbfmc.modules.mooncraft.MooncraftModule;
import net.sbfmc.utils.GeneralUtils;
import net.sbfmc.world.selection.CoordXYZ;
import net.sbfmc.world.selection.Region;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

public class NormalGeneration extends WorldGeneration {
	private MooncraftModule module = (MooncraftModule) getModule();
	private static boolean genChange;

	@Override
	public void initGeneration() {
	}

	@Override
	public void deinitGeneration() {
	}

	@Override
	public void generateAfterGeneration(Chunk chunk) {
		int maxHeight = chunk.getWorld().getMaxHeight();

		for (int y = 0; y < maxHeight; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Block block = chunk.getBlock(x, y, z);
					generateStep1(block, GeneralUtils.getRandom());
					generateStep2(block);
					generateStep3(block);
				}
			}
		}
	}

	@Override
	public void generateAfterLoad(Chunk chunk) {
		int maxHeight = chunk.getWorld().getMaxHeight();

		for (int y = 0; y < maxHeight; y++) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					Block block = chunk.getBlock(x, y, z);
					if (genChange) {
						generateStep2(block);
					}
					generateStep3(block);
				}
			}
		}
	}

	private void generateStep1(Block block, Random random) {
		int id = block.getTypeId();

		// setting right biome
		if (!block.getBiome().equals(Biome.EXTREME_HILLS)) {
			block.setBiome(Biome.EXTREME_HILLS);
		}

		// replacing blocks
		if (id == 1 && (block.getY() > 50 || random.nextInt(4) < 1)) {
			block.setTypeId(121);
		} else if ((id == 15 || id == 16 || id == 56) && random.nextInt(4) < 1) {
			block.setTypeId(121);
		} else if (id == 17) {
			block.setTypeId(0);
		}
	}

	private void generateStep2(Block block) {
		int id = block.getTypeId();

		if (id == 8 || id == 9 || id == 79) {
			block.setTypeId(0, false);
		} else if (id == 12 || id == 82) {
			block.setTypeId(121);
		}
	}

	private void generateStep3(Block block) {
		int id = block.getTypeId();

		if (id == 2 || id == 3 || id == 17 || id == 18 || id == 104 || id == 105 || id == 106 || id == 5 ||
				id == 83 || id == 31 || id == 37 || id == 38 || id == 59 || id == 141 || id == 142 || id == 78 || id == 111) {
			// check if coord in air region
			CoordXYZ coord = new CoordXYZ(block.getX(), block.getY(), block.getZ());
			boolean inAirRegion = false;

			for (Region region : module.getAirRegions()) {
				if (region.isInRegion(coord)) {
					inAirRegion = true;
					break;
				}
			}

			// replacing blocks
			if (inAirRegion) {
				return;
			}
			if (id == 5 && block.getData() != 1) {
				block.setData((byte) 1);
			} else if (id == 2 || id == 3 || id == 17) {
				block.setTypeId(121);
			} else if (id == 104 || id == 105 || id == 106 || id == 83 || id == 31 || id == 38 ||
					id == 59 || id == 141 || id == 142 || id == 78 || id == 18 || id == 111) {
				block.setTypeId(0);
			} else if (id == 8 || id == 9) {
				block.setTypeId(0, false);
			}
		}
	}

	public static boolean isGenChange() {
		return genChange;
	}

	public static void setGenChange(boolean genChange) {
		NormalGeneration.genChange = genChange;
	}

	@Override
	public int getModuleID() {
		return 2;
	}
}
