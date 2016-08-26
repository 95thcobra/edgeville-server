package edgeville.net.message.impl;

import edgeville.game.character.player.Player;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The decoder used to handle useless messages sent from the client.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class DefaultMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {

    }
}
