package edgeville.game.character.newcombat.combatstrategies.player;

import edgeville.game.character.Entity;
import edgeville.game.character.newcombat.combatstrategies.PlayerCombatStrategy;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.WeaponInterface;

public class MeleeCombatStrategy extends PlayerCombatStrategy {

	private Player attacker;
	private Entity target;

	public MeleeCombatStrategy(Player attacker, Entity target) {
		super(attacker, target);
		this.attacker = attacker;
		this.target = target;
	}

	@Override
	public void attack() {
		// TODO Auto-generated method stub

	}

	@Override
	public int attackDistance() {
		if (attacker.getWeapon() == WeaponInterface.HALBERD) {
			return 2;
		}
		return 1;
	}

}
