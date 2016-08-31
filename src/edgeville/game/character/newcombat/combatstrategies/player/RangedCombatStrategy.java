package edgeville.game.character.newcombat.combatstrategies.player;

import edgeville.game.character.Entity;
import edgeville.game.character.combat.Combat;
import edgeville.game.character.combat.weapon.FightStyle;
import edgeville.game.character.newcombat.combatstrategies.PlayerCombatStrategy;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.WeaponInterface;

public class RangedCombatStrategy extends PlayerCombatStrategy {
	
	private Player attacker;
	private Entity target;
	
	public RangedCombatStrategy(Player attacker, Entity target) {
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
		return Combat.getRangedDistance(attacker.getWeapon()) + (attacker.getFightType().getStyle() == FightStyle.DEFENSIVE ? 2 : 0);
	}
	
}
