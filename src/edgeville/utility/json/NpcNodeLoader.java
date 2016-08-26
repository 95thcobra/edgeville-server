package edgeville.utility.json;

import java.util.Objects;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import edgeville.game.World;
import edgeville.game.character.npc.Npc;
import edgeville.game.location.Position;
import edgeville.utility.JsonLoader;

/**
 * The {@link JsonLoader} implementation that loads all npc nodes.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class NpcNodeLoader extends JsonLoader {

    /**
     * Creates a new {@link NpcNodeLoader}.
     */
    public NpcNodeLoader() {
        super("./data/json/npcs/npc_nodes.json");
    }

    @Override
    public void load(JsonObject reader, Gson builder) {
        int id = reader.get("id").getAsInt();
        Position position = Objects.requireNonNull(builder.fromJson(reader.get("position").getAsJsonObject(), Position.class));
        boolean coordinate = reader.get("random-walk").getAsBoolean();
        int radius = reader.get("walk-radius").getAsInt();
        
        String direction = reader.get("direction").getAsString();
        
        Preconditions.checkState(!(coordinate && radius == 0));
        Preconditions.checkState(!(!coordinate && radius > 0));
        Npc npc = new Npc(id, position);
        npc.setOriginalRandomWalk(coordinate);
        npc.getMovementCoordinator().setCoordinate(coordinate);
        npc.getMovementCoordinator().setRadius(radius);
        npc.setRespawn(true);
        
        if (!World.getNpcs().add(npc))
            throw new IllegalStateException("NPC could not be added to the " + "world!");

       // System.out.println(direction);
        switch(direction) {
        case "north":
    		npc.facePosition(new Position(npc.getPosition().getX(), npc.getPosition().getY() + 1));
        	break;
        case "east":
    		npc.facePosition(new Position(npc.getPosition().getX() + 1, npc.getPosition().getY()));
        	break;
        case "west":
    		npc.facePosition(new Position(npc.getPosition().getX() - 1, npc.getPosition().getY()));
        	break;
        case "south":
    		npc.facePosition(new Position(npc.getPosition().getX(), npc.getPosition().getY() - 1));
        	break;
        }
    }
}
