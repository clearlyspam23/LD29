package com.clearlyspam23.logic;

import java.util.Map;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.clearlyspam23.game.GroundUnitEntity;
import com.clearlyspam23.game.PlayerEntity;
import com.clearlyspam23.game.ProjectileEntity;
import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.Weapon;
import com.clearlyspam23.game.WeaponEntity;
import com.clearlyspam23.view.EntityRenderer;
import com.clearlyspam23.view.RenderStates;

public class BasicGroundFollower extends UnitController<GroundUnitEntity>{
	
	private float attackDelay;
	private float maxAttackDelay;
	private int attackDamage;
	private float movementSpeed;
	private float jumpHeight;
	private DropTable table;
	private Map<Weapon, Map<RenderStates, Animation>> weaponRenderMap;
	private Sound jumpSound;
	private Sound hitSound;

	public BasicGroundFollower(GroundUnitEntity t, float attackDelay, int attackDamage, float movementSpeed, float jumpHeight, EntityRenderer renderer, DropTable table, Map<Weapon, Map<RenderStates, Animation>> weaponRenderMap, Sound jump, Sound hit) {
		super(t, renderer);
		this.maxAttackDelay = attackDelay;
		this.attackDamage = attackDamage;
		this.movementSpeed = movementSpeed;
		this.jumpHeight = jumpHeight;
		this.table = table;
		this.weaponRenderMap = weaponRenderMap;
		this.jumpSound = jump;
		this.hitSound = hit;
	}

	@Override
	public void control(float delta, float mouseX, float mouseY) {
		GroundUnitEntity e = getEntity();
		PlayerEntity player = getEntity().getWorld().getPlayer();
		if(attackDelay>0)
		{
			attackDelay-=delta;
			if(attackDelay<=0)
				attack(e);
			return;
		}
		if(player.getRectangle().overlaps(e.getRectangle()))
		{
			attackDelay = maxAttackDelay;
			getRenderer().setCurrentState(RenderStates.shooting);
			getRenderer().setStateTime(0);
		}
		else
		{
			if(player.getLocation().x < e.getLocation().x)
			{
				//move right
				e.unit_move(-movementSpeed, movementSpeed);
				getRenderer().setCurrentState(RenderStates.moving);
			}
			else
			{
				//move left
				e.unit_move(movementSpeed, movementSpeed);
				getRenderer().setCurrentState(RenderStates.moving);
			}
			if(player.getLocation().y - player.getBounds().y/2> e.getLocation().y)
			{
				e.jump(jumpHeight);
				getRenderer().setCurrentState(RenderStates.air);
			}
		}
	}
	
	public void attack(GroundUnitEntity e)
	{
		ProjectileEntity p = new ProjectileEntity(e.getLocation().x, e.getLocation().y, e.getBounds().x, e.getBounds().y, e.getTeam(), attackDamage, 0.25f);
		e.getWorld().addEntity(p);
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
		jumpSound.play(0.5f);
	}

	@Override
	public void onHit(UnitEntity e) {
		hitSound.play(0.5f);
	}

}
