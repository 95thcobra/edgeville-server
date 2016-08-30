package edgeville.game.character.newcombat;

import edgeville.game.character.Hit;
import edgeville.game.character.player.Player;
import edgeville.game.character.timers.TimerKey;

/**
 * @author Simon on 30/8/2016
 */
public class PvPCombat extends AbstractCombat {

	private Player attacker;
	private Player target;
	
	public PvPCombat(Player attacker, Player target) {
		super(attacker, target);
		this.attacker = attacker;
		this.target = target;
	}

	@Override
	public void cycle() {
		
		// Check whether the player is attacking too soon.
		if (attacker.timers().has(TimerKey.COMBAT_ATTACK)) {
			return;
		}
		attacker.timers().add(TimerKey.COMBAT_ATTACK, attacker.getAttackSpeed());
		
		// Do animation.
		attacker.animation(attacker.getFightType().getAnimation());
		target.animation(404);
		
		// Hit the target.
		target.damage(new Hit(1));
	}

	@Override
	public boolean canAttack() {
		// TODO
		return true;
	}

}
