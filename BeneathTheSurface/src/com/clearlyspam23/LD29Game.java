package com.clearlyspam23;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.clearlyspam23.game.Entity;
import com.clearlyspam23.game.FlyingUnitEntity;
import com.clearlyspam23.game.GroundUnitEntity;
import com.clearlyspam23.game.PlayerEntity;
import com.clearlyspam23.game.Tile;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.Weapon;
import com.clearlyspam23.game.WeaponEntity;
import com.clearlyspam23.game.World;
import com.clearlyspam23.game.weapons.PrimaryWeapon;
import com.clearlyspam23.game.weapons.UpgradingWeapon;
import com.clearlyspam23.logic.BasicFlyingFollower;
import com.clearlyspam23.logic.BasicGroundFollower;
import com.clearlyspam23.logic.DropTable;
import com.clearlyspam23.logic.Engine;
import com.clearlyspam23.logic.PlayerController;
import com.clearlyspam23.view.EntityRenderer;
import com.clearlyspam23.view.RenderStates;
import com.clearlyspam23.view.TileRenderer;
import com.clearlyspam23.view.WorldView;

public class LD29Game implements ApplicationListener {
	
	private World model;
	private WorldView view;
	private Engine controller;
	private PlayerEntity player;
	private DropTable table;
	
	private static final float MAX_SPAWN_DELAY = 5f;
	
	private boolean startedSpawn = false;
	private float nextSpawnTime = 0;
	private int spawnsCount = 0;
	
	private static final int JUMP = Input.Keys.W;
	private static final int MOVE_LEFT = Input.Keys.D;
	private static final int MOVE_RIGHT = Input.Keys.A;
	private static final int FIRE = Input.Buttons.LEFT;
	
	private Texture tileTexture;
	private Texture playerTexture;
	private Texture slimeTexture;
	private Texture weaponItemsTexture;
	private Texture weaponProjectileTexture;
	private Texture flyingTexture;
	private Map<RenderStates, Animation> slimeEnemyAnimations;
	private Map<Weapon, Map<RenderStates, Animation>> weaponItemAnimations;
	private Map<Weapon, Map<RenderStates, Animation>> weaponProjectileAnimations;
	private Map<RenderStates, Animation> flyingEnemyProjectile;
	private Map<RenderStates, Animation> flyingEnemyAnimations;
	
//	private OrthographicCamera camera;
	private SpriteBatch batch;
//	private Texture texture;
//	private Sprite sprite;
	
