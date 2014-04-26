package com.clearlyspam23;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.clearlyspam23.game.Entity;
import com.clearlyspam23.game.Tile;
import com.clearlyspam23.game.World;
import com.clearlyspam23.view.TestView;

public class LD29Game implements ApplicationListener {
	
	private static final int UP = 0x1;
	private static final int LEFT = 0x2;
	private static final int RIGHT = 0x4;
	
	private World world;
	private TestView view;
	private Entity player;
//	private OrthographicCamera camera;
//	private SpriteBatch batch;
//	private Texture texture;
//	private Sprite sprite;
	
	@Override
	public void create() {		
		
		world = new World(20, 20);
		world.setGravity(0.025f);
		Tile t = new Tile();
		t.blocksMovement = true;
		for(int i = 0; i < world.getWidth(); i++)
		{
			world.setTile(i, 0, t);
			world.setTile(i, 1, t);
		}
		world.setTile(10, 2, t);
		world.setTile(10, 3, t);
		world.buildTileRectangles();
		view = new TestView(world, 20, 20);
		player = new Entity(5f, 5f, 0.5f, 2f);
		world.addEntity(player);
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//		
//		camera = new OrthographicCamera(1, h/w);
//		batch = new SpriteBatch();
//		
//		texture = new Texture(Gdx.files.internal("data/libgdx.png"));
//		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		
//		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
//		
//		sprite = new Sprite(region);
//		sprite.setSize(0.9f, 0.9f * sprite.getHeight() / sprite.getWidth());
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);
	}

	@Override
	public void dispose() {
//		batch.dispose();
//		texture.dispose();
	}

	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		int keys = 0;
		
		if(Gdx.input.isKeyPressed(Input.Keys.W))
			keys|=UP;
		if(Gdx.input.isKeyPressed(Input.Keys.A))
			keys|=RIGHT;
		if(Gdx.input.isKeyPressed(Input.Keys.D))
			keys|=LEFT;
		player.setVelocity(0, 0);
		Vector2 velo = player.getVelocity();
		if((keys&UP)!=0)
			player.setVelocity(velo.x, 5f);
		if((keys&LEFT)!=0)
			player.setVelocity(velo.x+3, velo.y);
		if((keys&RIGHT)!=0)
			player.setVelocity(velo.x-3, velo.y);
		world.update(Gdx.graphics.getDeltaTime());
		view.draw(Gdx.graphics.getDeltaTime());
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		sprite.draw(batch);
//		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
