package com.clearlyspam23.logic;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.clearlyspam23.game.PlayerEntity;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.Weapon;
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

	public PlayerController(PlayerEntity t, int u, int l, int r, int s, EntityRenderer renderer, Map<Weapon, Map<RenderStates, Animation>> weaponProjectileAnimations) {
		super(t, renderer);
		up = u;
		left = l;
		right = r;
		shoot = s;
		this.weaponProjectileAnimations = weaponProjectileAnimations;
	}
	
	public boolean isRightOfPlayer(float x, float y)
	{
		return x-getEntity().getLocation().x>0;
	}

	@Override
	public void control(float delta, float x, float y) {
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
		// TODO Auto-generated method stub
		
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
	}

}
