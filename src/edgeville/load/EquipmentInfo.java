package edgeville.load;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edgeville.Constants;
import edgeville.game.character.player.Player;
import edgeville.game.item.Item;

/**
 * @author Simon on 1-9-2016
 */
public class EquipmentInfo {

	public static ItemDefinitionNew[] itemDefinitions = new ItemDefinitionNew[Constants.ITEM_COUNT];

	public static void loadAll() {
		loadItemDefinitions();
		loadBonuses();
		loadEquipmentSlots();
	}

	public static void loadItemDefinitions() {
		try (Scanner scanner = new Scanner(new File(Constants.ITEM_DEFINITIONS))) {
			int count = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.startsWith("//")) {
					continue;
				}

				String[] array = line.split(":");
				int id = Integer.parseInt(array[0]);

				if (id > Constants.ITEM_COUNT) {
					return;
				}

				String name = array[1];
				boolean noted = Boolean.parseBoolean(array[2]);
				boolean noteable = Boolean.parseBoolean(array[3]);
				boolean stackable = Boolean.parseBoolean(array[4]);
				int parentId = Integer.parseInt(array[5]);
				int notedId = Integer.parseInt(array[6]);
				int price = Integer.parseInt(array[7]);
				int highAlch = Integer.parseInt(array[8]);
				int lowAlch = Integer.parseInt(array[9]);

				itemDefinitions[id] = new ItemDefinitionNew(id, name, "It's a " + name + ".", noted, noteable, stackable, parentId, notedId, price, highAlch, lowAlch);

				count++;
			}

			System.out.printf("%d item definitions loaded.\n", count);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void loadBonuses() {
		int numdef = 0;
		try (Scanner scanner = new Scanner(new File(Constants.ITEM_BONUSES))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				int id = Integer.parseInt(line.split(":")[0]);

				if (id > Constants.ITEM_COUNT) {
					return;
				}

				String params[] = line.split(":")[1].split(",");

				int[] bonuses = new int[14];
				for (int i = 0; i < bonuses.length; i++) {
					bonuses[i] = Integer.parseInt(params[i]);
				}

				ItemDefinitionNew itemDef = EquipmentInfo.itemDefinitions[id];
				if (itemDef == null) {
					continue;
				}

				itemDef.setBonuses(bonuses);

				numdef++;
			}
			System.out.printf("%d equipment bonuses loaded.\n", numdef);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void loadEquipmentSlots() {
		try (Scanner scanner = new Scanner(new File(Constants.ITEM_SLOTS))) {
			int numdef = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				int id = Integer.parseInt(line.split(":")[0]);

				if (id > Constants.ITEM_COUNT) {
					return;
				}

				String params = line.split(":")[1];

				int slot = Byte.parseByte(params.split(",")[0]);
				int type = Byte.parseByte(params.split(",")[1]);

				//slots[id] = Byte.parseByte(params.split(",")[0]);
				//types[id] = Byte.parseByte(params.split(",")[1]);

				ItemDefinitionNew itemDef = EquipmentInfo.itemDefinitions[id];
				if (itemDef == null) {
					continue;
				}
				itemDef.setEquipmentSlot(slot);

				numdef++;
			}

			System.out.printf("%d equipment slots loaded.\n", numdef);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Bonuses getTotalEquipmentBonuses(Player player) {
		int[] totalBonuses = new int[14];
		for (int i = 0; i < 14; i++) {
			Item item = player.getEquipment().get(i);
			if (item == null) {
				continue;
			}
			int itemId = item.getId();
			int[] bonuses = itemDefinitions[itemId].getBonuses().getBonusesAsArray();

			totalBonuses[i] += bonuses[i];
			
		}
		
		return new Bonuses(totalBonuses);
	}
}
