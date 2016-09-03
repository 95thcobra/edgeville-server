package edgeville.load;

public class Bonuses {
	private int[] bonuses;
	
	public Bonuses(int[] bonuses) {
		this.bonuses = bonuses;
	}
	
	public int[] getBonusesAsArray() {
		return bonuses;
	}
	
	public int getStabAttackBonus() {
		return bonuses[0];
	}
	
	public int getSlashAttackBonus() {
		return bonuses[1];
	}
	
	public int getCrushAttackBonus() {
		return bonuses[2];
	}
	
	public int getMagicAttackBonus() {
		return bonuses[3];
	}
	
	public int getRangeAttackBonus() {
		return bonuses[4];
	}
	public int getStabDefenceBonus() {
		return bonuses[5];
	}
	
	public int getSlashDefenceBonus() {
		return bonuses[6];
	}
	
	public int getCrushDefenceBonus() {
		return bonuses[7];
	}
	
	public int getMagicDefenceBonus() {
		return bonuses[8];
	}
	
	public int getRangeDefenceBonus() {
		return bonuses[9];
	}
	
	public int getMeleeStrengthBonus() {
		return bonuses[10];
	}
	
	public int getRangedStrengthBonus() {
		return bonuses[11];
	}
	
	public int getMagicDamageBonus() {
		return bonuses[12];
	}
	
	public int getPrayerBonus() {
		return bonuses[13];
	}
	
	public int getUndeadBonus() {
		return 0;//TODO
	}
	
	public int getSlayerBonus() {
		return 0;//TODO
	}
	
}
