package edgeville.game.character.combat.strategy;

import edgeville.game.NodeType;
import edgeville.game.character.Animation;
import edgeville.game.character.AnimationPriority;
import edgeville.game.character.Entity;
import edgeville.game.character.combat.Combat;
import edgeville.game.character.combat.CombatSessionData;
import edgeville.game.character.combat.CombatStrategy;
import edgeville.game.character.combat.CombatType;
import edgeville.game.character.combat.magic.CombatSpell;
import edgeville.game.character.combat.weapon.FightType;
import edgeville.game.character.npc.Npc;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.WeaponInterface;
import edgeville.game.item.Item;
import edgeville.game.item.container.Equipment;

public class DefaultMagicCombatStrategy implements CombatStrategy {

	@Override
	public boolean canAttack(Entity character, Entity victim) {
		if (character.getType() == NodeType.NPC) {
			return true;
		}
		Player player = (Player) character;
		return getSpell(player).canCast(player);
	}

	private CombatSpell getSpell(Player player) {
		if (player.isAutocast() && player.getCastSpell() != null && player.getAutocastSpell() != null || !player.isAutocast() && player.getAutocastSpell() == null) {
			return player.getCastSpell();
		}
		return player.getAutocastSpell();
	}

	@Override
	public CombatSessionData attack(Entity character, Entity victim) {
		if (character.getType() == NodeType.PLAYER) {
			Player player = (Player) character;
			player.prepareSpell(getSpell(player), victim);
		} else if (character.getType() == NodeType.NPC) {
			Npc npc = (Npc) character;
			npc.prepareSpell(Combat.prepareSpellCast(npc).getSpell(), victim);
		}

		if (character.getCurrentlyCasting().maximumHit() == -1) {
			return new CombatSessionData(character, victim, CombatType.MAGIC, true);
		}
		return new CombatSessionData(character, victim, 1, CombatType.MAGIC, true);
	}

	@Override
	public int attackDelay(Entity character) {
		return 10;
	}

	@Override
	public int attackDistance(Entity character) {
		return 8;
	}

	@Override
	public int[] getNpcs() {
		return new int[] { 13, 172, 174 };
	}
}
