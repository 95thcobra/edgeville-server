package edgeville.game.character.player.content;

import java.util.Optional;

import edgeville.game.character.CharacterNode;
import edgeville.game.character.Spell;
import edgeville.game.character.player.Player;
import edgeville.game.item.Item;
import edgeville.game.location.Position;

/**
 * The spell implementation that provides additional functions exclusively for
 * teleportation spells.
 *
 * @author lare96 <http://github.com/lare96>
 */
public abstract class TeleportSpell extends Spell {

    @Override
    public final Optional<Item[]> equipmentRequired(Player player) {
        return Optional.empty();
    }

    @Override
    public final void startCast(CharacterNode cast, CharacterNode castOn) {

    }

    /**
     * The position that the caster will be moved to.
     *
     * @return the teleport position.
     */
    public abstract Position moveTo();

    /**
     * The teleportation method that this spell uses.
     *
     * @return the teleportation type.
     */
    public abstract Spellbook type();
}