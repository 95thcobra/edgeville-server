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
		
	}

	@Override
	public boolean canAttack() {
		// TODO
		return true;
	}

	@Override
	public int attackAnimation() {
		return attacker.getFightType().getAnimation();
	}

	@Override
	public int defendAnimation() {
		return 404;
	}
}
