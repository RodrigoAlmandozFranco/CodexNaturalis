package it.polimi.ingsw.am42.model.cards.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.gson.*;
import it.polimi.ingsw.am42.model.decks.PlayableDeck;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * This class tests the GoalCard
 * @see GoalCard
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

class GoalCardTest {

    @org.junit.jupiter.api.Test
    void getEvaluator() {
            GoalCard goalCard = new GoalCard(1, null, null);
            assertNull(goalCard.getEvaluator());
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


        List<GoalCard> goals = new ArrayList<>();


        String src = "src/main/resources/it/polimi/ingsw/am42/json/goal.json";

        try {
            FileReader reader = new FileReader(src);
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            for(int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                GoalCard card = gson.fromJson(jsonObject, GoalCard.class);
                goals.add(card);
            }
        } catch (FileNotFoundException e)   {
            throw new RuntimeException(e);
        }

        for (GoalCard goal : goals)
            System.out.println(goal);
    }
}