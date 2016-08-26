package edgeville.net.message.impl;

import edgeville.actions.NpcFirstClickAction;
import edgeville.actions.NpcSecondClickAction;
import edgeville.game.World;
import edgeville.game.character.combat.magic.CombatSpell;
import edgeville.game.character.combat.magic.CombatSpells;
import edgeville.game.character.npc.Npc;
import edgeville.game.character.npc.NpcDefinition;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.minigame.MinigameHandler;
import edgeville.game.location.Location;
import edgeville.game.location.Position;
import edgeville.game.plugin.context.NpcFirstClickPlugin;
import edgeville.game.plugin.context.NpcSecondClickPlugin;
import edgeville.net.ByteOrder;
import edgeville.net.ValueType;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The message sent from the client when a player attacks or clicks on an NPC.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcActionMessage implements InputMessageListener {

	@Override
	public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
		if (player.isDisabled())
			return;
		switch (opcode) {
		case 72:
			attackOther(player, payload);
			break;
		case 131:
			attackMagic(player, payload);
			break;
		case 155:
			firstClick(player, payload);
			break;
		case 17:
			secondClick(player, payload);
			break;
		}
	}

	/**
	 * Handles the melee and ranged attacks on an NPC.
	 *
	 * @param player
	 *            the player this will be handled for.
	 * @param payload
	 *            the payloadfer that will read the sent data.
	 */
	private void attackOther(Player player, MessageBuilder payload) {
		int index = payload.getShort(false, ValueType.A);
		Npc npc = World.getNpcs().get(index);
		player.message("Npc id: %d", npc.getId());
		if (npc == null || !checkAttack(player, npc))
			return;
		player.getTolerance().reset();
		player.getCombatBuilder().attack(npc);
	}

	/**
	 * Handles the magic attacks on an NPC.
	 *
	 * @param player
	 *            the player this will be handled for.
	 * @param payload
	 *            the payloadfer that will read the sent data.
	 */
	private void attackMagic(Player player, MessageBuilder payload) {
		int index = payload.getShort(true, ValueType.A, ByteOrder.LITTLE);
		int spellId = payload.getShort(true, ValueType.A);
		Npc npc = World.getNpcs().get(index);
		CombatSpell spell = CombatSpells.getSpell(spellId).orElse(null).getSpell();
		if (npc == null || spell == null || !checkAttack(player, npc))
			return;
		player.setCastSpell(spell);
		player.getTolerance().reset();
		player.getCombatBuilder().attack(npc);
	}

	/**
	 * Handles the first click NPC slot.
	 *
	 * @param player
	 *            the player this will be handled for.
	 * @param payload
	 *            the payloadfer that will read the sent data.
	 */
	private void firstClick(Player player, MessageBuilder payload) {
		int index = payload.getShort(true, ByteOrder.LITTLE);
		Npc npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		Position position = npc.getPosition().copy();
		if (player.isDebugEnabled())
			player.message("Npc first click - id: %d", npc.getId());
		player.getMovementListener().append(() -> {
			if (player.getPosition().withinDistance(position, 1)) {
				player.facePosition(npc.getPosition());
				npc.facePosition(player.getPosition());
				MinigameHandler.execute(player, m -> m.onFirstClickNpc(player, npc));
				//World.getPlugins().execute(player, NpcFirstClickPlugin.class, new NpcFirstClickPlugin(npc));
				new NpcFirstClickAction().handle(player, npc, position);
			}
		});
	}

	/**
	 * Handles the second click NPC slot.
	 *
	 * @param player
	 *            the player this will be handled for.
	 * @param payload
	 *            the payloadfer that will read the sent data.
	 */
	private void secondClick(Player player, MessageBuilder payload) {
		int index = payload.getShort(false, ValueType.A, ByteOrder.LITTLE);
		Npc npc = World.getNpcs().get(index);
		if (npc == null)
			return;
		Position position = npc.getPosition().copy();
		if (player.isDebugEnabled())
			player.message("Npc second click - id: %d", npc.getId());
		player.getMovementListener().append(() -> {
			if (player.getPosition().withinDistance(position, 1)) {
				player.facePosition(npc.getPosition());
				npc.facePosition(player.getPosition());
				MinigameHandler.execute(player, m -> m.onSecondClickNpc(player, npc));
				//World.getPlugins().execute(player, NpcSecondClickPlugin.class, new NpcSecondClickPlugin(npc));
				new NpcSecondClickAction().handle(player, npc, position);
			}
		});
	}

	/**
	 * Determines if {@code player} can make an attack on {@code npc}.
	 *
	 * @param player
	 *            the player attempting to make an attack.
	 * @param npc
	 *            the npc being attacked.
	 * @return {@code true} if the player can make an attack, {@code false}
	 *         otherwise.
	 */
	private boolean checkAttack(Player player, Npc npc) {
		if (!NpcDefinition.DEFINITIONS[npc.getId()].isAttackable())
			return false;
		if (!MinigameHandler.execute(player, true, m -> m.canHit(player, npc)))
			return false;
		if (!Location.inMultiCombat(player) && player.getCombatBuilder().isBeingAttacked() && !npc.equals(player.getCombatBuilder().getLastAttacker())) {
			player.getMessages().sendMessage("You are already under attack!");
			return false;
		}
		return true;
	}
}
