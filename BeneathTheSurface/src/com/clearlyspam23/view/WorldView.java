package com.clearlyspam23.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.clearlyspam23.game.Tile;
import com.clearlyspam23.game.World;

public class WorldView {
	
	private OrthographicCamera camera;
	private TileRenderData[][] data;
	private Map<Tile, TileRenderer> tileRenderMap;
	private SpriteBatch batch;
	private List<EntityRenderer> entityRenderers = new ArrayList<EntityRenderer>();
	
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
		batch.end();
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

}
