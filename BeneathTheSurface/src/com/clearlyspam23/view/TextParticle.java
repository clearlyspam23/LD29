package com.clearlyspam23.view;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextParticle extends Particle{

	public String text;
	public BitmapFont font;

	@Override
	public void draw(SpriteBatch b, float delta) {
		
	}
	
	public void set(float x, float y, float bx, float by, float vx, float vy, float dur, String text, BitmapFont font)
	{
		set(x, y, bx, by, vx, vy, dur);
		this.text = text;
		this.font = font;
	}
}
