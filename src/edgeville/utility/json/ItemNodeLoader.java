package edgeville.utility.json;

import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edgeville.game.item.Item;
import edgeville.game.item.ItemNodeManager;
import edgeville.game.item.ItemNodeStatic;
import edgeville.game.item.ItemPolicy;
import edgeville.game.location.Position;
import edgeville.utility.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all item nodes.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ItemNodeLoader extends JsonLoader {

    /**
     * Creates a new {@link ItemNodeLoader}.
     */
    public ItemNodeLoader() {
        super("./data/json/items/item_nodes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        int amount = reader.get("amount").getAsInt();
        Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
        ItemNodeManager.register(new ItemNodeStatic(new Item(id, amount), position, ItemPolicy.TIMEOUT));
    }
}
