package com.clearlyspam23.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.clearlyspam23.game.Entity;
import com.clearlyspam23.game.PlayerEntity;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.WeaponEntity;
import com.clearlyspam23.game.World;

public class TestView {
	
	private ShapeRenderer testRenderer = new ShapeRenderer();
	private OrthographicCamera camera;
	
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
	
	public TestView(World world, float width, float height, float boundswidth, float boundsheight)
	{
		this.world = world;
		camera = new OrthographicCamera(width, height);
		camera.position.set(width/2, height/2, 0);
		camera.update();
		boundingRegion = new Vector2(boundswidth, boundsheight);
	}
	
	public void draw(float delta)
	{
		cameraBound(camera, world.getPlayer().getLocation(), boundingRegion);
		testRenderer.setProjectionMatrix(camera.combined);
		testRenderer.begin(ShapeType.Rectangle);
		testRenderer.setColor(Color.BLACK);
		for(Rectangle r : world.getTileRects())
		{
			testRenderer.rect(r.x, r.y, r.width, r.height);
		}
		testRenderer.setColor(Color.BLUE);
		for(Entity e : world.getEntities())
		{
			if(e instanceof PlayerEntity)
				testRenderer.setColor(Color.BLUE);
			else if(e instanceof UnitEntity)
				testRenderer.setColor(Color.GRAY);
			else if(e instanceof WeaponEntity)
				testRenderer.setColor(Color.ORANGE);
			else if(e instanceof ProjectileEntity)
			{
				if(((ProjectileEntity)e).getTeam()==0)
					testRenderer.setColor(Color.GREEN);
				else
					testRenderer.setColor(Color.RED);
			}
			Rectangle r = e.getRectangle();
			testRenderer.rect(r.x, r.y, r.width, r.height);
		}
		testRenderer.end();
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

}
