package edgeville.actions;

import java.util.Optional;
import java.util.function.Consumer;

import edgeville.game.character.Animation;
import edgeville.game.character.Locations;
import edgeville.game.character.npc.Npc;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.Spellbook;
import edgeville.game.character.player.dialogue.NpcDialogue;
import edgeville.game.character.player.dialogue.OptionDialogue;
import edgeville.game.character.player.dialogue.OptionType;
import edgeville.game.character.player.skill.Skills;
import edgeville.game.location.Position;

public class NpcFirstClickAction {

	public void handle(Player player, Npc npc, Position position) {
		int npcId = npc.getId();
		switch (npcId) {

		// edge teleport wizard
		case 4400:
			player.getDialogueChain().append(new NpcDialogue(npcId, "Where would you like to teleport to?"));
			player.getDialogueChain().append(new OptionDialogue("Godwars Dungeon", "Other") {
				@Override
				public Optional<Consumer<OptionType>> getOptionListener() {
					return Optional.of(new Consumer<OptionType>() {
						@Override
						public void accept(OptionType t) {
							switch (t) {
							case FIRST_OPTION:
								player.teleport(Locations.BANDOS_OUTSIDE.getPosition());
								break;
							case SECOND_OPTION:
								player.message("Other");
								break;
								
							default:
								player.message("Unhandled option");
								break;
							}
							player.getMessages().sendCloseWindows();
						}
					});
				}
			});
			player.getDialogueChain().advance();
			break;
		}
	}

}
