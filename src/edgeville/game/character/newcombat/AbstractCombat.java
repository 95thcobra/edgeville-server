package edgeville.game.character.newcombat;

import edgeville.game.World;
import edgeville.game.character.Entity;
import edgeville.game.character.Hit;
import edgeville.game.character.newcombat.combatstrategies.AbstractCombatStrategy;
import edgeville.game.character.newcombat.formulas.AccuracyFormula;
import edgeville.game.character.newcombat.formulas.MaxHitFormulas;
import edgeville.game.character.player.Player;
import edgeville.game.character.timers.TimerKey;
import edgeville.game.location.Position;
import edgeville.game.region.Region;
import edgeville.task.Task;

/**
 * @author Simon on 30/8/2016
 */
public abstract class AbstractCombat {

	private Entity attacker;
	private Entity target;
	private Task combatTask;
	private AbstractCombatStrategy combatStrategy;

	public AbstractCombat(Entity attacker, Entity target) {
		this.attacker = attacker;
		this.target = target;
	}

	public void start() {
		// determine strategy
		combatStrategy = determineStrategy();

		attacker.setCombat(this);

		combatTask = new Task(0, true) {

			@Override
			public void execute() {

				// Face target.
				attacker.faceCharacter(target);

				// Check if player can attack.
				if (!canAttack()) {
					return;
				}
				
				// You can't attack if you're on same position as target
				if (attacker.getPosition().equals(target.getPosition())) {
					return;
				}
				
				// Follow player
				if (!attacker.getPosition().withinDistance(target.getPosition(), combatStrategy.attackDistance())) {
					attacker.combatStepTowardsEntity(target, combatStrategy.attackDistance());
				} else {
					attacker.getMovementQueue().reset();
				}

				// Check whether the player is attacking too soon.
				if (attacker.timers().has(TimerKey.COMBAT_ATTACK)) {
					return;
				}
				attacker.timers().add(TimerKey.COMBAT_ATTACK, attacker.getAttackSpeed());

				// Do animation
				attacker.animation(attackAnimation());
				target.animation(defendAnimation());

				// Calculate hit and damage target.
				int maxHit = MaxHitFormulas.maximumMeleeHit((Player)attacker);
				int hit = AccuracyFormula.calculateMeleeHit((Player)attacker, (Player)target, maxHit);
				target.damage(new Hit(hit));

				cycle();
				combatStrategy.attack();
			}

		};

		World.submit(combatTask);
	}

	public void stop() {
		attacker.setCombat(null);
		combatTask.cancel();
	}

	public abstract void cycle();

	public abstract boolean canAttack();

	public abstract int attackAnimation();

	public abstract int defendAnimation();

	public abstract void initiate();

	public abstract AbstractCombatStrategy determineStrategy();

	public Entity getAttacker() {
		return attacker;
	}

	public Entity getTarget() {
		return target;
	}

}
