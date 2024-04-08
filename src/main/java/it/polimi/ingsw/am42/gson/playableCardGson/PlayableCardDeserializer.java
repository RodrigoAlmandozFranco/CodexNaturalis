package it.polimi.ingsw.am42.gson.playableCardGson;


import com.google.gson.*;
import it.polimi.ingsw.am42.model.cards.types.Back;
import it.polimi.ingsw.am42.model.cards.types.Front;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.GoldCard;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.cards.types.playables.StartingCard;
import it.polimi.ingsw.am42.model.enumeration.Resource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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

        switch(jsonObject.get("type").getAsString()){
            case "ResourceCard":
                return new ResourceCard(id, front, back);
            case "GoldCard":
                return new GoldCard(id, front, back);
            case "StartingCard":
                List<Resource> l = new ArrayList<>();
                if(jsonObject.get("resource") != null && !jsonObject.get("resource").isJsonNull()) {
                    JsonArray jsonArray = jsonObject.get("resource").getAsJsonArray();
                    for(int i = 0; i < jsonArray.size(); i++) {
                        JsonObject element = jsonArray.get(i).getAsJsonObject();
                        Resource r = Resource.valueOf(element.get("resource").getAsString());
                        l.add(r);
                    }
                }
                return new StartingCard(id, front, back, l);
            default:
                return null;
        }
    }
}

