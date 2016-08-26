package edgeville.utility.json;

import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edgeville.game.character.PoisonType;
import edgeville.game.character.combat.effect.CombatPoisonEffect;
import edgeville.utility.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all weapons that poison
 * players.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class WeaponPoisonLoader extends JsonLoader {

    /**
     * Creates a new {@link WeaponPoisonLoader}.
     */
    public WeaponPoisonLoader() {
        super("./data/json/equipment/weapon_poison.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        PoisonType type = Objects.requireNonNull(PoisonType.valueOf(reader.get("type").getAsString()));
        CombatPoisonEffect.TYPES.put(id, type);
    }
}
