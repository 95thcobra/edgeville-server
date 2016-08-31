package edgeville.game.character.newcombat;

import edgeville.game.character.Animation;
import edgeville.game.character.Entity;
import edgeville.game.character.Hit;
import edgeville.game.character.combat.Combat;
import edgeville.game.character.combat.CombatType;
import edgeville.game.character.combat.weapon.FightStyle;
import edgeville.game.character.combat.weapon.FightType;
import edgeville.game.character.newcombat.combatstrategies.AbstractCombatStrategy;
import edgeville.game.character.newcombat.combatstrategies.PlayerCombatStrategy;
import edgeville.game.character.newcombat.combatstrategies.player.MageCombatStrategy;
import edgeville.game.character.newcombat.combatstrategies.player.MeleeCombatStrategy;
import edgeville.game.character.newcombat.combatstrategies.player.RangedCombatStrategy;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.WeaponInterface;
import edgeville.game.character.timers.TimerKey;
import edgeville.game.item.Item;
import edgeville.game.item.container.Equipment;

/**
 * @author Simon on 30/8/2016
 */
public class PlayerVersusEntity extends AbstractCombat {

	private Player attacker;
	private Entity target;

	public PlayerVersusEntity(Player attacker, Entity target) {
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

	@Override
	public void initiate() {
		// TODO Auto-generated method stub

	}

	@Override
	public PlayerCombatStrategy determineStrategy() {		
		if (attacker.getCastSpell() != null || attacker.getAutocastSpell() != null) {
			return new MageCombatStrategy(attacker, target);
		}
		WeaponInterface weapon = attacker.getWeapon();
		if (weapon == WeaponInterface.SHORTBOW || weapon == WeaponInterface.LONGBOW || weapon == WeaponInterface.CROSSBOW || weapon == WeaponInterface.DART || weapon == WeaponInterface.JAVELIN || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.KNIFE) {
			return new RangedCombatStrategy(attacker, target);
		}
		return new MeleeCombatStrategy(attacker, target);
	}
}
