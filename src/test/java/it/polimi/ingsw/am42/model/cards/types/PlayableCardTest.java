package it.polimi.ingsw.am42.model.cards.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.gson.*;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.decks.PlayableDeck;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {
    /**
     * This class represents a Card
     * It has an id
     * @see it.polimi.ingsw.am42.model.cards.Card
     * @author Rodrigo Almandoz Franco
     */

        @org.junit.jupiter.api.Test
        void getFront() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            PlayableCard card = new PlayableCard(1, front, back);
            assertEquals(front, card.getFront());
        }

        @org.junit.jupiter.api.Test
        void getBack() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            PlayableCard card = new PlayableCard(1, front, back);
            assertEquals(back, card.getBack());
        }

        @org.junit.jupiter.api.Test
        void setVisibility() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            PlayableCard card = new PlayableCard(1, front, back);
            card.setVisibility(true);
            assertTrue(card.getVisibility());
        }

        @org.junit.jupiter.api.Test
        void getVisibility() {
            Front front = new Front("front", null, Color.RED, null, null);
            Back back = new Back("back", null, Color.RED, null);
            PlayableCard card = new PlayableCard(1, front, back);
            assertFalse(card.getVisibility());
        }

    @org.junit.jupiter.api.Test
        void toStringTest() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Evaluator.class, new EvaluatorDeserializer())
                    .registerTypeAdapter(Front.class, new FrontDeserializer())
                    .registerTypeAdapter(Back.class, new BackDeserializer())
                    .registerTypeAdapter(Corner.class, new CornerDeserializer())
                    .registerTypeAdapter(PlayableCard.class, new PlayableCardDeserializer())
                    .registerTypeAdapter(GoalCard.class, new GoalCardDeserializer())
                    .create();

            List<PlayableCard> d = new ArrayList<>();

            String resource = "src/main/resources/it/polimi/ingsw/am42/json/resource.json";
            String gold = "src/main/resources/it/polimi/ingsw/am42/json/gold.json";
            String starting = "src/main/resources/it/polimi/ingsw/am42/json/starting.json";

            List<String> sources = new ArrayList<>();
            sources.add(resource);
            sources.add(gold);
            sources.add(starting);

            for(String src : sources) {
                try{
                    FileReader reader = new FileReader(src);
                    JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

                    for(int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        PlayableCard card = gson.fromJson(jsonObject, PlayableCard.class);
                        card.setVisibility(true);
                        d.add(card);
                    }
                } catch (FileNotFoundException e)   {
                    throw new RuntimeException(e);
                }
            }

            for (PlayableCard c : d)
                System.out.println(c);
        }

}