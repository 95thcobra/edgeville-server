package edgeville.net.message.impl;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import edgeville.actions.ButtonClickAction;
import edgeville.game.World;
import edgeville.game.character.Locations;
import edgeville.game.character.combat.magic.CombatSpells;
import edgeville.game.character.combat.prayer.CombatPrayer;
import edgeville.game.character.combat.weapon.CombatSpecial;
import edgeville.game.character.combat.weapon.FightType;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.Rights;
import edgeville.game.character.player.content.Spellbook;
import edgeville.game.character.player.content.TradeStage;
import edgeville.game.character.player.content.WeaponInterface;
import edgeville.game.character.player.dialogue.OptionType;
import edgeville.game.character.player.minigame.Minigame;
import edgeville.game.character.player.minigame.MinigameHandler;
import edgeville.game.item.container.Equipment;
import edgeville.game.location.Position;
import edgeville.game.plugin.context.ButtonClickPlugin;
import edgeville.net.message.InputMessageListener;
import edgeville.net.message.MessageBuilder;
import edgeville.task.Task;
import edgeville.utility.BufferUtils;

/**
 * The message sent from the client when the player clicks some sort of button
 * or module.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ClickButtonMessage implements InputMessageListener {

	// TODO: Convert all buttons to the proper identifications.

	/**
	 * The flag that determines if this message should be read properly.
	 */
	private static final boolean PROPER_READ = false;

	@Override
	public void handleMessage(Player player, int opcode, int size, MessageBuilder payload) {
		int button = PROPER_READ ? payload.getShort() : BufferUtils.hexToInt(payload.getBytes(2));
		//World.getPlugins().execute(player, ButtonClickPlugin.class, new ButtonClickPlugin(button));
		if (player.getRights() == Rights.DEVELOPER) {
			player.message("Button clicked: %d", button);
		}

		new ButtonClickAction().handle(player, button);
	}
}
