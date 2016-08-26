package edgeville.utility.json;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edgeville.game.location.Position;
import edgeville.game.object.ObjectDirection;
import edgeville.game.object.ObjectNode;
import edgeville.game.object.ObjectNodeManager;
import edgeville.game.object.ObjectType;
import edgeville.utility.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all object nodes.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class ObjectNodeLoader extends JsonLoader {

    /**
     * Create a new {@link ObjectNodeLoader}.
     */
    public ObjectNodeLoader() {
        super("./data/json/objects/object_nodes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        Position position = Objects.requireNonNull(builder.fromJson(reader.get("position"), Position.class));
        ObjectDirection face = Objects.requireNonNull(ObjectDirection.valueOf(reader.get("direction").getAsString()));
        ObjectType type = Objects.requireNonNull(ObjectType.valueOf(reader.get("type").getAsString()));
        Preconditions.checkState(!ObjectNodeManager.REMOVE_OBJECTS.contains(position));
        ObjectNodeManager.register(new ObjectNode(id, position, face, type));
    }
}