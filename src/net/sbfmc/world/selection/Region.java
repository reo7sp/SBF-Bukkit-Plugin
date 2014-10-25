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

public class Region {
	private CoordXYZ minPoint, maxPoint;

	public Region() {
	}

	public Region(CoordXYZ minPoint, CoordXYZ maxPoint) {
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
		repairPoints();
	}

	public boolean repairPoints() {
		if (minPoint == null || maxPoint == null) {
			return false;
		}

		if (minPoint.getX() > maxPoint.getX()) {
			int a = maxPoint.getX();
			maxPoint.setX(minPoint.getX());
			minPoint.setX(a);
		}
		if (minPoint.getY() > maxPoint.getY()) {
			int a = maxPoint.getY();
			maxPoint.setY(minPoint.getY());
			minPoint.setY(a);
		}
		if (minPoint.getZ() > maxPoint.getZ()) {
			int a = maxPoint.getZ();
			maxPoint.setZ(minPoint.getZ());
			minPoint.setZ(a);
		}
		return false;
	}

	public boolean isInRegion(CoordXYZ point) {
		if (minPoint == null || maxPoint == null) {
			return false;
		}

		return (point.getX() >= minPoint.getX() && point.getY() >= minPoint.getY() && point.getZ() >= minPoint.getZ()) &&
				(point.getX() <= maxPoint.getX() && point.getY() <= maxPoint.getY() && point.getZ() <= maxPoint.getZ());
	}

	public CoordXYZ getMinPoint() {
		return minPoint;
	}

	public void setMinPoint(CoordXYZ point) {
		minPoint = point;
		repairPoints();
	}

	public CoordXYZ getMaxPoint() {
		return maxPoint;
	}

	public void setMaxPoint(CoordXYZ point) {
		maxPoint = point;
		repairPoints();
	}

	@Override
	public String toString() {
		return "(" + minPoint + "), (" + maxPoint + ")";
	}

	@Override
	public boolean equals(Object objectRaw) {
		if (objectRaw != null && objectRaw instanceof Region && minPoint != null && maxPoint != null) {
			Region object = (Region) objectRaw;
			return minPoint.equals(object.getMinPoint()) && maxPoint.equals(object.getMaxPoint());
		}
		return false;
	}
}
