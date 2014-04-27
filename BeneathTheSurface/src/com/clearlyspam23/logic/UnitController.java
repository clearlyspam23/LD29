package com.clearlyspam23.logic;

import com.clearlyspam23.game.UnitEntity;
import com.clearlyspam23.game.UnitEventListener;
import com.clearlyspam23.view.EntityRenderer;

public abstract class UnitController<T extends UnitEntity> extends Controller<T> implements UnitEventListener{

	public UnitController(T t, EntityRenderer renderer) {
		super(t, renderer);
		t.addListener(this);
	}

}
