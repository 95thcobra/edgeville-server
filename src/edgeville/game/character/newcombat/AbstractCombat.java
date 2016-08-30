package edgeville.game.character.newcombat;

import edgeville.game.World;
import edgeville.game.character.Entity;
import edgeville.task.Task;

/**
 * @author Simon
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
		combatTask = new Task(0, true) {

			@Override
			public void execute() {
				cycle();
			}

		};

		World.submit(combatTask);
	}

	public void stop() {
		combatTask.cancel();
	}

	public abstract void cycle();

	public Entity getAttacker() {
		return attacker;
	}

	public Entity getTarget() {
		return target;
	}

}
