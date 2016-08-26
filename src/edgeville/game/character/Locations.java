package edgeville.game.character;

import edgeville.game.location.Position;

/**
 * Created by Sky on 25-6-2016.
 */
public enum Locations {
    EDGEVILLE(new Position(3097, 3499)),
    VARROCK(new Position(3210, 3424)),
    FALADOR(new Position(2964, 3378)),
    LUMBRIDGE(new Position(3222, 3218)),
    CAMELOT(new Position(2757, 3477)),
    
    // PVM
    BANDOS_OUTSIDE(new Position(2861, 5354, 2)),

    SPAWN_LOCATION(new Position(3097, 3499)),
    RESPAWN_LOCATION(new Position(3097, 3499));

    private Position position;

    Locations(Position tile) {
        this.position = tile;
    }

    public Position getPosition() {
        return position;
    }
}
