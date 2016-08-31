package edgeville.game.character.newcombat.combatstrategies;

import edgeville.game.character.Entity;
import edgeville.game.character.player.Player;

public abstract class PlayerCombatStrategy extends AbstractCombatStrategy {

	public PlayerCombatStrategy(Player attacker, Entity target) {
		super(attacker, target);
	}
	
}