	@Override
	public void create() {		
		
		//load assets
		
		tileTexture = new Texture(Gdx.files.internal("LD29Tiles.png"));
		weaponItemsTexture = new Texture(Gdx.files.internal("WeaponItems.png"));
		weaponProjectileTexture = new Texture(Gdx.files.internal("WeaponProjectiles.png"));
		playerTexture = new Texture(Gdx.files.internal("LD29Dude.png"));
		slimeTexture = new Texture(Gdx.files.internal("SlimeEnemy.png"));
		flyingTexture = new Texture(Gdx.files.internal("FlyingEnemy.png"));
		batch = new SpriteBatch();
		
		model = new World(60, 20);
		model.setGravity(25f);
		model.setTerminalVelocity(50f);
		Tile t = new Tile();
		t.blocksMovement = true;
		t.movementCap = 7f;
		for(int i = 0; i < model.getWidth(); i++)
		{
			model.setTile(i, 0, t);
			model.setTile(i, 1, t);
			model.setTile(i, model.getHeight()-1, t);
			model.setTile(i, model.getHeight()-2, t);
		}
		for(int j = 0; j < model.getHeight(); j++)
		{
			model.setTile(0, j, t);
			model.setTile(1, j, t);
			model.setTile(model.getWidth()-1, j, t);
			model.setTile(model.getWidth()-2, j, t);
		}
		model.setTile(10, 2, t);
		model.setTile(10, 3, t);
		
		model.setTile(20, 6, t);
		model.setTile(21, 6, t);
		model.setTile(22, 6, t);
		model.setTile(23, 6, t);
		
		model.setTile(41, 2, t);
		model.setTile(42, 2, t);
		model.setTile(43, 2, t);
		model.setTile(44, 2, t);
		model.setTile(41, 3, t);
		model.setTile(42, 3, t);
		model.setTile(43, 3, t);
		model.setTile(44, 3, t);
		
		model.buildTileRectangles();
		
		
		Map<Tile, TileRenderer> tileMap = new HashMap<Tile, TileRenderer>();
		tileMap.put(t, new TileRenderer(takeN(TextureRegion.split(tileTexture, 16, 16), 16)));
		view = new WorldView(batch, model, tileMap, 18, 18, 1, 1);
		view.calculateRenderData();
		
		loadWeapons();
		
		//PrimaryWeapon w = new PrimaryWeapon(0.5f, 10f, 10f, 1, 3, 60f, 0.25f, 0.25f);
		player = new PlayerEntity(5f, 5f, 0.75f, 1.25f, 100);
		model.setPlayer(player);
		WeaponEntity pe = new WeaponEntity(15f, 5f, 1f, 1f, primary);
		model.addEntity(pe);
		
		Map<RenderStates, Animation> playerAnimations = new HashMap<RenderStates, Animation>();
		loadPlayerRenderMap(playerAnimations);
		EntityRenderer playerRenderer = new EntityRenderer(player, playerAnimations);
		view.addEntityRenderer(playerRenderer);
		EntityRenderer primaryWeaponRenderer = new EntityRenderer(pe, weaponItemAnimations.get(primary));
		view.addEntityRenderer(primaryWeaponRenderer);
		
		slimeEnemyAnimations = new HashMap<RenderStates, Animation>();
		loadSlimeRenderMap(slimeEnemyAnimations);
		flyingEnemyAnimations = new HashMap<RenderStates, Animation>();
		loadFlyingRenderMap(flyingEnemyAnimations);
		
		controller = new Engine(view);
		controller.addController(new PlayerController(player, JUMP, MOVE_LEFT, MOVE_RIGHT, FIRE, playerRenderer, weaponProjectileAnimations));
		table = buildDropTable();
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//		
//		camera = new OrthographicCamera(1, h/w);
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
	
	private PrimaryWeapon primary;
	private Weapon sniper;
	private Weapon scatter;
	private Weapon drums;
	private Weapon heavy;
	private Weapon homing;
	private Weapon speedup;
	private Weapon cooldowndown;
	private Weapon spreadDown;
	private Weapon durationUp;
	
	public void loadWeapons()
	{
		//weapon part of it
		primary = new PrimaryWeapon(0.5f, 10f, 6f, 1, 10, 60f, 0.25f, 0.25f);
		sniper = new UpgradingWeapon(-0.15f, 3f, 0.25f, 0, 1, 0.10f, 0f, 0f);
		scatter = new UpgradingWeapon(0f, 0f, -1f, 1, 0, 0f, 0f, 0f);
		drums = new UpgradingWeapon(0.2f, -1f, 0f, 0, 0, -0.05f, 0f, 0f);
		//render part of it
		TextureRegion[][] regions = TextureRegion.split(weaponItemsTexture, 16, 16);
		weaponItemAnimations = new HashMap<Weapon, Map<RenderStates, Animation>>();
		Map<RenderStates, Animation> m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][0]));
		weaponItemAnimations.put(primary, m);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][5]));
		weaponItemAnimations.put(sniper, m);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][4]));
		weaponItemAnimations.put(drums, m);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][3]));
		weaponItemAnimations.put(scatter, m);
		
		weaponProjectileAnimations = new HashMap<Weapon, Map<RenderStates, Animation>>();
		regions = TextureRegion.split(weaponProjectileTexture, 8, 8);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][0]));
		weaponProjectileAnimations.put(primary, m);
		flyingEnemyProjectile = new HashMap<RenderStates, Animation>();
		flyingEnemyProjectile.put(RenderStates.idle, new Animation(1f, regions[0][1]));
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
				ans[index++] = regions[i][j];
		return ans;
	}
	
	public TextureRegion[] takeN(TextureRegion[] base, int num, int start)
	{
		TextureRegion[] ans = new TextureRegion[num];
		for(int i = 0; i < num; i++)
			ans[i] = base[i+start];
		return ans;
	}
	
	public DropTable buildDropTable()
	{
		//float chance = 10f;
		DropTable t = new DropTable();
		t.addEntry(10f, sniper);
		t.addEntry(10f, scatter);
		t.addEntry(10f, drums);
//		UpgradingWeapon w = new UpgradingWeapon(0.25f, 0f, 0f, 0, 0, 0f, 0f, 0f);
//		t.addEntry(chance, w);
//		w = new UpgradingWeapon(0f, 3f, 0f, 0, 0, 0f, 0f, 0f);
//		t.addEntry(chance, w);
//		w = new UpgradingWeapon(0f, 0f, 0f, 1, 0, 0f, 0f, 0f);
//		t.addEntry(chance, w);
//		w = new UpgradingWeapon(0f, 0f, 0f, 0, 1, 0f, 0f, 0f);
//		t.addEntry(chance, w);
//		w = new UpgradingWeapon(0f, 0f, 0f, 0, 0, 0.05f, 0f, 0f);
//		t.addEntry(chance, w);
//		w = new UpgradingWeapon(0f, 0f, 0f, 0, 0, 0f, 0.125f, 0.125f);
//		t.addEntry(chance, w);
		return t;
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
		Gdx.gl.glClearColor(133f/255f, 94f/255f, 94f/255f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		Vector3 mouseLoc = view.getMouseIn(Gdx.input.getX(), Gdx.input.getY());
		if(nextSpawnTime>0)
			nextSpawnTime-=delta;
		if(!startedSpawn&&model.getEntities().size()<2)
		{
			startedSpawn = true;
		}
		if(startedSpawn&&(nextSpawnTime<=0||countUnits()==1))
		{
			for(int i = 0; i < spawnsCount/5+1; i++)
			{
				GroundUnitEntity enemy = new GroundUnitEntity((float)(4 + Math.random()*(model.getWidth()-8)), (float)(4+Math.random()*(model.getHeight()-8)), 17f/16f, 1f, 1, 50);
				EntityRenderer r = new EntityRenderer(enemy, slimeEnemyAnimations);
				BasicGroundFollower f = new BasicGroundFollower(enemy, 0.5f, 10, 3f, 9f, r, table, weaponItemAnimations);
				model.addEntity(enemy);
				view.addEntityRenderer(r);
				controller.addController(f);
			}
			for(int i = 0; i < (spawnsCount+2)/5; i++)
			{
				UnitEntity enemy = new FlyingUnitEntity((float)(4 + Math.random()*(model.getWidth()-8)), (float)(4+Math.random()*(model.getHeight()-8)), 25f/16f, 1f, 1, 25);
				EntityRenderer r = new EntityRenderer(enemy, flyingEnemyAnimations);
				BasicFlyingFollower f = new BasicFlyingFollower(enemy, 1f, 10f, 1f, 6f, 3f, 8f, 5, 5, 6f, 6f, r, table, weaponItemAnimations, flyingEnemyProjectile);
				model.addEntity(enemy);
				view.addEntityRenderer(r);
				controller.addController(f);
			}
			spawnsCount++;
			nextSpawnTime = MAX_SPAWN_DELAY;
		}
		controller.control(delta, mouseLoc.x, mouseLoc.y);
		model.update(delta);
		view.draw(delta);
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
//		sprite.draw(batch);
//		batch.end();
	}
	
	private int countUnits()
	{
		int count = 0;
		for(Entity e : model.getEntities())
			if (e instanceof UnitEntity)
				count++;
		return count;
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
