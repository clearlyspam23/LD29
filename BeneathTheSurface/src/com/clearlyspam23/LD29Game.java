package com.clearlyspam23;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.clearlyspam23.view.RenderStates;

public class LD29Game implements ApplicationListener {
	
	
	public Texture tileTexture;
	public Texture playerTexture;
	public Texture slimeTexture;
	public Texture weaponItemsTexture;
	public Texture weaponProjectileTexture;
	public Texture flyingTexture;
	public Map<RenderStates, Animation> slimeEnemyAnimations;
	public Map<RenderStates, Animation> flyingEnemyProjectile;
	public Map<RenderStates, Animation> flyingEnemyAnimations;
	public Map<RenderStates, Animation> playerAnimations;
	
	public BitmapFont textFont;
	
	public Sound jumpSound;
	public Sound fireSound;
	public Sound hitSound;
	
	
	
//	private OrthographicCamera camera;
	public SpriteBatch batch;
//	private Texture texture;
//	private Sprite sprite;
	
	private GameInstance i;
	
	@Override
	public void create() {		
		
		//load assets
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("Jump2.wav"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("Hit_Hurt.wav"));
		fireSound = Gdx.audio.newSound(Gdx.files.internal("Laser_Shoot6.wav"));
		tileTexture = new Texture(Gdx.files.internal("LD29Tiles.png"));
		weaponItemsTexture = new Texture(Gdx.files.internal("WeaponItems.png"));
		weaponProjectileTexture = new Texture(Gdx.files.internal("WeaponProjectiles.png"));
		playerTexture = new Texture(Gdx.files.internal("LD29Dude.png"));
		slimeTexture = new Texture(Gdx.files.internal("SlimeEnemy.png"));
		flyingTexture = new Texture(Gdx.files.internal("FlyingEnemy.png"));
		batch = new SpriteBatch();
		
		flyingEnemyProjectile = new HashMap<RenderStates, Animation>();
		flyingEnemyProjectile.put(RenderStates.idle, new Animation(1f, new TextureRegion(weaponProjectileTexture, 8, 0, 8, 8)));
		
		playerAnimations = new HashMap<RenderStates, Animation>();
		loadPlayerRenderMap(playerAnimations);
		
		slimeEnemyAnimations = new HashMap<RenderStates, Animation>();
		loadSlimeRenderMap(slimeEnemyAnimations);
		flyingEnemyAnimations = new HashMap<RenderStates, Animation>();
		loadFlyingRenderMap(flyingEnemyAnimations);
		textFont = new BitmapFont(Gdx.files.internal("OutlineText.fnt"), false);
		
		
		
		
		
		i = new GameInstance(this);
	}
	
	
	private void loadPlayerRenderMap(Map<RenderStates, Animation> renderMap)
	{
		TextureRegion[] regions = takeN(TextureRegion.split(playerTexture, 12, 20), 8);
		Animation a = new Animation(1f, takeN(regions, 1, 0));
		renderMap.put(RenderStates.idle, a);
		a = new Animation(0.15f, takeN(regions, 4, 0));
		renderMap.put(RenderStates.moving, a);
		a = new Animation(1f, takeN(regions, 1, 4));
		renderMap.put(RenderStates.weaponidle, a);
		a = new Animation(0.15f, takeN(regions, 4, 4));
		renderMap.put(RenderStates.weaponmoving, a);
		a = new Animation(1f, takeN(regions, 1, 1));
		renderMap.put(RenderStates.air, a);
		a = new Animation(1f, takeN(regions, 1, 5));
		renderMap.put(RenderStates.weaponair, a);
	}
	
	public static TextureRegion fixBleeding(TextureRegion region) {
		float x = region.getRegionX();
		float y = region.getRegionY();
		float width = region.getRegionWidth();
		float height = region.getRegionHeight();
		float invTexWidth = 1f / region.getTexture().getWidth();
		float invTexHeight = 1f / region.getTexture().getHeight();
		region.setRegion((x + .01f) * invTexWidth, (y+.01f) * invTexHeight, (x + width - .01f) * invTexWidth, (y + height - .01f) * invTexHeight);
		return region;
	}
	
