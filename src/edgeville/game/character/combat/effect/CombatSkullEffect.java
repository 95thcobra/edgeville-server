package edgeville.game.character.combat.effect;

import edgeville.game.NodeType;
import edgeville.game.character.Entity;
import edgeville.game.character.Flag;
import edgeville.game.character.player.Player;

/**
 * The combat effect applied when a player needs to be skulled.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CombatSkullEffect extends CombatEffect {

    /**
     * Creates a new {@link CombatSkullEffect}.
     */
    public CombatSkullEffect() {
        super(50);
    }

    @Override
    public boolean apply(Entity c) {
        if (c.getType() == NodeType.PLAYER) {
            Player player = (Player) c;
            if (player.getSkullTimer().get() > 0) {
                return false;
            }
            player.getSkullTimer().set(3000);
            player.setSkullIcon(Player.WHITE_SKULL);
            player.getFlags().set(Flag.APPEARANCE);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeOn(Entity c) {
        if (c.getType() == NodeType.PLAYER) {
            Player player = (Player) c;
            if (player.getSkullTimer().get() <= 0) {
                player.setSkullIcon(-1);
                player.getFlags().set(Flag.APPEARANCE);
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public void process(Entity c) {
        if (c.getType() == NodeType.PLAYER) {
            Player player = (Player) c;
            player.getSkullTimer().decrementAndGet(50, 0);
        }
    }

    @Override
    public boolean onLogin(Entity c) {
        if (c.getType() == NodeType.PLAYER) {
            Player player = (Player) c;

            if (player.getSkullTimer().get() > 0) {
                player.setSkullIcon(Player.WHITE_SKULL);
                return true;
            }
            //if (FightCavesHandler.isChampion(player))
                //player.setSkullIcon(Player.RED_SKULL);
        }
        return false;
    }
}
