package edgeville.net.message.impl;

import edgeville.game.World;
import edgeville.game.character.player.Player;
import edgeville.game.item.Item;
import edgeville.game.plugin.context.ItemOnPlayerPlugin;
import edgeville.net.ByteOrder;
import edgeville.net.ValueType;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The message sent from the client when a player uses an item on another player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnPlayerMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        int container = payload.getShort(ValueType.A, ByteOrder.BIG);
        int index = payload.getShort();
        int itemUsed = payload.getShort();
        int itemSlot = payload.getShort(false, ValueType.A, ByteOrder.LITTLE);
        Item item = player.getInventory().get(itemSlot);
        Player usedOn = World.getPlayers().get(index);

        if (container < 0 || item == null || usedOn == null || itemUsed < 0)
            return;
        if (item.getId() != itemUsed)
            return;

        player.getMovementListener().append(() -> {
            if (player.getPosition().withinDistance(usedOn.getPosition(), 1)) {
                World.getPlugins().execute(player, ItemOnPlayerPlugin.class, new ItemOnPlayerPlugin(player, item));
            }
        });
    }
}
