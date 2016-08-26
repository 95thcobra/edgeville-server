package edgeville.game.character.player.content;

import edgeville.game.World;
import edgeville.game.character.combat.prayer.CombatPrayer;
import edgeville.game.character.combat.weapon.CombatSpecial;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.Rights;
import edgeville.game.character.player.skill.Skills;
import edgeville.task.Task;

/**
 * The class that handles the restoration of weakened skills.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class RestoreStatTask extends Task {

    /**
     * Creates a new {@link RestoreStatTask}.
     */
    public RestoreStatTask() {
        super(30, false);
    }

    @Override
    public void execute() {
        for (Player player : World.getPlayers()) {
            if (player == null) {
                continue;
            }

            for (int i = 0; i < player.getSkills().length; i++) {
                int realLevel = player.getSkills()[i].getRealLevel();
                if (i == Skills.HITPOINTS) {
                    if (player.getSkills()[i].getLevel() < realLevel && player.isAcceptAid()) {
                        player.getSkills()[i].increaseLevel(1);
                        if (CombatPrayer.isActivated(player, CombatPrayer.RAPID_HEAL)) {
                            if (player.getSkills()[i].getLevel() < realLevel) {
                                player.getSkills()[i].increaseLevel(1);
                            }
                        }
                        Skills.refresh(player, Skills.HITPOINTS);
                    }
                    continue;
                }
                if (player.getSkills()[i].getLevel() < realLevel && i != Skills.PRAYER) {
                    player.getSkills()[i].increaseLevel(1);

                    if (CombatPrayer.isActivated(player, CombatPrayer.RAPID_RESTORE)) {
                        if (player.getSkills()[i].getLevel() < realLevel) {
                            player.getSkills()[i].increaseLevel(1);
                        }
                    }
                    Skills.refresh(player, i);
                } else if (player.getSkills()[i].getLevel() > realLevel && i != Skills.PRAYER) {
                    player.getSkills()[i].decreaseLevel(1);
                    Skills.refresh(player, i);
                }
            }
            if (player.getSpecialPercentage().get() < 100) {
                if (player.getRights().equal(Rights.DEVELOPER)) {
                    CombatSpecial.restore(player, 100);
                    return;
                }
                CombatSpecial.restore(player, 5);
            }
        }
    }

    @Override
    public void onCancel() {
        World.submit(new RestoreStatTask());
    }
}
