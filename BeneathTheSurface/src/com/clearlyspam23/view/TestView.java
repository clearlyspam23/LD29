package com.clearlyspam23.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.clearlyspam23.game.Entity;
import com.clearlyspam23.game.World;

public class TestView {
	
	private ShapeRenderer testRenderer = new ShapeRenderer();
	private OrthographicCamera camera;
	private World world;
	
	public TestView(World world, float width, float height)
	{
		this.world = world;
		camera = new OrthographicCamera(width, height);
		camera.position.set(width/2, height/2, 0);
		camera.update();
	}
	
	public void draw(float delta)
	{
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
			Rectangle r = e.getRectangle();
			testRenderer.rect(r.x, r.y, r.width, r.height);
		}
		testRenderer.end();
	}

}
