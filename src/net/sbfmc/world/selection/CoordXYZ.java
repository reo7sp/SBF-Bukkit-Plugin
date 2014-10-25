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

package net.sbfmc.world.selection;

import org.bukkit.Location;

public class CoordXYZ {
	private int x, y, z;

	public CoordXYZ(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public boolean equals(Object objectRaw) {
		if (objectRaw == null || !(objectRaw instanceof CoordXYZ)) {
			return false;
		}
		CoordXYZ object = (CoordXYZ) objectRaw;
		return x == object.getX() && y == object.getY() && z == object.getZ();
	}

	@Override
	public String toString() {
		return x + ", " + y + ", " + z;
	}

	public static CoordXYZ parse(String string) {
		String[] pieces = string.split(", ");
		return new CoordXYZ(Integer.parseInt(pieces[0]), Integer.parseInt(pieces[1]), Integer.parseInt(pieces[2]));
	}

	public static CoordXYZ parse(Location location) {
		return new CoordXYZ(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
}
