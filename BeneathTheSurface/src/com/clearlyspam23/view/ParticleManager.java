package com.clearlyspam23.view;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ParticleManager {
	
	private ArrayList<AnimationParticle> animationParticlePool = new ArrayList<AnimationParticle>();
	private ArrayList<AnimationParticle> activeAnimParticles = new ArrayList<AnimationParticle>();
	
	public void addParticle(float x, float y, float width, float height, float velx, float vely, float duration, TextureRegion tr)
	{
		addParticle(x, y, width, height, velx, vely, duration, new Animation(5f, tr));
	}
	
	public void addParticle(float x, float y, float width, float height, float velx, float vely, float duration, Animation anim)
	{
		AnimationParticle p;
		if(animationParticlePool.size()<=0)
			p = new AnimationParticle();
		else
			p = animationParticlePool.remove(animationParticlePool.size()-1);
		p.set(x, y, width, height, velx, vely, duration, anim);
		activeAnimParticles.add(p);
	}
	
	public void render(SpriteBatch b, float delta)
	{
		for(int i = 0; i < activeAnimParticles.size(); i++)
		{
			AnimationParticle p = activeAnimParticles.get(i);
			p.render(b, delta);
			if(p.isDead())
			{
				animationParticlePool.add(p);
				activeAnimParticles.set(i, activeAnimParticles.get(activeAnimParticles.size()-1));
				activeAnimParticles.remove(activeAnimParticles.size()-1);
			}
		}
	}

}
