package edgeville.net.message.impl;

import java.util.Arrays;
import java.util.stream.Collectors;

import edgeville.game.World;
import edgeville.game.character.Animation;
import edgeville.game.character.Flag;
import edgeville.game.character.Graphic;
import edgeville.game.character.npc.Npc;
import edgeville.game.character.npc.drop.NpcDropManager;
import edgeville.game.character.npc.drop.NpcDropTable;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.skill.Skill;
import edgeville.game.character.player.skill.SkillData;
import edgeville.game.character.player.skill.Skills;
import edgeville.game.item.Item;
import edgeville.game.item.ItemDefinition;
import edgeville.game.item.container.Bank;
import edgeville.game.location.Position;
import edgeville.game.object.ObjectDirection;
import edgeville.game.object.ObjectNode;
import edgeville.game.object.ObjectNodeManager;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;

/**
 * The message that is sent from the client when the player chats anything
 * beginning with '::'.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class CommandMessage implements InputMessageListener {

	private static String glue(String... args) {
		return Arrays.stream(args).collect(Collectors.joining(" "));
	}

	@Override
	public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {

		String[] command = payload.getString().toLowerCase().split(" ");

		switch (player.getRights()) {
		case PLAYER:
		case DONATOR:
		case VETERAN:
			handlePlayerCommands(player, command);
			break;
		case MODERATOR:
			handlePlayerCommands(player, command);
			handleModeratorCommands(player, command);
			break;
		case ADMINISTRATOR:
			handlePlayerCommands(player, command);
			handleModeratorCommands(player, command);
			handleAdministratorCommands(player, command);
			break;
		case DEVELOPER:
			handlePlayerCommands(player, command);
			handleModeratorCommands(player, command);
			handleAdministratorCommands(player, command);
			handleDeveloperCommands(player, command);
			break;
		}
	}

	private void handlePlayerCommands(Player player, String[] command) {

	}

	private void handleModeratorCommands(Player player, String[] command) {
		switch (command[0].toLowerCase()) {
		case "debugon":
			player.setDebugEnabled(true);
			player.message("Debug is now @blu@enabled@bla@.");
			break;
		case "debugoff":
			player.setDebugEnabled(false);
			player.message("Debug is now @blu@disabled@bla@.");
			break;
		}
	}

	private void handleAdministratorCommands(Player player, String[] command) {
		switch (command[0].toLowerCase()) {
		case "anim":
			player.animation(new Animation(Integer.parseInt(command[1])));
			break;
		case "gfx":
			player.graphic(new Graphic(Integer.parseInt(command[1])));
			break;
		}
	}

	private void handleDeveloperCommands(Player player, String[] command) {
		switch (command[0].toLowerCase()) {

		case "s":
			int id = Integer.parseInt(command[1]);
			int amount = Integer.parseInt(command[2]);
			NpcDropTable table = NpcDropManager.TABLES.get(id);
			Bank bank = new Bank(player);
			for (Item item : table.toItems(player)) {
				bank.deposit(item);
			}
			bank.open();
			break;
		case "pnpc":
			int npcId = Integer.parseInt(command[1]);
			player.setPlayerNpc(npcId);
			player.getFlags().set(Flag.APPEARANCE);
			break;
		case "invisible":
			player.setVisible(false);
			break;
		case "visible":
			player.setVisible(true);
			break;
		case "setlevel":
			int skillId = Integer.parseInt(command[1]);
			int level = Integer.parseInt(command[2]);
			player.setLevel(player, skillId, level);
			break;

		case "interface":
			player.getMessages().sendInterface(Integer.parseInt(command[1]));
			break;
		case "sound":
			player.getMessages().sendSound(Integer.parseInt(command[1]), 0, Integer.parseInt(command[2]));
			break;

		case "npc":
			Npc n = new Npc(Integer.parseInt(command[1]), player.getPosition());
			if (command.length == 3 && command[2].equals("true"))
				n.setRespawn(true);
			World.getNpcs().add(n);
			break;
		case "dummy":
			Npc npc = new Npc(Integer.parseInt(command[1]), player.getPosition());
			npc.setCurrentHealth(100000);
			npc.setAutoRetaliate(false);
			World.getNpcs().add(npc);
			break;
		case "music":
			player.getMessages().sendMusic(Integer.parseInt(command[1]));
			break;

		case "object":
			ObjectNodeManager.register(new ObjectNode(Integer.parseInt(command[1]), player.getPosition(), ObjectDirection.SOUTH));
			break;

		case "update":
			int delay = Integer.parseInt(command[1]);
			World.update(delay);
			break;

		case "master":
			for (int i = 0; i <= 6; i++) {
				//Skill skill = player.getSkills()[i];
				Skills.experience(player, 15000000, i);
			}
			player.determineCombatLevel();
			break;
		case "item":
			if (command.length > 2) {
				player.getInventory().add(new Item(Integer.parseInt(command[1]), Integer.parseInt(command[2])));
			} else {
				player.getInventory().add(new Item(Integer.parseInt(command[1]), 1));
			}
			break;
		case "tele":
			player.move(new Position(Integer.parseInt(command[1]), Integer.parseInt(command[2]), (command.length > 3) ? Integer.parseInt(command[3]) : 0));
			break;
		case "teleto":
			Player p = World.getPlayer(command[1]).orElse(null);
			if (p == null)
				return;
			player.move(p.getPosition());
			break;
		case "teletome":
			Player p2 = World.getPlayer(command[1]).orElse(null);
			if (p2 == null)
				return;
			p2.move(player.getPosition());
			break;
		case "edge":
			player.move(new Position(3093, 3493));
			break;
		case "mypos":
			player.message("x: %d, y: %d, height: %d", player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
			break;
		case "local":
			int lx = player.getPosition().getLocalX();
			int ly = player.getPosition().getLocalY();
			int x = player.getPosition().getLocalX() + 8 * player.getPosition().getRegionX();
			int y = player.getPosition().getLocalY() + 8 * player.getPosition().getRegionY();
			String message = String.format("localx: %d (%d), localy:%d (%d)", lx, x, ly, y);
			player.getMessages().sendMessage(message);
			break;
		case "find":
			String item = command[1];
			if (command.length >= 3) {
				item += " " + command[2];
			}
			final String search = item.toLowerCase();

			new Thread(() -> {
				int found = 0;

				for (int i = 0; i < 7900; i++) {
					if (found >= 250) {
						player.message("Too many results, try again.");
						return;
					}
					ItemDefinition def = new Item(i).getDefinition();
					if (def != null && def.getName().toLowerCase().contains(search)) {
						player.message("Result: " + i + " - " + def.getName());
						found++;
					}
				}
				player.message("Found " + found + " items.");
			}).start();
		}
	}
}
