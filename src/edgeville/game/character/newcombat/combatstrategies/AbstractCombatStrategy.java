package edgeville.game.character.newcombat.combatstrategies;

import edgeville.game.character.Entity;
import edgeville.game.character.player.Player;

public abstract class AbstractCombatStrategy {
	
	private Entity attacker;
	public Entity getAttacker() {
		return attacker;
	}

	public Entity getTarget() {
		return target;
	}
	private Entity target;
	
	public AbstractCombatStrategy(Entity attacker, Entity target) {
		this.attacker = attacker;
		this.target = target;
	}
	
	public abstract void attack();
	public abstract int attackDistance();
	public abstract int calculateHit();
}
