package edgeville.net.message.impl;

import edgeville.game.World;
import edgeville.game.character.player.Player;
import edgeville.game.item.Item;
import edgeville.game.location.Position;
import edgeville.game.plugin.context.ItemOnObjectPlugin;
import edgeville.net.ByteOrder;
import edgeville.net.ValueType;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The message sent from the client when a player uses an item on an object.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemOnObjectMessage implements InputMessageListener {

    // TODO: When cache reading is done, check position of objects.

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        int container = payload.getShort(false);
        int objectId = payload.getShort(true, ByteOrder.LITTLE);
        int objectY = payload.getShort(true, ValueType.A, ByteOrder.LITTLE);
        int slot = payload.getShort(true, ByteOrder.LITTLE);
        int objectX = payload.getShort(true, ValueType.A, ByteOrder.LITTLE);
        int itemId = payload.getShort(false);
        int objectSize = 1;
        Item item = player.getInventory().get(slot);
        Position position = new Position(objectX, objectY, player.getPosition().getZ());
        if (item == null || container != 3214 || objectId < 0 || objectY < 0 || slot < 0 || objectX < 0 || itemId < 0)
            return;
        if (item.getId() != itemId)
            return;
        player.facePosition(position);
        player.getMovementListener().append(
            () -> {
                if (player.getPosition().withinDistance(position, objectSize)) {
                   /* Bone bone = Bone.getBone(itemId);
                    if (bone != null) {
                        PrayerBoneAltar altarAction = new PrayerBoneAltar(player, bone, new Position(objectX, objectY));
                        altarAction.start();
                        return;
                    }*/
                    World.getPlugins().execute(player, ItemOnObjectPlugin.class, new ItemOnObjectPlugin(objectId, position, objectSize, item,
                        slot));
                }
            });
    }
}
