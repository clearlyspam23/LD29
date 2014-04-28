package com.clearlyspam23.logic;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.clearlyspam23.game.PlayerEntity;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.Weapon;
import com.clearlyspam23.game.WeaponEventListener;
import com.clearlyspam23.view.EntityRenderer;
import com.clearlyspam23.view.RenderStates;

public class PlayerController extends UnitController<PlayerEntity>{
	
	private static final int UP = 0x1;
	private static final int LEFT = 0x2;
	private static final int RIGHT = 0x4;
	private static final int SHOOT = 0x8;
	
	private int up;
	private int left;
	private int right;
	private int shoot;
	
	private Map<Weapon, Map<RenderStates, Animation>> weaponProjectileAnimations;
	
	private float dead;
	private int doEvents = 0;
	private Sound hit;
	private Sound jump;
	private Sound shootSound;

	public PlayerController(PlayerEntity t, int u, int l, int r, int s, EntityRenderer renderer, Map<Weapon, Map<RenderStates, Animation>> weaponProjectileAnimations, Sound jump, Sound hit, Sound shootSound) {
		super(t, renderer);
		up = u;
		left = l;
		right = r;
		shoot = s;
		this.weaponProjectileAnimations = weaponProjectileAnimations;
		this.jump = jump;
		this.hit = hit;
		this.shootSound = shootSound;
	}
	
	public boolean isRightOfPlayer(float x, float y)
	{
		return x-getEntity().getLocation().x>0;
	}

	@Override
	public void control(float delta, float x, float y) {
		
		if(doEvents>0)
		{
			if(dead>0)
				dead-=delta;
			else
			{
				if(doEvents==1)
				{
					System.out.println("spawn text");
					getEngine().getView().addText("GAME OVER", getEngine().getView().getScreenCamera().position.x, getEngine().getView().getScreenCamera().position.y);
					doEvents = 2;
				}
				else
				{
					getEngine().getView().addText("press space to restart", getEngine().getView().getScreenCamera().position.x, getEngine().getView().getScreenCamera().position.y-getEngine().getView().getScreenCamera().viewportWidth/8);
					doEvents = 0;
				}
				dead = 1f;
			}
		}
		
		if(getEntity().isDead())
			return;
		
		int keys = 0;
		getEntity().setCurrentFacing((isRightOfPlayer(x, y)) ? UnitEntity.Facing.right : UnitEntity.Facing.left);
			
		if(Gdx.input.isKeyPressed(up))
			keys|=UP;
		if(Gdx.input.isKeyPressed(right))
			keys|=RIGHT;
		if(Gdx.input.isKeyPressed(left))
			keys|=LEFT;
		if(Gdx.input.isButtonPressed(shoot))
			keys|=SHOOT;
		if((keys&UP)!=0)
			getEntity().jump(11f);
		if((keys&LEFT)!=0)
			getEntity().unit_move(1, 5);
		if((keys&RIGHT)!=0)
			getEntity().unit_move(-1, 5);
		if((keys&SHOOT)!=0)
			processShoot(getEntity());
		RenderStates s = getRenderer().getCurrentState();
		if(!getEntity().isOnGround())
			getRenderer().setCurrentState(getEntity().getPrimaryWeapon()==null? RenderStates.air : RenderStates.weaponair);
		else if(!getEntity().isOnFlatGround())
			getRenderer().setCurrentState(getEntity().getPrimaryWeapon()==null? RenderStates.idle : RenderStates.weaponidle);
		else if(Math.abs(getEntity().getMovement().x)>0.005)
			getRenderer().setCurrentState(getEntity().getPrimaryWeapon()==null? RenderStates.moving : RenderStates.weaponmoving);
		else
			getRenderer().setCurrentState(getEntity().getPrimaryWeapon()==null? RenderStates.idle : RenderStates.weaponidle);
		if(getRenderer().getCurrentState()!=s)
			getRenderer().setStateTime(0);
	}

	@Override
	public void onDeath(UnitEntity e) {
		doEvents = 1;
		dead = 1.0f;
		getEngine().getView().removeRenderer(getRenderer());
		getEngine().setGameOver(true);
	}
	
	public void update(float delta, float mouseX, float mouseY)
	{
		control(delta, mouseX, mouseY);
	}
	
	public void processShoot(PlayerEntity e)
	{
		Map<Weapon, List<ProjectileEntity>> projectiles = e.shoot();
		for(Entry<Weapon, List<ProjectileEntity>> ew : projectiles.entrySet())
		{
			for(ProjectileEntity pe : ew.getValue())
			{
				e.getWorld().addEntity(pe);
				EntityRenderer r = new EntityRenderer(pe, weaponProjectileAnimations.get(ew.getKey()));
				getEngine().getView().addEntityRenderer(r);
			}
		}
		if(!projectiles.isEmpty())
			shootSound.play(1f);
	}

	@Override
	public void onJump(UnitEntity e) {
		jump.play(1f);
	}

	@Override
	public void onHit(UnitEntity e) {
		hit.play(1f);
	}

}
