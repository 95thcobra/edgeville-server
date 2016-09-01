package edgeville.load;

/**
 * @author Simon on 1-9-2016
 */
public class ItemDefinitionNew {

	private int id;
	private String name;
	private String examine;
	private boolean noted;
	private boolean noteable;
	private boolean stackable;
	private int parentId;
	private int notedId;
	private int price;
	private int highAlchPrice;
	private int lowAlchPrice;

	// set afterwards
	private int[] bonuses;
	private int equipmentSlot;

	public ItemDefinitionNew(int id, String name, String examine,
			/* int[] bonuses, */ boolean noted, boolean noteable, boolean stackable, int parentId, int notedId, int price, int highAlchPrice, int lowAlchPrice) {
		this.id = id;
		this.name = name;
		this.examine = examine;
		//this.bonuses = bonuses;
		this.noted = noted;
		this.noteable = noteable;
		this.stackable = stackable;
		this.parentId = parentId;
		this.notedId = notedId;
		this.price = price;
		this.highAlchPrice = highAlchPrice;
		this.lowAlchPrice = lowAlchPrice;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getExamine() {
		return examine;
	}

	public boolean isNoted() {
		return noted;
	}

	public boolean isNoteable() {
		return noteable;
	}

	public boolean isStackable() {
		return stackable;
	}

	public int getParentId() {
		return parentId;
	}

	public int getNotedId() {
		return notedId;
	}

	public int getPrice() {
		return price;
	}

	public int getHighAlchPrice() {
		return highAlchPrice;
	}

	public int getLowAlchPrice() {
		return lowAlchPrice;
	}

	public int[] getBonuses() {
		return bonuses;
	}

	public void setBonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}

	public int getEquipmentSlot() {
		return equipmentSlot;
	}

	public void setEquipmentSlot(int equipmentSlot) {
		this.equipmentSlot = equipmentSlot;
	}
}
