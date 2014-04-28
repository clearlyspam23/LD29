package com.clearlyspam23.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class HealthBarView {
	
	private TextureRegion overlay;
	private TextureRegion health;
	private TextureRegion lowHealth;
	
	public HealthBarView(TextureRegion overlay, TextureRegion health, TextureRegion lowHealth)
	{
		this.overlay = overlay;
		this.health = health;
		this.lowHealth = lowHealth;
	}
	
	public void render(SpriteBatch batch, float x, float y, float width, float height, float percentage)
	{
		percentage = Math.max(0, percentage);
		TextureRegion t = health;
		if(percentage<.2f)
			t = lowHealth;
		batch.draw(t, x, y, width*percentage, height);
		batch.draw(overlay, x, y, width, height);
	}

}
