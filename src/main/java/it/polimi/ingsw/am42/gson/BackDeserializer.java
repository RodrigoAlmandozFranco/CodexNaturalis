package it.polimi.ingsw.am42.gson;


import com.google.gson.*;
import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Resource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows to deserialize correctly the Back class in gson.
 *
 * @author Rodrigo Almandoz Franco
 */



public class BackDeserializer implements JsonDeserializer<Back> {
    @Override
    public Back deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();

        String srcImage = jsonObject.get("srcImage").getAsString();
        JsonArray jsonArray = jsonObject.get("corners").getAsJsonArray();
        List<Corner> corners = new ArrayList<>();

        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject element = jsonArray.get(i).getAsJsonObject();
            Corner corner = context.deserialize(element, Corner.class);
            corners.add(corner);
        }

        Color color = Color.valueOf(jsonObject.get("color").getAsString());

        List<Resource> resource = new ArrayList<>();
        jsonArray = jsonObject.get("resource").getAsJsonArray();

        for(int i = 0; i < jsonArray.size(); i++) {
            Resource r = Resource.valueOf(jsonArray.get(i).getAsString());
            resource.add(r);
        }
        return new Back(srcImage, corners, color, resource);
    }
}

