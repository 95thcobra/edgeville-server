package edgeville.actions;

import edgeville.game.character.Animation;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.Spellbook;
import edgeville.game.character.player.skill.Skills;
import edgeville.game.location.Position;

public class ObjectSecondClickAction {

	public void handle(Player player, int objectId, Position position) {
		switch (objectId) {
			// bank
		case 11744:
			player.getBank().open();
			break;
		}
	}

}
