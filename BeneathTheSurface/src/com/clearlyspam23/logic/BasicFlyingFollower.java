package com.clearlyspam23.logic;

import java.util.Map;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.clearlyspam23.game.PlayerEntity;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.Weapon;
import com.clearlyspam23.game.WeaponEntity;
import com.clearlyspam23.view.EntityRenderer;
import com.clearlyspam23.view.RenderStates;

public class BasicFlyingFollower extends UnitController<UnitEntity>{
	
	private float attackDelay;
	private float bigAttackDelay;
	private float chargeTime;
	private float maxDistance;
	private float minDistance;
	private int bigProjectileCount;
	private int attackDamage;
	private float movementAcceleration;
	private float movementCap;
	private float minimumHeight;
	private DropTable table;
	private Map<Weapon, Map<RenderStates, Animation>> weaponRenderMap;
	private Map<RenderStates, Animation> projectileAnim;
	
	private float attackTimer;
	private float bigAttackTimer;
	private float chargeDelay;
	
	private Sound hit;
	private Sound shoot;

	public BasicFlyingFollower(UnitEntity t, float attackDelay, float bigAttackDelay, float chargeTime, float maxDistance, float minDistance, float minimumHeight, 
			int attackDamage, int bigProjectileCount, float movementAcceleration, float movementCap, EntityRenderer renderer, 
			DropTable table, Map<Weapon, Map<RenderStates, Animation>> weaponRenderMap, Map<RenderStates, Animation> projectileAnim,
			Sound hit, Sound shoot) {
		super(t, renderer);
		this.attackDelay = attackTimer =attackDelay;
		this.bigAttackDelay = bigAttackTimer = bigAttackDelay;
		this.attackDamage = attackDamage;
		this.movementAcceleration = movementAcceleration;
		this.movementCap = movementCap;
		this.table = table;
		this.weaponRenderMap = weaponRenderMap;
		this.projectileAnim = projectileAnim;
		this.maxDistance = maxDistance;
		this.minDistance = minDistance;
		this.chargeTime = chargeTime;
		this.bigProjectileCount = bigProjectileCount;
		this.minimumHeight = minimumHeight;
		this.hit = hit;
		this.shoot = shoot;
	}

	@Override
	public void control(float delta, float mouseX, float mouseY) {
		PlayerEntity e = getEntity().getWorld().getPlayer();
		Vector2 vec = new Vector2(e.getLocation());
		vec.sub(getEntity().getLocation());
		if(chargeDelay>0)
		{
			chargeDelay-=delta;
			if(chargeDelay<=0)
			{
				float step = 90;
				if(bigProjectileCount-1!=0)
					step/=bigProjectileCount;
				float angle = -45;
				if(bigProjectileCount==1)
					angle = 0;
				for(int i = 0; i < bigProjectileCount; i++)
				{
					float a = angle+vec.angle();
					float sin = (float) Math.sin(Math.toRadians(a));
					float cos = (float) Math.cos(Math.toRadians(a));
					fireProjectile(cos, sin);
					angle+=step;
				}
				shoot.play(0.5f);
			}
			return;
		}
		float distance = vec.len();
		vec.nor();
		if(distance>minDistance)
			getEntity().setMovementAcceleration(vec.x*movementAcceleration, vec.y*movementAcceleration);
		else
			getEntity().setMovementAcceleration(-vec.x*movementAcceleration, -vec.y*movementAcceleration);
		if(getEntity().getMovement().len()>movementCap)
		{
			getEntity().getMovement().nor();
			getEntity().getMovement().x*=movementCap;
			getEntity().getMovement().y*=movementCap;
		}
		if(getEntity().getLocation().y<minimumHeight)
		{
			Vector2 move = new Vector2(getEntity().getMovementAcceleration());
			move.nor();
			getEntity().setMovementAcceleration(move.x*movementAcceleration, Math.abs(move.y)*movementAcceleration);
		}
		getRenderer().setCurrentState(RenderStates.idle);
		if(distance<maxDistance)
		{
			bigAttackTimer-=delta;
			if(bigAttackTimer<=0)
			{
				getEntity().setMovementAcceleration(0, 0);
				getEntity().setMovement(0, 0);
				getRenderer().setCurrentState(RenderStates.shooting);
				bigAttackTimer+=bigAttackDelay;
				chargeDelay = chargeTime;
			}
			else
			{
				attackTimer-=delta;
				if(attackTimer<=0)
				{
					attackTimer+=attackDelay;
					fireProjectile(vec.x, vec.y);
					shoot.play(0.5f);
				}
			}
		}
	}
	
	private void fireProjectile(float xDir, float yDir)
	{
		ProjectileEntity e = new ProjectileEntity(getEntity().getLocation().x, getEntity().getLocation().y, 0.25f, 0.25f, getEntity().getTeam(), attackDamage, 3f);
		e.setVelocity(xDir*movementCap*4/3, yDir*movementCap*4/3);
		getEntity().getWorld().addEntity(e);
		EntityRenderer r = new EntityRenderer(e, projectileAnim);
		getEngine().getView().addEntityRenderer(r);
	}

	@Override
	public void onDeath(UnitEntity e) {
		Object o = table.getValue();
		if(o!=null)
		{
			if(o instanceof Weapon)
			{
				Weapon w = (Weapon) o;
				WeaponEntity we = new WeaponEntity(e.getLocation().x, e.getLocation().y, 1f, 1f, w);
				e.getWorld().addEntity(we);
				EntityRenderer re = new EntityRenderer(we, weaponRenderMap.get(w));
				getEngine().getView().addEntityRenderer(re);
			}
		}
	}

	@Override
	public void onJump(UnitEntity e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHit(UnitEntity e) {
		hit.play(0.5f);
	}

}
