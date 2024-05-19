package it.polimi.ingsw.am42.gson;


import com.google.gson.*;
import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.GoldCard;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.cards.types.playables.StartingCard;

import java.lang.reflect.Type;


/**
 * This class allows to deserialize correctly the PlayableCard class and its subclasses in gson.
 *
 * @author Rodrigo Almandoz Franco
 */

public class PlayableCardDeserializer implements JsonDeserializer<PlayableCard> {
    @Override
    public PlayableCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        JsonObject jsonObject = json.getAsJsonObject();

        int id = jsonObject.get("id").getAsInt();

        Front front = context.deserialize(jsonObject.get("front"), Front.class);
        Back back = context.deserialize(jsonObject.get("back"), Back.class);
        front.setId(id);
        back.setId(id);

        return switch (jsonObject.get("type").getAsString()) {
            case "ResourceCard" -> new ResourceCard(id, front, back);
            case "GoldCard" -> new GoldCard(id, front, back);
            case "StartingCard" -> new StartingCard(id, front, back);
            default -> null;
        };
    }
}

