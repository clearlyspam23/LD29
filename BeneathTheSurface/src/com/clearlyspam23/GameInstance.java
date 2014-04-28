package com.clearlyspam23;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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
import com.clearlyspam23.game.weapons.HeavyWeapon;
import com.clearlyspam23.game.weapons.PrimaryWeapon;
import com.clearlyspam23.game.weapons.SniperWeapon;
import com.clearlyspam23.game.weapons.SprayWeapon;
import com.clearlyspam23.game.weapons.UpgradingWeapon;
import com.clearlyspam23.logic.BasicFlyingFollower;
import com.clearlyspam23.logic.BasicGroundFollower;
import com.clearlyspam23.logic.DropTable;
import com.clearlyspam23.logic.Engine;
import com.clearlyspam23.logic.PlayerController;
import com.clearlyspam23.view.EntityRenderer;
import com.clearlyspam23.view.HealthBarView;
import com.clearlyspam23.view.RenderStates;
import com.clearlyspam23.view.TileRenderer;
import com.clearlyspam23.view.WorldView;

public class GameInstance {
	
	private static final int JUMP = Input.Keys.W;
	private static final int MOVE_LEFT = Input.Keys.D;
	private static final int MOVE_RIGHT = Input.Keys.A;
	private static final int FIRE = Input.Buttons.LEFT;
	
	private static final float MAX_SPAWN_DELAY = 5f;
	
	private boolean startedSpawn = false;
	private float nextSpawnTime = 0;
	private int spawnsCount = 0;
	
	private World model;
	private WorldView view;
	private Engine controller;
	private PlayerEntity player;
	private DropTable table;
	private DropTable specialTable;
	
	private Map<Weapon, Map<RenderStates, Animation>> weaponItemAnimations;
	private Map<Weapon, Map<RenderStates, Animation>> weaponProjectileAnimations;
	Map<Tile, TileRenderer> tileMap;
	private Tile t;
	
	private LD29Game game;
	
	private boolean startPressed;
	private boolean startLeft;
	private int depth;
	
	public GameInstance(LD29Game game)
	{
		this.game = game;
		
		t = new Tile();
		t.blocksMovement = true;
		t.movementCap = 7f;
		loadWeapons();
		tileMap = new HashMap<Tile, TileRenderer>();
		tileMap.put(t, new TileRenderer(game.takeN(TextureRegion.split(game.tileTexture, 16, 16), 16)));
		player = new PlayerEntity(5f, 5f, 0.75f, 1.25f, 100);
		table = buildNormalDropTable();
		specialTable = buildSpecialDropTable();
		
		
		model = generateTreasureWorld(t, false);
		populateInitialWorld(player, false, 1);
		startLeft = true;
	}
	
	public boolean isGameOver()
	{
		return false;
	}
	
