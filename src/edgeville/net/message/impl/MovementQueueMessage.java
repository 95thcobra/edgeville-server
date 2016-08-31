package edgeville.net.message.impl;

import edgeville.Server;
import edgeville.game.World;
import edgeville.game.character.player.Player;
import edgeville.game.location.Position;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The message sent from the client when a player makes a yellow {@code X}
 * click, a red {@code X} click, or when they click the minimap.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class MovementQueueMessage implements InputMessageListener {

	@Override
	public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
		if (player.isDisabled())
			return;

		player.stopActions();

		player.faceCharacter(null);

		if (opcode == 248) {
			player.setSkillAction(false);
			player.setFollowing(false);
			player.getCombatBuilder().cooldown(false);
			size -= 14;
		} else if (opcode == 164) {
			player.setSkillAction(false);
			player.setFollowing(false);
			player.getCombatBuilder().cooldown(false);
		} else if (opcode == 98) { // combat attack
			return;
		}

		if (player.isFrozen()) {
			player.getMessages().sendMessage("You are frozen and unable to " + "move!");
			return;
		}

		if (player.getDialogueChain() != null)
			player.getDialogueChain().interrupt();

		if (player.getTradeSession().inTradeSession()) {
			player.getTradeSession().reset(false);
		}

		player.getMessages().sendCloseWindows();
		player.setOpenShop(null);

		int steps = (size - 5) / 2;
		int[][] path = new int[steps][2];
		int firstStepX = payload.getShort(edgeville.net.ValueType.A, edgeville.net.ByteOrder.LITTLE);

		for (int i = 0; i < steps; i++) {
			path[i][0] = payload.get();
			path[i][1] = payload.get();
		}
		int firstStepY = payload.getShort(edgeville.net.ByteOrder.LITTLE);
		// player.getMovementQueue().reset();
		// player.getMovementQueue().setRunPath(payload.get(com.asteria.net.ValueType.C) == 1);
		// player.getMovementQueue().addToPath(new Position(firstStepX, firstStepY));

		int x = firstStepX;
		int y = firstStepY;
		for (int i = 0; i < steps; i++) {
			path[i][0] += firstStepX;
			path[i][1] += firstStepY;
			//player.getMovementQueue().addToPath(new Position(path[i][0], path[i][1]));
			x = path[i][0];
			y = path[i][1];
		}
		// player.getMovementQueue().finish();

		player.playerWalk(x, y);

		//if (Server.DEBUG)
		// player.getMessages().sendMessage("DEBUG[walking= " + player.getPosition().getRegion() + "]");
	}
}
