package edgeville.game.character.newcombat;

import edgeville.game.character.Entity;
import edgeville.game.character.newcombat.combatstrategies.AbstractCombatStrategy;
import edgeville.game.character.newcombat.combatstrategies.npc.NpcCombatStrategy;
import edgeville.game.character.npc.Npc;

public class NpcVersusEntity extends AbstractCombat {

	private Npc attacker;
	private Entity target;
	
	public NpcVersusEntity(Npc attacker, Entity target) {
		super(attacker, target);
		this.attacker = attacker;
		this.target = target;
	}

	@Override
	public void cycle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canAttack() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int attackAnimation() {
		return attacker.getDefinition().getAttackAnimation();
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
	public NpcCombatStrategy determineStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

}