	public void render(float delta) {	
		
		Gdx.gl.glClearColor(133f/255f, 94f/255f, 94f/255f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		Vector3 mouseLoc = view.getMouseIn(Gdx.input.getX(), Gdx.input.getY());
		controller.control(delta, mouseLoc.x, mouseLoc.y);
		model.update(delta);
		view.draw(delta);
		if(controller.isGameOver())
		{
			if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
				startPressed = true;
		}
		else if(model.shouldExit())
		{
			if(player.getPrimaryWeapon()==null)
			{
				player.setPrimaryWeapon(primary);
				player.addWeapon(primary);
			}
			double rand = Math.random();
			if(rand<=0.1)
			{
				model = generateTreasureWorld(t, startLeft);
				populateTreasureWorld(player, startLeft, depth);
			}
			else
			{
				model = generateWorld2(t, startLeft);
				populateWorld2(player, startLeft, depth);
			}
			startLeft = !startLeft;
			depth++;
		}
	}
	
	public boolean shouldRestart()
	{
		return controller.isGameOver()&&startPressed;
	}
	
	private int countUnits()
	{
		int count = 0;
		for(Entity e : model.getEntities())
			if (e instanceof UnitEntity)
				count++;
		return count;
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
	private Weapon damageUp;
	
	public void loadWeapons()
	{
		//weapon part of it
		primary = new PrimaryWeapon(0.4f, 10f, 6f, 1, 10, 60f, 0.25f, 0.25f);
		sniper = new SniperWeapon(-0.5f, 3f, 0.25f, 0, 15, 0.10f, 0f, 0f);
		scatter = new UpgradingWeapon(0f, 0f, -1f, 1, 0, 0f, 0f, 0f);
		drums = new SprayWeapon(0.5f, 0f, 0f, 0, 0, -0.15f, 0f, 0f);
		heavy = new HeavyWeapon(0f, 0f, 0f, 0, 15, 0f, 0.25f, 0.25f);
		cooldowndown = new UpgradingWeapon(0.03f, 0f, 0f, 0, 0, 0f, 0f, 0f);
		speedup = new UpgradingWeapon(0, 0.25f, 0f, 0, 0, 0f, 0f, 0f);
		spreadDown = new UpgradingWeapon(0f, 0f, 0f, 0, 0, 0.03f, 0f, 0f);
		durationUp = new UpgradingWeapon(0f, 0f, 0.1f, 0, 0, 0f, 0f, 0f);
		damageUp = new UpgradingWeapon(0f, 0f, 0f, 0, 1, 0f, 0f, 0f);
		//render part of it
		TextureRegion[][] regions = TextureRegion.split(game.weaponItemsTexture, 16, 16);
		weaponItemAnimations = new HashMap<Weapon, Map<RenderStates, Animation>>();
		Map<RenderStates, Animation> m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][0]));
		weaponItemAnimations.put(primary, m);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][5]));
		weaponItemAnimations.put(sniper, m);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][2]));
		weaponItemAnimations.put(heavy, m);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][4]));
		weaponItemAnimations.put(drums, m);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][3]));
		weaponItemAnimations.put(scatter, m);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][6]));
		weaponItemAnimations.put(speedup, m);
		weaponItemAnimations.put(cooldowndown, m);
		weaponItemAnimations.put(spreadDown, m);
		weaponItemAnimations.put(durationUp, m);
		weaponItemAnimations.put(damageUp, m);
		
		weaponProjectileAnimations = new HashMap<Weapon, Map<RenderStates, Animation>>();
		regions = TextureRegion.split(game.weaponProjectileTexture, 8, 8);
		m = new HashMap<RenderStates, Animation>();
		m.put(RenderStates.idle, new Animation(1f, regions[0][0]));
		weaponProjectileAnimations.put(primary, m);
	}
	
	
	
	private HealthBarView loadHealthBarView()
	{
		TextureRegion overlay = new TextureRegion(game.weaponProjectileTexture, 0, 8, 56, 8);
		TextureRegion health = new TextureRegion(game.weaponProjectileTexture, 0, 16, 56, 8);
		TextureRegion lowHealth = new TextureRegion(game.weaponProjectileTexture, 0, 24, 56, 8);
		return new HealthBarView(overlay, health, lowHealth);
	}
	
	public DropTable buildNormalDropTable()
	{
		//float chance = 10f;
		DropTable t = new DropTable();
		t.addEntry(5f, speedup);
		t.addEntry(5f, cooldowndown);
		t.addEntry(5f, spreadDown);
		t.addEntry(5f, durationUp);
		t.addEntry(5f, damageUp);
		return t;
	}
	
	private void populateWorldWithEnemies(World world, WorldView view, Engine controller, int depth)
	{
		for(int i = 0; i < depth+1; i++)
		{
			GroundUnitEntity enemy = new GroundUnitEntity((float)(4 + Math.random()*(model.getWidth()-8)), (float)(4+Math.random()*(model.getHeight()-8)), 17f/16f, 1f, 1, 50);
			EntityRenderer r = new EntityRenderer(enemy, game.slimeEnemyAnimations);
			BasicGroundFollower f = new BasicGroundFollower(enemy, 0.5f, 10, 3f, 9f, r, table, weaponItemAnimations, game.jumpSound, game.hitSound);
			model.addEntity(enemy);
			view.addEntityRenderer(r);
			controller.addController(f);
		}
		for(int i = 0; i < (depth-3); i++)
		{
			UnitEntity enemy = new FlyingUnitEntity((float)(4 + Math.random()*(model.getWidth()-8)), (float)(4+Math.random()*(model.getHeight()-8)), 25f/16f, 1f, 1, 25);
			EntityRenderer r = new EntityRenderer(enemy, game.flyingEnemyAnimations);
			BasicFlyingFollower f = new BasicFlyingFollower(enemy, 1f, 10f, 1f, 6f, 3f, 8f, 5, 5, 6f, 6f, r, table, weaponItemAnimations, game.flyingEnemyProjectile, game.hitSound, game.fireSound);
			model.addEntity(enemy);
			view.addEntityRenderer(r);
			controller.addController(f);
		}
	}
	
	private void populateWorld(PlayerEntity e, boolean leftExit)
	{
		if(leftExit)
			e.setLocation(model.getWidth()-3, model.getHeight()-2);
		else
			e.setLocation(3 ,model.getHeight()-2);
		view = new WorldView(game.batch, model, tileMap, 17, 17, 1, 1);
		view.calculateRenderData();
		view.setFont(game.textFont);
		
		model.setPlayer(player);
		
		
		
		EntityRenderer playerRenderer = new EntityRenderer(player, game.playerAnimations);
		view.addEntityRenderer(playerRenderer);
		view.setHealthBar(loadHealthBarView());
		
		controller = new Engine(view);
		controller.addController(new PlayerController(player, JUMP, MOVE_LEFT, MOVE_RIGHT, FIRE, playerRenderer, weaponProjectileAnimations, game.jumpSound, game.hitSound, game.fireSound));
	}
	
	private World generateWorldBase(Tile t, boolean leftExit)
	{
		World model = new World(60, 20);
		model.setGravity(25f);
		model.setTerminalVelocity(50f);
		for(int i = 0; i < model.getWidth(); i++)
		{
			model.setTile(i, 0, t);
			model.setTile(i, 1, t);
			model.setTile(i, 2, t);
			model.setTile(i, model.getHeight()-1, t);
			model.setTile(i, model.getHeight()-2, t);
			model.setTile(i, model.getHeight()-3, t);
		}
		for(int j = 0; j < model.getHeight(); j++)
		{
			model.setTile(0, j, t);
			model.setTile(1, j, t);
			model.setTile(model.getWidth()-1, j, t);
			model.setTile(model.getWidth()-2, j, t);
		}
		if(leftExit)
		{
			model.setTile(2, 2, null);
			model.setTile(3, 2, null);
			model.setTile(2, 1, null);
			model.setTile(3, 1, null);
			model.setExitRectangle(2, 1, 2, 1);
			model.setTile(model.getWidth()-3, model.getHeight()-2, null);
			model.setTile(model.getWidth()-4, model.getHeight()-2, null);
			model.setTile(model.getWidth()-3, model.getHeight()-3, null);
			model.setTile(model.getWidth()-4, model.getHeight()-3, null);
		}
		else
		{
			model.setTile(model.getWidth()-3, 2, null);
			model.setTile(model.getWidth()-4, 2, null);
			model.setTile(model.getWidth()-3, 1, null);
			model.setTile(model.getWidth()-4, 1, null);
			model.setExitRectangle(model.getWidth()-4, 1, 2, 1);
			model.setTile(2, model.getHeight()-2, null);
			model.setTile(3, model.getHeight()-2, null);
			model.setTile(2, model.getHeight()-3, null);
			model.setTile(3, model.getHeight()-3, null);
		}
		return model;
	}
	
	private void populateWorld2(PlayerEntity e, boolean leftExit, int depth)
	{
		populateWorld(e, leftExit);
		populateWorldWithEnemies(model, view, controller, depth);
	}
	
	private World generateWorld2(Tile t, boolean leftExit)
	{
		World model = generateWorldBase(t, leftExit);
		
		model.setTile(10, 3, t);
		model.setTile(10, 4, t);
		
		model.setTile(20, 7, t);
		model.setTile(21, 7, t);
		model.setTile(22, 7, t);
		model.setTile(23, 7, t);
		
		model.setTile(41, 3, t);
		model.setTile(42, 3, t);
		model.setTile(43, 3, t);
		model.setTile(44, 3, t);
		model.setTile(41, 4, t);
		model.setTile(42, 4, t);
		model.setTile(43, 4, t);
		model.setTile(44, 4, t);
		
		model.buildTileRectangles();
		return model;
	}
	
	private void populateWorld3(PlayerEntity e, boolean leftExit, int depth)
	{
		populateWorld(e, leftExit);
		populateWorldWithEnemies(model, view, controller, depth);
	}
	
	private World generateWorld3(Tile t, boolean leftExit)
	{
		World model = generateWorldBase(t, leftExit);
		
		model.setTile(10, 3, t);
		model.setTile(10, 4, t);
		
		model.setTile(20, 7, t);
		model.setTile(21, 7, t);
		model.setTile(22, 7, t);
		model.setTile(23, 7, t);
		
		model.setTile(41, 3, t);
		model.setTile(42, 3, t);
		model.setTile(43, 3, t);
		model.setTile(44, 3, t);
		model.setTile(41, 4, t);
		model.setTile(42, 4, t);
		model.setTile(43, 4, t);
		model.setTile(44, 4, t);
		
		model.buildTileRectangles();
		return model;
	}
	
	private void populateInitialWorld(PlayerEntity e, boolean leftExit, int depth)
	{
		populateWorld(e, leftExit);
		WeaponEntity pe = new WeaponEntity(30.5f, 5f, 1f, 1f, primary);
		model.addEntity(pe);
		
		EntityRenderer primaryWeaponRenderer = new EntityRenderer(pe, weaponItemAnimations.get(primary));
		view.addEntityRenderer(primaryWeaponRenderer);
	}
	
	private void populateTreasureWorld(PlayerEntity e, boolean leftExit, int depth)
	{
		populateWorld(e, leftExit);
		Weapon w = (Weapon) specialTable.getValue();
		WeaponEntity pe = new WeaponEntity(30.5f, 5f, 1f, 1f, w);
		model.addEntity(pe);
		
		EntityRenderer primaryWeaponRenderer = new EntityRenderer(pe, weaponItemAnimations.get(w));
		view.addEntityRenderer(primaryWeaponRenderer);
	}
	
	private World generateTreasureWorld(Tile t, boolean leftExit)
	{
		World model = generateWorldBase(t, leftExit);
		model.setTile(10, 2, t);
		model.setTile(10, 3, t);
		
		model.setTile(30, 3, t);
		model.setTile(31, 3, t);
		model.setTile(30, 4, t);
		model.setTile(31, 4, t);
		
		model.buildTileRectangles();
		return model;
	}
	
	public DropTable buildSpecialDropTable()
	{
		DropTable t = new DropTable();
		t.addEntry(25f, heavy);
		t.addEntry(25f, sniper);
		t.addEntry(25f, scatter);
		t.addEntry(25f, drums);
		return t;
	}

}
