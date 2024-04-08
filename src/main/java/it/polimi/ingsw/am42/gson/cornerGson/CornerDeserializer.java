package it.polimi.ingsw.am42.gson.cornerGson;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.enumeration.CornerState;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;

import java.lang.reflect.Type;

/**
 * This class allows to deserialize correctly the Corner class in gson.
 *
 * @author Rodrigo Almandoz Franco
 */


public class CornerDeserializer implements JsonDeserializer<Corner> {
    @Override
    public Corner deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();

        CornerState state = CornerState.valueOf(jsonObject.get("state").getAsString());
        Direction direction = Direction.valueOf(jsonObject.get("direction").getAsString());


        if(!jsonObject.get("resource").isJsonNull()){
            Resource resource = Resource.valueOf(jsonObject.get("resource").getAsString());
            return new Corner(resource, state, direction);
        }
        return new Corner(null, state, direction);
    }
}
