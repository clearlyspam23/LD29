package com.clearlyspam23.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class Particle {
	
	public Vector2 location = new Vector2();
	public Vector2 bounds = new Vector2();
	public Vector2 velocity = new Vector2();
	public float duration;
	
	public void render(SpriteBatch b, float delta)
	{
		duration-=delta;
		location.x+=velocity.x*delta;
		location.y+=velocity.y*delta;
		draw(b, delta);
	}
	
	public abstract void draw(SpriteBatch b, float delta);
	
	public boolean isDead()
	{
		return duration<=0;
	}
	
	public void set(float x, float y, float bx, float by, float vx, float vy, float dur)
	{
		location.set(x, y);
		bounds.set(bx, by);
		velocity.set(vx, vy);
		duration = dur;
	}

}
