package com.clearlyspam23.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TileRenderer {
	
	private TextureRegion[] tileset;
	
	public TileRenderer(TextureRegion[] tileset)
	{
		this.tileset = tileset;
	}
	
	public void draw(SpriteBatch batch, float delta, float x, float y, float width, float height, TileRenderData data)
	{
		batch.draw(tileset[data.marchingNumber], x, y, width, height);
	}

}
