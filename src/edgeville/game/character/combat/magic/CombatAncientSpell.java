package edgeville.game.character.combat.magic;

import java.util.Optional;
import java.util.function.Consumer;

import edgeville.game.NodeType;
import edgeville.game.character.Entity;
import edgeville.game.character.combat.Combat;
import edgeville.game.character.combat.CombatType;
import edgeville.game.character.npc.Npc;
import edgeville.game.character.player.Player;
import edgeville.game.item.Item;
import edgeville.game.location.Location;

/**
 * The {@link CombatSpell} extension with support for effects and the ability to
 * multicast characters within a certain radius.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class CombatAncientSpell extends CombatSpell {

    @Override
    public final void executeOnHit(Entity cast, Entity castOn, boolean accurate, int damage) {
        if (accurate) {
            effect(cast, castOn, damage);
            if (radius() == 0 || !Location.inMultiCombat(castOn))
                return;
            if (castOn.getType() == NodeType.PLAYER) {
                Combat.damagePlayersWithin(cast, castOn.getPosition(), radius(), 1, CombatType.MAGIC, false, new Consumer<Player>() {
                    @Override
                    public void accept(Player t) {
                        cast.getCurrentlyCasting().endGraphic().ifPresent(t::graphic);
                        effect(cast, castOn, damage);
                    }
                });
            } else {
                Combat.damageNpcsWithin(cast, castOn.getPosition(), radius(), 1, CombatType.MAGIC, false, new Consumer<Npc>() {
                    @Override
                    public void accept(Npc t) {
                        cast.getCurrentlyCasting().endGraphic().ifPresent(t::graphic);
                        effect(cast, castOn, damage);
                    }
                });
            }
        }
    }

    @Override
    public final Optional<Item[]> equipmentRequired(Player player) {
        return Optional.empty();
    }

    /**
     * Executed when the spell casted by {@code cast} hits {@code castOn}.
     *
     * @param cast
     *            the character who casted the spell.
     * @param castOn
     *            the character who the spell was casted on.
     * @param damage
     *            the damage that was inflicted by the spell.
     */
    public abstract void effect(Entity cast, Entity castOn, int damage);

    /**
     * The radius of this spell for multicast support.
     *
     * @return the radius of this spell.
     */
    public abstract int radius();
}
