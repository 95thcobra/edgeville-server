package edgeville.actions;

import edgeville.game.character.Animation;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.Spellbook;
import edgeville.game.character.player.skill.Skills;
import edgeville.game.location.Position;

public class ObjectFirstClickAction {

	public void handle(Player player, int objectId, Position position) {
		switch (objectId) {

		// prayer altar
		case 409:
			int level = player.getSkills()[Skills.PRAYER].getRealLevel();
			if (player.getSkills()[Skills.PRAYER].getLevel() < level) {
				player.animation(new Animation(645));
				player.getSkills()[Skills.PRAYER].setLevel(level, true);
				player.message("You recharge your prayer points.");
				Skills.refresh(player, Skills.PRAYER);
			} else {
				player.message("You already have full prayer points.");
			}
			break;

		// switch spellbook altar
		case 6552:
			if (player.getSpellbook() == Spellbook.ANCIENT) {
				Spellbook.convert(player, Spellbook.NORMAL);
			} else if (player.getSpellbook() == Spellbook.NORMAL) {
				Spellbook.convert(player, Spellbook.ANCIENT);
			}
			break;

		// edgeville ditch
		case 23271:
			Position pos = player.getPosition();
			boolean below = pos.getY() < 3521;
			player.move(new Position(pos.getX(), below ? 3523 : 3520));
			break;

		// bandos door to boss
		case 26503:
			int x = player.getPosition().getX();
			boolean outside = x < 2863;
			player.move(new Position(outside ? 2864 : 2861, 5354, 2));
			break;
		}
	}

}
