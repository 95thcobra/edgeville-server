package edgeville.game.character.newcombat;

import edgeville.game.World;
import edgeville.game.character.Entity;
import edgeville.game.character.Hit;
import edgeville.game.character.timers.TimerKey;
import edgeville.task.Task;

/**
 * @author Simon on 30/8/2016
 */
public abstract class AbstractCombat {

	private Entity attacker;
	private Entity target;
	private Task combatTask;

	public AbstractCombat(Entity attacker, Entity target) {
		this.attacker = attacker;
		this.target = target;
	}

	public void start() {
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
				
				// Check if player is within distance. TODO: This should be checked in strategies.
				if (!attacker.getPosition().withinDistance(target.getPosition(), 2)) {
					return;
				}
				
				// Check whether the player is attacking too soon.
				if (attacker.timers().has(TimerKey.COMBAT_ATTACK)) {
					return;
				}
				attacker.timers().add(TimerKey.COMBAT_ATTACK, attacker.getAttackSpeed());
				
				// Do animation
				attacker.animation(attackAnimation());
				target.animation(defendAnimation());
				
				// Hit the target.
				target.damage(new Hit(1));
				
				cycle();
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
	
	public Entity getAttacker() {
		return attacker;
	}

	public Entity getTarget() {
		return target;
	}

}
