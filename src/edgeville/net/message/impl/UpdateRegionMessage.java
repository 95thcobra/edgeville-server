package edgeville.net.message.impl;

import edgeville.Server;
import edgeville.game.character.player.Player;
import edgeville.game.item.ItemNodeManager;
import edgeville.game.object.ObjectNodeManager;
import edgeville.game.region.Region;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The message sent from the client when a player sends the load map region
 * message.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class UpdateRegionMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
    	Region.load(player);
    	
        if (player.isUpdateRegion()) {
            ObjectNodeManager.updateRegion(player);
            ItemNodeManager.updateRegion(player);
            player.sendInterfaces();
            player.getTolerance().reset();
            player.setUpdateRegion(false);

           // if (Server.DEBUG)
               // player.getMessages().sendMessage("DEBUG[region= " + player.getPosition().getRegion() + "]");
        }
    }
}
