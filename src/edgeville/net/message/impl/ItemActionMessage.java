package edgeville.net.message.impl;

import edgeville.game.World;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.FoodConsumable;
import edgeville.game.character.player.content.PotionConsumable;
import edgeville.game.item.Item;
import edgeville.game.item.ItemDefinition;
import edgeville.game.plugin.context.ItemFirstClickPlugin;
import edgeville.net.ByteOrder;
import edgeville.net.ValueType;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The message sent from the client when the player clicks an item.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemActionMessage implements InputMessageListener {

    @Override
    public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
        if (player.isDisabled())
            return;

        switch (opcode) {
        case 122:
            firstClick(player, payload);
            break;
        }
    }

    /**
     * Handles the first slot of an item action.
     *
     * @param player
     *            the player to handle this for.
     * @param payload
     *            the payloadfer for reading the sent data.
     */
    private void firstClick(Player player, MessageBuilder payload) {
        int container = payload.getShort(true, ValueType.A, ByteOrder.LITTLE);
        int slot = payload.getShort(false, ValueType.A);
        int id = payload.getShort(false, ByteOrder.LITTLE);

        if (slot < 0 || container < 0 || id < 0 || id > ItemDefinition.DEFINITIONS.length)
            return;

        player.setSkillAction(false);
        player.getCombatBuilder().cooldown(true);

        if (container == 3214) {
            Item item = player.getInventory().get(slot);

            if (item == null || item.getId() != id) {
                return;
            }
            if (FoodConsumable.consume(player, item, slot)) {
                return;
            }
            if (PotionConsumable.consume(player, item, slot)) {
                return;
            }
            /*Bone bone = Bone.getBone(id);
            if (bone != null) {
                PrayerBoneBury buryAction = new PrayerBoneBury(player, bone);
                buryAction.start();
                return;
            }*/
            World.getPlugins().execute(player, ItemFirstClickPlugin.class, new ItemFirstClickPlugin(slot, item));
        }
    }
}
