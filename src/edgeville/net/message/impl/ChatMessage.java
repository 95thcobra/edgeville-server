package edgeville.net.message.impl;

import edgeville.game.character.Flag;
import edgeville.game.character.player.Player;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The message sent from the client when the player speaks.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ChatMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        int effects = payload.get(false, edgeville.net.ValueType.S);
        int color = payload.get(false, edgeville.net.ValueType.S);
        int chatLength = (size - 2);
        byte[] text = payload.getBytesReverse(chatLength, edgeville.net.ValueType.A);
        if (effects < 0 || color < 0 || chatLength < 0)
            return;
        player.setChatEffects(effects);
        player.setChatColor(color);
        player.setChatText(text);
        player.getFlags().set(Flag.CHAT);
    }
}
