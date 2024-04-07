package it.polimi.ingsw.am42.gson.evaluatorGson;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.enumeration.Direction;
import it.polimi.ingsw.am42.model.enumeration.Resource;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPoints;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPointsPerCorner;
import it.polimi.ingsw.am42.model.evaluator.EvaluatorPointsPerResource;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * This class allows to deserialize correctly the Evaluator class and its subclasses in gson.
 *
 * @author Rodrigo Almandoz Franco
 */


public class EvaluatorDeserializer implements JsonDeserializer<Evaluator> {
    @Override
    public Evaluator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();
        String evaluator = jsonObject.get("type").getAsString();

        int numPoints = jsonObject.get("numPoints").getAsInt();

        Direction direction;

        switch (evaluator) {
            case "EvaluatorPoints":
                return new EvaluatorPoints(numPoints);

            case "EvaluatorPointsPerCorner":
                return new EvaluatorPointsPerCorner(numPoints);

            case "EvaluatorPointsPerResource":
                Map<Resource, Integer> resourceMap = new HashMap<>();
                JsonObject resourceJson = jsonObject.getAsJsonObject("resourceMap");
                for (Map.Entry<String, JsonElement> entry : resourceJson.entrySet()) {
                    Resource resource = Resource.valueOf(entry.getKey());
                    int value = entry.getValue().getAsInt();
                    resourceMap.put(resource, value);
                }
                return new EvaluatorPointsPerResource(numPoints, resourceMap);

            case "EvaluatorPointsPerStair":
                Resource kingdom = Resource.valueOf(jsonObject.get("kingdom").getAsString());
                direction = Direction.valueOf(jsonObject.get("direction").getAsString());
                return new EvaluatorPointsPerStair(numPoints, kingdom, direction);

            case "EvaluatorPointsPerChair":
                Resource kingdom1 = Resource.valueOf(jsonObject.get("kingdom1").getAsString());
                Resource kingdom2 = Resource.valueOf(jsonObject.get("kingdom2").getAsString());
                direction = Direction.valueOf(jsonObject.get("direction").getAsString());
                return new EvaluatorPointsPerChair(numPoints, kingdom1, kingdom2, direction);

            default:
                return null;
        }
    }
}