	private void loadSlimeRenderMap(Map<RenderStates, Animation> renderMap)
	{
		TextureRegion[] regions = takeN(TextureRegion.split(slimeTexture, 17, 16), 4);
		Animation a = new Animation(1f, takeN(regions, 2, 0));
		renderMap.put(RenderStates.idle, a);
		a = new Animation(0.15f, takeN(regions, 2, 0));
		renderMap.put(RenderStates.moving, a);
		a = new Animation(1f, takeN(regions, 1, 1));
		renderMap.put(RenderStates.air, a);
		a = new Animation(0.125f, takeN(regions, 2, 2));
		renderMap.put(RenderStates.shooting, a);
	}
	
	private void loadFlyingRenderMap(Map<RenderStates, Animation> renderMap)
	{
		TextureRegion[] regions = takeN(TextureRegion.split(flyingTexture, 25, 16), 4);
		Animation a = new Animation(0.15f, takeN(regions, 2, 0));
		renderMap.put(RenderStates.idle, a);
		a = new Animation(0.15f, takeN(regions, 2, 0));
		renderMap.put(RenderStates.moving, a);
		a = new Animation(0.125f, takeN(regions, 2, 2));
		renderMap.put(RenderStates.shooting, a);
	}
	
	public TextureRegion[] takeN(TextureRegion[][] regions, int num)
	{
		TextureRegion[] ans = new TextureRegion[num];
		int index = 0;
		for(int i = 0; i < regions.length&&index < num;i++)
			for(int j = 0; j < regions[i].length&&index < num;j++)
				ans[index++] = fixBleeding(regions[i][j]);
		return ans;
	}
	
	public TextureRegion[] takeN(TextureRegion[] base, int num, int start)
	{
		TextureRegion[] ans = new TextureRegion[num];
		for(int i = 0; i < num; i++)
			ans[i] = base[i+start];
		return ans;
	}

	@Override
	public void dispose() {
		batch.dispose();
		tileTexture.dispose();
		playerTexture.dispose();
	}

	@Override
	public void render() {	
		
		float delta = Gdx.graphics.getDeltaTime();
		if(delta>0.05)
			return;
		i.render(delta);
		if(i.shouldRestart())
			i = new GameInstance(this);
//		Gdx.gl.glClearColor(133f/255f, 94f/255f, 94f/255f, 1);
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

//		Vector3 mouseLoc = view.getMouseIn(Gdx.input.getX(), Gdx.input.getY());
//		if(nextSpawnTime>0)
//			nextSpawnTime-=delta;
//		if(!startedSpawn&&model.getEntities().size()<2)
//		{
//			startedSpawn = true;
//		}
//		if(startedSpawn&&(nextSpawnTime<=0||countUnits()==1))
//		{
//			for(int i = 0; i < spawnsCount/5+1; i++)
//			{
//				GroundUnitEntity enemy = new GroundUnitEntity((float)(4 + Math.random()*(model.getWidth()-8)), (float)(4+Math.random()*(model.getHeight()-8)), 17f/16f, 1f, 1, 50);
//				EntityRenderer r = new EntityRenderer(enemy, slimeEnemyAnimations);
//				BasicGroundFollower f = new BasicGroundFollower(enemy, 0.5f, 10, 3f, 9f, r, table, weaponItemAnimations);
//				model.addEntity(enemy);
//				view.addEntityRenderer(r);
//				controller.addController(f);
//			}
//			for(int i = 0; i < (spawnsCount+2)/5; i++)
//			{
//				UnitEntity enemy = new FlyingUnitEntity((float)(4 + Math.random()*(model.getWidth()-8)), (float)(4+Math.random()*(model.getHeight()-8)), 25f/16f, 1f, 1, 25);
//				EntityRenderer r = new EntityRenderer(enemy, flyingEnemyAnimations);
//				BasicFlyingFollower f = new BasicFlyingFollower(enemy, 1f, 10f, 1f, 6f, 3f, 8f, 5, 5, 6f, 6f, r, table, weaponItemAnimations, flyingEnemyProjectile);
//				model.addEntity(enemy);
//				view.addEntityRenderer(r);
//				controller.addController(f);
//			}
//			spawnsCount++;
//			nextSpawnTime = MAX_SPAWN_DELAY;
//		}
//		controller.control(delta, mouseLoc.x, mouseLoc.y);
//		model.update(delta);
//		view.draw(delta);
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		sprite.draw(batch);
//		batch.end();
	}
	
//	private int countUnits()
//	{
//		int count = 0;
//		for(Entity e : model.getEntities())
//			if (e instanceof UnitEntity)
//				count++;
//		return count;
//	}

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
