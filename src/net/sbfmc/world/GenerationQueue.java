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

package net.sbfmc.world;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.gen.WorldGeneration;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class GenerationQueue {
	private static Collection<GenerationQueueElement> chunks = new Vector<GenerationQueueElement>();
	private static int taskID = -1;

	private static int CPS, rawCPS;
	private static long lastTimeCPS;
	private static int GCPS, rawGCPS;
	private static long lastTimeGCPS;
	private static int LCPS, rawLCPS;
	private static long lastTimeLCPS;

	public static void start() {
		stop();

		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(
				SBFPlugin.getPlugin(),
				new Runnable() {
					@Override
					public void run() {
						long startLoopTime = System.currentTimeMillis();

						while (true) {
							if (System.currentTimeMillis() - startLoopTime > 100) {
								break;
							}

							if (System.currentTimeMillis() - lastTimeCPS > 1000) {
								CPS = rawCPS;
								rawCPS = 0;
								lastTimeCPS = System.currentTimeMillis();
							}
							if (System.currentTimeMillis() - lastTimeGCPS > 1000) {
								GCPS = rawGCPS;
								rawGCPS = 0;
								lastTimeGCPS = System.currentTimeMillis();
							}
							if (System.currentTimeMillis() - lastTimeLCPS > 1000) {
								LCPS = rawLCPS;
								rawLCPS = 0;
								lastTimeLCPS = System.currentTimeMillis();
							}

							boolean genPerformed = false;

							for (GenerationQueueElement currentElement : chunks) {
								if (System.currentTimeMillis() - currentElement.getTime() > 3000) {
									currentElement.generate();
									genPerformed = true;
									rawCPS++;
									if (currentElement.loadGeneration) {
										rawLCPS++;
									} else {
										rawGCPS++;
									}
									chunks.remove(currentElement);
									break;
								}
							}

							if (!genPerformed) {
								break;
							}
						}
					}
				},
				10,
				10);
	}

	public static void stop() {
		CPS = 0;
		rawCPS = 0;
		lastTimeCPS = 0;
		chunks.clear();
		if (taskID != -1) {
			Bukkit.getScheduler().cancelTask(taskID);
			taskID = -1;
		}
	}

	public static void add(Chunk chunk, WorldGeneration generator, boolean loadGeneration) {
		if (chunk == null || generator == null) {
			return;
		}

		for (GenerationQueueElement element : chunks) {
			if (element.getChunk().equals(chunk)) {
				if (element.isLoadGeneration() == loadGeneration) {
					return;
				}
			}
		}

		chunks.add(new GenerationQueueElement(chunk, generator, loadGeneration));
	}

	@Deprecated
	public static Collection<GenerationQueueElement> getChunks() {
		return Collections.unmodifiableCollection(chunks);
	}

	public static int getCPS() {
		return CPS;
	}

	public static int getGCPS() {
		return GCPS;
	}

	public static int getLCPS() {
		return LCPS;
	}

	public static class GenerationQueueElement {
		private Chunk chunk;
		private boolean loadGeneration;
		private WorldGeneration generator;
		private long time;

		public GenerationQueueElement(Chunk chunk, WorldGeneration generator, boolean loadGeneration) {
			this.chunk = chunk;
			this.generator = generator;
			this.loadGeneration = loadGeneration;
			this.time = System.currentTimeMillis();
		}

		public void generate() {
			if (chunk.load(false)) {
				if (loadGeneration) {
					generator.generateAfterLoad(chunk);
				} else {
					generator.generateAfterGeneration(chunk);
				}
			}
		}

		public long getTime() {
			return time;
		}

		public Chunk getChunk() {
			return chunk;
		}

		public boolean isLoadGeneration() {
			return loadGeneration;
		}

		public WorldGeneration getGenerator() {
			return generator;
		}
	}
}
