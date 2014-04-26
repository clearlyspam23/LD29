package com.clearlyspam23.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class World {
	
	private Tile[][] tiles;
	private List<Entity> entities;
	private float gravity;
	private List<Rectangle> tileRectangles;
	
	public World(int width, int height)
	{
		setTiles(new Tile[width][height]);
		entities = new ArrayList<Entity>();
	}
	
	public void update(float delta)
	{
		for(Entity e : entities)
		{
			e.move(0, -gravity);
			e.move(delta);
		}
		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			for(Rectangle r : tileRectangles)
				if(e.getRectangle().overlaps(r))
					e.collidesWithTile(r, null);
			for(int j = i; j < entities.size(); j++)
			{
				Entity other = entities.get(j);
				if(e.getRectangle().overlaps(other.getRectangle()))
				{
					e.collidesWithEntity(other);
					other.collidesWithEntity(e);
				}
			}
		}
	}
	
	public int getWidth()
	{
		return tiles.length;
	}
	
	public int getHeight()
	{
		return tiles[0].length;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}
	
	public void setTile(int locx, int locy, Tile t){
		tiles[locx][locy] = t;
	}
	
	public Tile getTileAt(int locx, int locy)
	{
		return tiles[locx][locy];
	}
	
	public boolean isValidLocation(int x, int y)
	{
		return x >= 0 && x < tiles.length && y >= 0 && y < tiles[x].length;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void addEntity(Entity e)
	{
		entities.add(e);
		e.setWorld(this);
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}
	
	public List<Tile> getTilesIn(Entity entity)
	{
		List<Tile> tiles = new ArrayList<Tile>();
		//get the 4 points that make up this entity
		Vector2 loc = entity.getLocation();
		Vector2 bounds = entity.getBounds();
		if(isValidLocation((int)(loc.x + bounds.x/2), (int)(loc.y + bounds.y/2)))
			tiles.add(getTileAt((int)(loc.x + bounds.x/2), (int)(loc.y + bounds.y/2)));
		if(isValidLocation((int)(loc.x - bounds.x/2), (int)(loc.y + bounds.y/2)))
			tiles.add(getTileAt((int)(loc.x - bounds.x/2), (int)(loc.y + bounds.y/2)));
		if(isValidLocation((int)(loc.x + bounds.x/2), (int)(loc.y - bounds.y/2)))
			tiles.add(getTileAt((int)(loc.x + bounds.x/2), (int)(loc.y - bounds.y/2)));
		if(isValidLocation((int)(loc.x - bounds.x/2), (int)(loc.y - bounds.y/2)))
			tiles.add(getTileAt((int)(loc.x - bounds.x/2), (int)(loc.y - bounds.y/2)));
		return tiles;
	}
	
	public List<Rectangle> getTileRects()
	{
		return tileRectangles;
	}
	
	public void buildTileRectangles()
	{
		//this function got messy fast
		tileRectangles = new ArrayList<Rectangle>();
		Tile[][] tilesCopy = tiles.clone();
		for(int i = 0; i < tilesCopy.length; i++)
		{
			for(int j = 0; j < tilesCopy[i].length; j++)
			{
				if(blocksMovement(tilesCopy[i][j]))
				{
					int width1 = 1;
					int height1 = Integer.MAX_VALUE;
					for(int i2 = i+1; i2 < tilesCopy.length && blocksMovement(tilesCopy[i2][j]); i2++)
					{
						width1++;
						int tempHeight = 1;
						for(int j2 = j + 1; j2 < tilesCopy[i2].length && blocksMovement(tilesCopy[i2][j2]);j2++)
						{
							tempHeight++;
						}
						if(tempHeight < height1)
							height1 = tempHeight;
					}
					if(height1 == Integer.MAX_VALUE)
						height1 = 1;
					int width2 = Integer.MAX_VALUE;
					int height2 = 1;
					for(int j2 = j+1; j2 < tilesCopy[i].length && blocksMovement(tilesCopy[i][j2]); j2++)
					{
						height2++;
						int tempWidth = 1;
						for(int i2 = i + 1; i2 < tilesCopy.length && blocksMovement(tilesCopy[i2][j2]);i2++)
						{
							tempWidth++;
						}
						if(tempWidth < width2)
							width2 = tempWidth;
					}
					if(width2 == Integer.MAX_VALUE)
						width2 = 1;
					Rectangle r = new Rectangle(i, j, width1, height1);
					int width = width1;
					int height = height1;
					if(width1*height1 < width2*height2)
					{
						r.set(i, j, width2, height2);
						width = width2;
						height = height2;
					}
					for(int l = i; l < i +width; l++)
						for(int m = j; m < i+height; m++)
							tilesCopy[l][m] = null;
					tileRectangles.add(r);
				}
			}
		}
	}
	
	private boolean blocksMovement(Tile t)
	{
		return t!=null&&t.blocksMovement();
	}

}
