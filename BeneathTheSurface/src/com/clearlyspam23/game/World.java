package com.clearlyspam23.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class World {
	
	private Tile[][] tiles;
	private List<Entity> entities;
	private List<Entity> toRemove = new ArrayList<Entity>();
	private float gravity;
	private float terminalVelocity;
	private List<Rectangle> tileRectangles;
	private List<Tile> rectTypes;
	private PlayerEntity player;
	private Rectangle exitRectangle = new Rectangle();
	
	public World(int width, int height)
	{
		setTiles(new Tile[width][height]);
		entities = new ArrayList<Entity>();
	}
	
	public void update(float delta)
	{
		for(Entity e : toRemove)
			entities.remove(e);
		toRemove.clear();
		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			e.move(delta);
			if(Math.abs(e.getVelocity().y)>terminalVelocity)
				e.setVelocity(e.getVelocity().x, Math.max(-terminalVelocity, Math.min(e.getVelocity().y, terminalVelocity)));
			if(Math.abs(e.getVelocity().x)>terminalVelocity)
				e.setVelocity(Math.max(-terminalVelocity, Math.min(e.getVelocity().x, terminalVelocity)), e.getVelocity().y);
			e.update(delta);
		}
		for(int i = 0; i < entities.size(); i++)
		{
			Entity e = entities.get(i);
			for(int j = 0; j < tileRectangles.size(); j++)
				if(e.getRectangle().overlaps(tileRectangles.get(j)))
					e.collidesWithTile(tileRectangles.get(j), rectTypes.get(j));
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
		if(e.affectedByGravity())
			e.setAcceleration(0, -gravity);
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
		rectTypes = new ArrayList<Tile>();
		Tile[][] tilesCopy = new Tile[getWidth()][getHeight()];
		for(int i = 0; i < tilesCopy.length; i++)
			for(int j = 0; j < tilesCopy[i].length; j++)
				tilesCopy[i][j] = tiles[i][j];
		for(int i = 0; i < tilesCopy.length; i++)
		{
			for(int j = 0; j < tilesCopy[i].length; j++)
			{
				if(blocksMovement(tilesCopy[i][j]))
				{
					Tile t = tilesCopy[i][j];
					int width1 = 1;
					int height1 = Integer.MAX_VALUE;
					for(int i2 = i+1; i2 < tilesCopy.length && blocksMovementAndSame(tilesCopy[i2][j], t); i2++)
					{
						width1++;
						int tempHeight = 1;
						for(int j2 = j + 1; j2 < tilesCopy[i2].length && blocksMovementAndSame(tilesCopy[i2][j2], t);j2++)
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
					for(int j2 = j+1; j2 < tilesCopy[i].length && blocksMovementAndSame(tilesCopy[i][j2], t); j2++)
					{
						height2++;
						int tempWidth = 1;
						for(int i2 = i + 1; i2 < tilesCopy.length && blocksMovementAndSame(tilesCopy[i2][j2], t);i2++)
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
						for(int m = j; m < j+height; m++)
							tilesCopy[l][m] = null;
					tileRectangles.add(r);
					rectTypes.add(t);
				}
			}
		}
	}
	
	private boolean blocksMovement(Tile t)
	{
		return t!=null&&t.blocksMovement();
	}
	
	private boolean blocksMovementAndSame(Tile t, Tile other)
	{
		return blocksMovement(t)&&t==other;
	}

	public float getTerminalVelocity() {
		return terminalVelocity;
	}

	public void setTerminalVelocity(float terminalVelocity) {
		this.terminalVelocity = terminalVelocity;
	}

	public void removeEntity(Entity e) {
		toRemove.add(e);
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	public void setPlayer(PlayerEntity player) {
		this.player = player;
		addEntity(player);
	}

	public Rectangle getExitRectangle() {
		return exitRectangle;
	}

	public void setExitRectangle(float x, float y, float width, float height) {
		exitRectangle.set(x, y, width, height);
	}
	
	public boolean shouldExit()
	{
		return player.getRectangle()!=null&&player.getRectangle().overlaps(exitRectangle);
	}

}
