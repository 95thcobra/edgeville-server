package edgeville.task.impl;

import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;

import edgeville.game.World;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.serialize.PlayerSerialization;
import edgeville.task.Task;

public class SystemUpdateTask extends Task {

	public SystemUpdateTask(int delay) {
		super(delay, false);
	}

	@Override
	public void execute() {
		World.setServerUpdated(true);
		for (Player player : World.getPlayers()) {
			if (player == null) {
				continue;
			}
			//World.getPlayers().remove(player); // logs ppl out
			//player.save();
			new PlayerSerialization(player).serialize();
		}

		try {
			if (SystemUtils.IS_OS_LINUX) {
				Runtime.getRuntime().exec("killall screen");
				Runtime.getRuntime().exec("screen -A -m -d -S rsps java -classpath bin:lib/* edgeville.GameServer");
			} else if (SystemUtils.IS_OS_WINDOWS) {
				Runtime.getRuntime().exec("cmd /c start run.bat");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Server has shut down.");
		System.exit(0);
		
		//this.cancel(); // obviously not necessary
	}

}
