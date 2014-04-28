package com.clearlyspam23.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.clearlyspam23.game.Tile;
import com.clearlyspam23.game.World;

public class WorldView {
	
	private OrthographicCamera camera;
	private OrthographicCamera screenCamera;
	private TileRenderData[][] data;
	private Map<Tile, TileRenderer> tileRenderMap;
	private SpriteBatch batch;
	private List<EntityRenderer> entityRenderers = new ArrayList<EntityRenderer>();
	private HealthBarView healthBar;
	private BitmapFont font;
	private List<FontInstance> drawTexts = new ArrayList<FontInstance>();
	
	private class FontInstance
	{
		public String text;
		public Vector2 location = new Vector2();
		
		public FontInstance(String text, float x, float y)
		{
			TextBounds b = font.getBounds(text);
			location.set(x-b.width/2, y-b.height/2);
			this.text = text;
		}
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public Vector3 getMouseIn(float x, float y)
	{
		Vector3 coords = new Vector3(x, y, 0);
		camera.unproject(coords);
		return coords;
	}

	private World world;
	private Vector2 boundingRegion;
	
	public WorldView(SpriteBatch batch, World world, Map<Tile, TileRenderer> tileMap, float width, float height, float boundswidth, float boundsheight)
	{
		this.world = world;
		this.batch = batch;
		camera = new OrthographicCamera(width, height);
		camera.position.set(width/2, height/2, 0);
		camera.update();
		boundingRegion = new Vector2(boundswidth, boundsheight);
		tileRenderMap = tileMap;
		screenCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		screenCamera.position.set(screenCamera.viewportWidth/2, screenCamera.viewportHeight/2, 0);
		screenCamera.update();
	}
	
	public void draw(float delta)
	{
		cameraBound(camera, world.getPlayer().getLocation(), boundingRegion);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(int i = 0; i < world.getWidth(); i++)
		{
			for(int j = 0; j < world.getHeight(); j++)
			{
				Tile t = world.getTileAt(i, j);
				if(t!=null)
				{
					tileRenderMap.get(t).draw(batch, delta, i, j, 1, 1, data[i][j]);
				}
			}
		}
		for(int i = 0; i < entityRenderers.size(); i++)
		{
			if(!world.getEntities().contains(entityRenderers.get(i).getEntity()))
				entityRenderers.remove(i--);
				
		}
		for(EntityRenderer r : entityRenderers)
		{
			r.incrementStateTime(delta);
			r.render(batch);
		}
		batch.setProjectionMatrix(screenCamera.combined);
		for(FontInstance f : drawTexts)
			font.draw(batch, f.text, f.location.x, f.location.y);
		if(healthBar!=null)
			healthBar.render(batch, screenCamera.viewportWidth-168-20, screenCamera.viewportHeight-24-20, 168, 24, 1.0f*world.getPlayer().getHealth()/world.getPlayer().getMaxHealth());
		batch.end();
	}
	
	public void removeRenderer(EntityRenderer r)
	{
		entityRenderers.remove(r);
	}
	
	private void cameraBound(OrthographicCamera camera, Vector2 location, Vector2 boundsRegion)
	{
		camera.position.set(location.x, location.y, 0);
		if(camera.position.x - camera.viewportWidth/2 < boundsRegion.x)
			camera.position.x = camera.viewportWidth/2 + boundsRegion.x;
		if(camera.position.y - camera.viewportHeight/2 < boundsRegion.y)
			camera.position.y = camera.viewportHeight/2+boundsRegion.y;
		if(camera.position.x + camera.viewportWidth/2 > world.getWidth() - boundsRegion.x)
			camera.position.x = world.getWidth() - camera.viewportWidth/2 - boundsRegion.x;
		if(camera.position.y + camera.viewportHeight/2 > world.getHeight() - boundsRegion.y)
			camera.position.y = world.getHeight() - camera.viewportHeight/2 - boundsRegion.y;
		camera.update();
	}
	
	public void calculateRenderData()
	{
		data = new TileRenderData[world.getWidth()][world.getHeight()];
		for(int i = 0; i < world.getWidth(); i++)
		{
			for(int j = 0; j < world.getHeight(); j++)
			{
				Tile t = world.getTileAt(i, j);
				if(t!=null)
				{
					TileRenderData d = new TileRenderData();
					if(j+1>=world.getHeight()||world.getTileAt(i, j+1)==t)
						d.marchingNumber|=1;
					if(i+1>=world.getWidth()||world.getTileAt(i+1, j)==t)
						d.marchingNumber|=2;
					if(j-1<0||world.getTileAt(i, j-1)==t)
						d.marchingNumber|=4;
					if(i-1<4||world.getTileAt(i-1, j)==t)
						d.marchingNumber|=8;
					data[i][j] = d;
				}
			}
		}
	}

	public List<EntityRenderer> getEntityRenderer() {
		return entityRenderers;
	}

	public void addEntityRenderer(EntityRenderer entityRenderer) {
		entityRenderers.add(entityRenderer);
	}

	public HealthBarView getHealthBar() {
		return healthBar;
	}

	public void setHealthBar(HealthBarView healthBar) {
		this.healthBar = healthBar;
	}
	
	public void resize(float x, float y)
	{
		screenCamera.viewportWidth = x;
		screenCamera.viewportHeight = y;
		screenCamera.position.set(x/2, y/2, 0);
		screenCamera.update();
	}

	public OrthographicCamera getScreenCamera() {
		return screenCamera;
	}

	public void setScreenCamera(OrthographicCamera screenCamera) {
		this.screenCamera = screenCamera;
	}
	
	public void addText(String text, float x, float y)
	{
		drawTexts.add(new FontInstance(text, x, y));
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}

}
