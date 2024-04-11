package it.polimi.ingsw.am42.gson.goalCardGson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;

import java.lang.reflect.Type;


/**
 * This class allows to deserialize correctly the GoalCard class in gson.
 *
 * @author Rodrigo Almandoz Franco
 */


public class GoalCardDeserializer implements JsonDeserializer<GoalCard> {
    @Override
    public GoalCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject jsonObject = json.getAsJsonObject();

        int id = jsonObject.get("id").getAsInt();
        String srcImage = jsonObject.get("srcImage").getAsString();
        Evaluator eval = context.deserialize(jsonObject.get("evaluator"), Evaluator.class);

        return new GoalCard(id, srcImage, eval);
    }
}
