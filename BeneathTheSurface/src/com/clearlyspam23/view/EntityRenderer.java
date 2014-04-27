package com.clearlyspam23.view;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.clearlyspam23.game.Entity;

public class EntityRenderer {
	
	private float stateTime;
	private RenderStates currentState = RenderStates.idle;
	private Map<RenderStates, Animation> renderMap;
	private Entity e;
	
	public EntityRenderer(Entity e, Map<RenderStates, Animation> renderMap)
	{
		this.renderMap = renderMap;
		this.e = e;
	}
	
	public void render(SpriteBatch batch)
	{
		TextureRegion frame = getAnimation(currentState).getKeyFrame(stateTime, true);
		batch.draw(frame.getTexture(), e.getLocation().x-e.getBounds().x/2, e.getLocation().y-e.getBounds().y/2, e.getBounds().x, e.getBounds().y, 
				frame.getRegionX(), frame.getRegionY(), frame.getRegionWidth(), frame.getRegionHeight(), e.getCurrentFacing()==Entity.Facing.left, false);
	}
	
	private Animation getAnimation(RenderStates state)
	{
		if(!renderMap.containsKey(state))
			return renderMap.get(RenderStates.idle);
		return renderMap.get(state);
	}

	public RenderStates getCurrentState() {
		return currentState;
	}

	public void setCurrentState(RenderStates currentState) {
		this.currentState = currentState;
	}

	public float getStateTime() {
		return stateTime;
	}
	
	public void incrementStateTime(float delta)
	{
		stateTime+=delta;
	}

	public void setStateTime(float stateTime) {
		this.stateTime = stateTime;
	}
	
	public Entity getEntity()
	{
		return e;
	}

}
