package it.polimi.ingsw.am42.gson.frontGson;

import com.google.gson.*;
import it.polimi.ingsw.am42.model.cards.types.Corner;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class allows to deserialize correctly the Front class in gson.
 *
 * @author Rodrigo Almandoz Franco
 */


public class FrontDeserializer implements JsonDeserializer<Front> {
    @Override
    public Front deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
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
        Map<Resource, Integer> requirements = new HashMap<>();

        if(!jsonObject.get("requirements").isJsonNull()){
            JsonObject requirementsJson = jsonObject.getAsJsonObject("requirements");
            for (Map.Entry<String, JsonElement> entry : requirementsJson.entrySet()) {
                Resource resource = Resource.valueOf(entry.getKey());
                int value = entry.getValue().getAsInt();
                requirements.put(resource, value);
            }
        }


        Evaluator evaluator = context.deserialize(jsonObject.get("evaluator"), Evaluator.class);

        return new Front(srcImage, corners, color, requirements, evaluator);
    }
}

