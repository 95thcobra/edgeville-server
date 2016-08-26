package edgeville.net.message.impl;

import edgeville.Server;
import edgeville.actions.ObjectFirstClickAction;
import edgeville.actions.ObjectSecondClickAction;
import edgeville.game.World;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.minigame.MinigameHandler;
import edgeville.game.location.Position;
import edgeville.game.plugin.context.ObjectFirstClickPlugin;
import edgeville.game.plugin.context.ObjectSecondClickPlugin;
import edgeville.net.ByteOrder;
import edgeville.net.ValueType;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;
import edgeville.task.impl.WalkActionTask;

/**
 * The message sent from the client when a player clicks an object.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectActionMessage implements InputMessageListener {

	// TODO: When cache reading is done, check position of objects.

	@Override
	public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
		if (player.isDisabled())
			return;

		switch (opcode) {
		case 132:
			firstClick(player, payload);
			break;
		case 252:
			secondClick(player, payload);
			break;
		case 70:
			thirdClick(player, payload);
			break;
		}
	}

	/**
	 * Handles the first slot object click for {@code player}.
	 *
	 * @param player
	 *            the player to handle this for.
	 * @param payload
	 *            the payloadfer for reading the sent data.
	 */
	private void firstClick(Player player, MessageBuilder payload) {
		int objectX = payload.getShort(true, ValueType.A, ByteOrder.LITTLE);
		int objectId = payload.getShort(false);
		int objectY = payload.getShort(false, ValueType.A);
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		int size = 1;
		if (objectId < 0 || objectX < 0 || objectY < 0)
			return;
		//if (Server.DEBUG)
		if (player.isDebugEnabled())
			player.message("Object first click - id:%d, x:%d, y:%d", objectId, objectX, objectY);
		player.facePosition(position);

/*World.submit(new WalkActionTask(player, position) {

	@Override
	public void doAction() {
		new ObjectFirstClickAction().handle(player, objectId, position);
	}
	
});*/
		
		player.getMovementListener().append(() -> {
			
			if (player.getPosition().withinDistance(position, /*size*/3)) { // this dont work, idk why
		       // if (Math.abs(position.getX() - player.getPosition().getX()) <= 3 && Math.abs(position.getY() - player.getPosition().getY()) <= 3){
				//MinigameHandler.execute(player, m -> m.onFirstClickObject(player, objectId, position.copy()));
				//World.getPlugins().execute(player, ObjectFirstClickPlugin.class, new ObjectFirstClickPlugin(objectId, position, size));
				new ObjectFirstClickAction().handle(player, objectId, position);
			}
		});
	}

	/**
	 * Handles the second slot object click for {@code player}.
	 *
	 * @param player
	 *            the player to handle this for.
	 * @param payload
	 *            the payloadfer for reading the sent data.
	 */
	private void secondClick(Player player, MessageBuilder payload) {
		int objectId = payload.getShort(false, ValueType.A, ByteOrder.LITTLE);
		int objectY = payload.getShort(true, ByteOrder.LITTLE);
		int objectX = payload.getShort(false, ValueType.A);
		int size = 1;
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if (objectId < 0 || objectX < 0 || objectY < 0)
			return;
		//if (Server.DEBUG)
		//	player.getMessages().sendMessage("[DEBUG]: ID - " + objectId + ", " + "X - " + objectX + ", Y - " + objectY);
		if (player.isDebugEnabled())
			player.message("Object second click - id:%d, x:%d, y:%d", objectId, objectX, objectY);
		player.facePosition(position);
		
		
		player.getMovementListener().append(() -> {
			if (player.getPosition().withinDistance(position, /*size*/3)) {
			//if (Math.abs(position.getX() - player.getPosition().getX()) <= 3 && Math.abs(position.getY() - player.getPosition().getY()) <= 3){
					MinigameHandler.execute(player, m -> m.onSecondClickObject(player, objectId, position.copy()));
				//World.getPlugins().execute(player, ObjectSecondClickPlugin.class, new ObjectSecondClickPlugin(objectId, position, size));
				new ObjectSecondClickAction().handle(player, objectId, position);
			}
		});
		
		
		/*World.submit(new WalkActionTask(player, position) {

			@Override
			public void doAction() {
				new ObjectSecondClickAction().handle(player, objectId, position);
			}
			
		});*/
	}

	/**
	 * Handles the third slot object click for {@code player}.
	 *
	 * @param player
	 *            the player to handle this for.
	 * @param payload
	 *            the payloadfer for reading the sent data.
	 */
	private void thirdClick(Player player, MessageBuilder payload) {
		int objectX = payload.getShort(true, ByteOrder.LITTLE);
		int objectY = payload.getShort(false);
		int objectId = payload.getShort(false, ValueType.A, ByteOrder.LITTLE);
		int size = 1;
		Position position = new Position(objectX, objectY, player.getPosition().getZ());
		if (objectId < 0 || objectX < 0 || objectY < 0)
			return;
		if (player.isDebugEnabled())
			player.message("Object third click - id:%d, x:%d, y:%d", objectId, objectX, objectY);
		player.facePosition(position);
		player.getMovementListener().append(new Runnable() {
			@Override
			public void run() {
				if (player.getPosition().withinDistance(position, /*size*/3)) {
					MinigameHandler.execute(player, m -> m.onThirdClickObject(player, objectId, position.copy()));
					switch (objectId) {

					}
				}
			}
		});
		/*World.submit(new WalkActionTask(player, position) {

			@Override
			public void doAction() {
				//new ObjectFirstClickAction().handle(player, objectId, position);
			}
			
		});*/
	}

}
