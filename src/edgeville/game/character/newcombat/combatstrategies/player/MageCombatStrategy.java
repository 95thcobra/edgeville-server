package edgeville.game.character.newcombat.combatstrategies.player;

import edgeville.game.character.Entity;
import edgeville.game.character.combat.Combat;
import edgeville.game.character.combat.weapon.FightStyle;
import edgeville.game.character.newcombat.combatstrategies.PlayerCombatStrategy;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.WeaponInterface;

public class MageCombatStrategy extends PlayerCombatStrategy {
	
	private Player attacker;
	private Entity target;
	
	public MageCombatStrategy(Player attacker, Entity target) {
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
		return 7;
	}
	
}
