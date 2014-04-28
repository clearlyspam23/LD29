package com.clearlyspam23.view;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AnimationParticle extends Particle{
	
	public Animation anim;
	private float stateTime;

	@Override
	public void draw(SpriteBatch b, float delta) {
		stateTime+=delta;
		b.draw(anim.getKeyFrame(stateTime), location.x, location.y, bounds.x, bounds.y);
	}
	
	public void set(float x, float y, float bx, float by, float vx, float vy, float dur, Animation anim)
	{
		set(x, y, bx, by, vx, vy, dur);
		this.anim = anim;
		stateTime = 0;
	}

}
