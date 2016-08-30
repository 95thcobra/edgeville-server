package edgeville.game.character.combat.strategy;

import edgeville.game.NodeType;
import edgeville.game.character.Animation;
import edgeville.game.character.AnimationPriority;
import edgeville.game.character.Entity;
import edgeville.game.character.Flag;
import edgeville.game.character.Graphic;
import edgeville.game.character.Projectile;
import edgeville.game.character.combat.Combat;
import edgeville.game.character.combat.CombatSessionData;
import edgeville.game.character.combat.CombatStrategy;
import edgeville.game.character.combat.CombatType;
import edgeville.game.character.combat.ranged.CombatRangedAmmo;
import edgeville.game.character.combat.ranged.CombatRangedBow;
import edgeville.game.character.combat.weapon.FightStyle;
import edgeville.game.character.combat.weapon.FightType;
import edgeville.game.character.npc.Npc;
import edgeville.game.character.player.Player;
import edgeville.game.character.player.content.WeaponInterface;
import edgeville.game.item.Item;
import edgeville.game.item.container.Equipment;
import edgeville.game.region.Region;

public class DefaultRangedCombatStrategy implements CombatStrategy {

	@Override
	public boolean canAttack(Entity character, Entity victim) {
		
		if (!Region.canAttack(character, victim)) {
			return false;
		}
		
		if (character.getType() == NodeType.NPC)
			return true;
		Player player = (Player) character;
		if (Combat.isCrystalBow(player)) {
			return true;
		}
		return checkAmmo(player);
	}
	
	@Override
	public CombatSessionData attack(Entity character, Entity victim) {
		if (character.getType() == NodeType.NPC) {
            Npc npc = (Npc)character;
            CombatRangedAmmo ammo = Combat.prepareRangedAmmo(npc);
            character.animation(new Animation(npc.getDefinition().getAttackAnimation()));
            character.highGraphic(new Graphic(ammo.getGraphic()));
            new Projectile(character, victim, ammo.getProjectile(), ammo.getDelay(), ammo.getSpeed(), ammo.getStartHeight(), ammo
                    .getEndHeight(), 0).sendProjectile();
            return new CombatSessionData(character, victim, 1, CombatType.RANGED, true);
        }

        Player player = (Player)character;
        player.setRangedAmmo(null);
        player.setFireAmmo(0);
        CombatRangedAmmo ammo = CombatRangedAmmo.getPlayerAmmo(player).get();
        if(!CombatRangedBow.canUse(player, ammo)) {
            player.getCombatBuilder().reset();
            return new CombatSessionData(character, victim, null, true);
        }
        player.setRangedAmmo(ammo);
        if (!Combat.isCrystalBow(player)) {
            decrementAmmo(player);
        }
        if (!player.isSpecialActivated()) {
            player.highGraphic(new Graphic(ammo.getGraphic()));
            new Projectile(character, victim, ammo.getProjectile(), ammo.getDelay(), ammo.getSpeed(), ammo.getStartHeight(), ammo
                    .getEndHeight(), 0).sendProjectile();
        }
        startAnimation(player);
        return new CombatSessionData(character, victim, 1, CombatType.RANGED, true);
	}

	@Override
	public int attackDelay(Entity character) {
		int attackSpeed = character.getAttackSpeed();
		if (character instanceof Player) {
			((Player)character).message("Attackspeed: %d", attackSpeed);
		}
		return character.getAttackSpeed();
	}

	@Override
	public int attackDistance(Entity character) {
		if (character.getType() == NodeType.NPC)
			return 6;
		Player player = (Player) character;
		return Combat.getRangedDistance(player.getWeapon()) + (player.getFightType().getStyle() == FightStyle.DEFENSIVE ? 2 : 0);
	}

	@Override
	public int[] getNpcs() {
		return new int[] { 688 };
	}
	
	private void startAnimation(Player player) {
		if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils")) {
			player.animation(new Animation(2075));
		} else {
			player.animation(new Animation(player.getFightType().getAnimation()));
		}
	}
	
	private boolean checkAmmo(Player player) {
		Item item = player.getWeapon() == WeaponInterface.DART || player.getWeapon() == WeaponInterface.KNIFE || player.getWeapon() == WeaponInterface.JAVELIN || player.getWeapon() == WeaponInterface.THROWNAXE ? player.getEquipment().get(Equipment.WEAPON_SLOT) : player.getEquipment().get(Equipment.ARROWS_SLOT);

		if (!Item.valid(item)) {
			player.message("You do not have enough ammo to use this ranged weapon.");
			player.getCombatBuilder().reset();
			return false;
		}
		if (player.getWeapon() == WeaponInterface.SHORTBOW || player.getWeapon() == WeaponInterface.LONGBOW) {
			if (!Combat.isArrows(player)) {
				player.message("You need to use arrows with your bow.");
				player.getCombatBuilder().reset();
				return false;
			}
		} else if (player.getWeapon() == WeaponInterface.CROSSBOW) {
			if (player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils") && !item.getDefinition().getName().endsWith("rack")) {
				player.message("You need to use bolt racks with this crossbow.");
				player.getCombatBuilder().reset();
				return false;
			} else if (!player.getEquipment().get(Equipment.WEAPON_SLOT).getDefinition().getName().startsWith("Karils") && !Combat.isBolts(player)) {
				player.message("You need to use bolts with your crossbow.");
				player.getCombatBuilder().reset();
				return false;
			}
		}
		return true;
	}

	private void decrementAmmo(Player player) {
		int slot = player.getWeapon() == WeaponInterface.SHORTBOW || player.getWeapon() == WeaponInterface.LONGBOW || player.getWeapon() == WeaponInterface.CROSSBOW ? Equipment.ARROWS_SLOT : Equipment.WEAPON_SLOT;

		player.setFireAmmo(player.getEquipment().get(slot).getId());
		player.getEquipment().get(slot).decrementAmount();

		if (player.getEquipment().get(slot).getAmount() == 0) {
			player.message("That was your last piece of ammo!");
			player.getEquipment().set(slot, null);

			if (slot == Equipment.WEAPON_SLOT) {
				WeaponInterface.execute(player, null);
			}
		}

		if (slot == Equipment.WEAPON_SLOT) {
			player.getFlags().set(Flag.APPEARANCE);
		}
		player.getEquipment().refresh();
	}
}
