package edgeville.net.message.impl;

import edgeville.game.World;
import edgeville.game.character.player.Player;
import edgeville.game.region.PathFinder;
import edgeville.net.ByteOrder;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The message sent from the client when a player tries to follow another player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class FollowPlayerMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        int index = payload.getShort(false, ByteOrder.LITTLE);
        Player follow = World.getPlayers().get(index);

        if (follow == null || !follow.getPosition().isViewableFrom(player.getPosition()) || follow.equals(player))
            return;
        player.setSkillAction(false);
       
        
        
        player.getMovementQueue().follow(follow);
      //  player.followPlayer(follow);
       //PathFinder.findRoute(player, follow);
    }
}
