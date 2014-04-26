package com.clearlyspam23.util;

import com.badlogic.gdx.math.Rectangle;

public class UtilMath {
	
	public static Rectangle intersection(Rectangle r1, Rectangle r2)
	{
		if(!r1.overlaps(r2))
			return null;
		Rectangle r = new Rectangle();
		r.x = Math.max(r1.x, r2.x);
		r.y = Math.max(r1.y, r2.y);
		r.width = r1.width + r2.width - (Math.max(r1.x+r1.width, r2.x + r2.width) - Math.min(r1.x, r2.x));
		r.height = r1.height + r2.height - (Math.max(r1.y+r1.height, r2.y + r2.height) - Math.min(r1.y, r2.y));
		return r;
	}

}
