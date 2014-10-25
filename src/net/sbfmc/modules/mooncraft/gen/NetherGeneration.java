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
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class NetherGeneration extends WorldGeneration {
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
				}
			}
		}
	}

	@Override
	public void generateAfterLoad(Chunk chunk) {
	}

	private void generateStep1(Block block, Random random) {
		if (block.getY() == 96) {
			block.setTypeId(7);
		} else if (block.getY() == 95) {
			block.setTypeId(random.nextInt(4) < 1 ? 121 : 87);
		} else if (block.getY() > 96) {
			block.setTypeId(0);
		} else if (block.getTypeId() == 87) {
			if (block.getY() > 80 && random.nextInt(4) < 1) {
				block.setTypeId(121);
			} else if (block.getY() > 64 && random.nextInt(8) < 1) {
				block.setTypeId(121);
			} else if (block.getY() > 48 && random.nextInt(16) < 1) {
				block.setTypeId(121);
			} else if (block.getY() > 32 && random.nextInt(32) < 1) {
				block.setTypeId(121);
			}
		}
	}

	@Override
	public int getModuleID() {
		return 2;
	}
}
