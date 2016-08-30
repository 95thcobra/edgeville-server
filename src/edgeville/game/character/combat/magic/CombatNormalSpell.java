package edgeville.game.character.combat.magic;

import edgeville.game.character.Entity;

/**
 * The {@link CombatSpell} extension with support for normal spells that have no
 * effects whatsoever.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatNormalSpell extends CombatSpell {

    @Override
    public final void executeOnHit(Entity cast, Entity castOn, boolean accurate, int damage) {

    }
}