package com.clearlyspam23.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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
	
//	+	def calculate_normal(rect, point1, vector):
//		+	    #lets solve this using the parametric equation
//		+	    point2 = (point1[0] + vector[0], point1[1] + vector[1])
//		+	    h, k = point1
//		+	    p, q = point2
//		+	    x1 = p - h
//		+	    y1 = q - k
//		+	    normal = (1, 0)
//		+	    t = 9999999.9999 #a really large number
//		+	    if x1 != 0:
//		+	        t_temp = math.fabs((rect.right - h)/x1)
//		+	        t = t_temp
//		+	        t_temp = math.fabs((rect.left - h)/x1)
//		+	        if t_temp < t:
//		+	            t = t_temp
//		+	            normal = (-1, 0)
//		+	    if y1 != 0:
//		+	        t_temp = math.fabs((rect.bottom - k)/y1)
//		+	        if t_temp>=0 and t_temp < t:
//		+	            t = t_temp
//		+	            normal = (0, 1)
//		+	        t_temp = math.fabs((rect.top - k)/y1)
//		+	        if t_temp>=0 and t_temp < t:
//		+	            t = t_temp
//		+	            normal = (0, -1)
//		+	    return normal
	
	public static Vector2 calculateNormal(Rectangle rect1, Vector2 point, Vector2 answer)
	{
		//0 = to the left and below
		//1 = directly below
		//2 = to the right and below
		//3 = directly to the right
		//4 = in the middle of?
		//5 = directly to the left
		//6 = to the left and above
		//7 = directly above
		//8 = to the right and above
		int svalue = 0;
		if(point.x>rect1.x+rect1.width)
			svalue = 2;
		else if(point.x <= rect1.x+rect1.width && point.x >= rect1.x)
			svalue = 1;
		if(point.y>rect1.y+rect1.height)
			svalue+=3*2;
		else if(point.y<=rect1.y+rect1.height&&point.y > rect1.y)
			svalue+=3*1;
		Vector2 normal = answer;
		switch(svalue)
		{
		case 0:
			if(rect1.x-point.x < rect1.y - point.y)
				normal.set(0, -1);
			else
				normal.set(-1, 0);
			break;
		case 1:
			normal.set(0, -1);
			break;
		case 2:
			if(point.x - (rect1.x + rect1.width) < rect1.y - point.y)
				normal.set(0, -1);
			else
				normal.set(1, 0);
			break;
		case 3:
			normal.set(-1, 0);
			break;
		case 4:
			normal.set(0, 0);
			break;
		case 5:
			normal.set(1, 0);
			break;
		case 6:
			if(rect1.x-point.x < point.y - (rect1.y + rect1.height))
				normal.set(0, 1);
			else
				normal.set(-1, 0);
			break;
		case 7:
			normal.set(0, 1);
			break;
		case 8:
			if(point.x - (rect1.x + rect1.width) < point.y - (rect1.y + rect1.height))
				normal.set(0, 1);
			else
				normal.set(1, 0);
			break;
		}
		return normal;
	}
	
	//a cheap, buggy way to calculate the normal from rect1 to rect2
	public static Vector2 calculateNormal(Rectangle rect1, Vector2 point)
	{
		return calculateNormal(rect1, point, new Vector2());
	}


}
