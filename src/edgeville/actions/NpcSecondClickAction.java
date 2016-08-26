package edgeville.actions;

import edgeville.game.character.Animation;
import edgeville.game.character.npc.Npc;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.Spellbook;
import edgeville.game.character.player.skill.Skills;
import edgeville.game.location.Position;

public class NpcSecondClickAction {

	public void handle(Player player, Npc npc, Position position) {
		switch (npc.getId()) {
		
		// edgeville ditch
		case 23271:
			Position pos = player.getPosition();
			boolean below = pos.getY() < 3521;
			player.move(new Position(pos.getX(), below ? 3523 : 3520));
			break;
		}
	}

}
