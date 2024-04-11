package it.polimi.ingsw.am42.gson.gameGson;

import com.google.gson.*;
import it.polimi.ingsw.am42.gson.backGson.BackDeserializer;
import it.polimi.ingsw.am42.gson.cornerGson.CornerDeserializer;
import it.polimi.ingsw.am42.gson.evaluatorGson.EvaluatorDeserializer;
import it.polimi.ingsw.am42.gson.frontGson.FrontDeserializer;
import it.polimi.ingsw.am42.gson.goalCardGson.GoalCardDeserializer;
import it.polimi.ingsw.am42.gson.playableCardGson.PlayableCardDeserializer;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.cards.types.*;
import it.polimi.ingsw.am42.model.decks.GoalDeck;
import it.polimi.ingsw.am42.model.decks.PlayableDeck;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.evaluator.Evaluator;
import it.polimi.ingsw.am42.model.structure.Board;
import it.polimi.ingsw.am42.model.structure.Position;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the deserialization from json to java.
 * It is the core component for persistence.
 *
 * @author Rodrigo Almandoz Franco
 */

public class GameDeserializer implements JsonDeserializer<Game> {

    private PlayableDeck resourceDeck;
    private PlayableDeck goldDeck;
    private PlayableDeck startingDeck;
    private GoalDeck goalDeck;

    private List<Player> players;
    private List<GoalCard> globalGoals;

    private List<PlayableCard> pickableResourceCards;
    private List<PlayableCard> pickableGoldCards;

    private Player currentPlayer;
    int numberPlayers;


    /**
     * This is the constructor for the deserialization process.
     * @param json The json is passed by parameter.
     * @param typeOfT It allows GSON to know the object to deserialize to.
     * @param context It defines methods for deserializing the json element.
     *                It is defined in the interface the class implements.
     * @return It returns the Game after its deserialization. It is a new instance.
     */
    public Game deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {

        JsonObject jsonObject = json.getAsJsonObject();

        resourceDeck = new PlayableDeck();
        goldDeck = new PlayableDeck();
        startingDeck = new PlayableDeck();
        goalDeck = new GoalDeck();
        players = new ArrayList<>();
        globalGoals = new ArrayList<>();
        pickableResourceCards = new ArrayList<>();
        pickableGoldCards = new ArrayList<>();

        initializeDecks();

        initializePlayers(jsonObject.get("players").getAsJsonArray());

        globalGoals = setGlobalGoals(jsonObject.get("globalGoals").getAsJsonArray());
        pickableResourceCards = setPickable(jsonObject.get("pickableResourceCards").getAsJsonArray(), "resource");
        pickableGoldCards = setPickable(jsonObject.get("pickableGoldCards").getAsJsonArray(), "gold");

        resourceDeck = setPlayableDeck(jsonObject.get("resourceDeck").getAsJsonArray(), "resource");
        goldDeck = setPlayableDeck(jsonObject.get("resourceDeck").getAsJsonArray(), "gold");
        startingDeck = setPlayableDeck(jsonObject.get("resourceDeck").getAsJsonArray(), "starting");
        goalDeck = setGoalDeck(jsonObject.get("goalDeck").getAsJsonArray());

        currentPlayer = getPlayer(jsonObject.get("currentPlayer").getAsString());
        numberPlayers = jsonObject.get("numberPlayers").getAsInt();


        return new Game(numberPlayers, players, globalGoals, resourceDeck, goldDeck,
                startingDeck, goalDeck, pickableResourceCards, pickableGoldCards, currentPlayer);
    }


    /**
     * This private method deserializes all the players inside the json.
     * It takes care of all the details of each Player.
     * At the end of the method, all the players are created,
     * and they are now in the players arraylist
     * @param p This is the JsonArray which each array element contains all the information about that player
     */
    private void initializePlayers(JsonArray p) {
        for(int i = 0; i < p.size(); i++) {
            JsonObject jsonObject = p.get(i).getAsJsonObject();
            String nickname = jsonObject.get("nickname").getAsString();
            int points = jsonObject.get("points").getAsInt();
            List<PlayableCard> handPlayer = getHand(jsonObject.get("hand").getAsJsonArray());
            Color color = Color.valueOf(jsonObject.get("color").getAsString());
            Board board = getBoard(jsonObject.get("board").getAsJsonArray());

            GoalCard personalGoal = getPersonalGoal(jsonObject.get("personalGoal").getAsInt());

            int numberGoals = jsonObject.get("numberGoalsAchieved").getAsInt();
            Player player = new Player(nickname, points, handPlayer, color, board, personalGoal, numberGoals);
            players.add(player);
        }
    }


    /**
     * This method initializes the decks without being shuffled.
     */
    private void initializeDecks() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Evaluator.class, new EvaluatorDeserializer())
                .registerTypeAdapter(Front.class, new FrontDeserializer())
                .registerTypeAdapter(Back.class, new BackDeserializer())
                .registerTypeAdapter(Corner.class, new CornerDeserializer())
                .registerTypeAdapter(PlayableCard.class, new PlayableCardDeserializer())
                .registerTypeAdapter(GoalCard.class, new GoalCardDeserializer())
                .create();

        PlayableDeck d;

        String resource = "src/main/resources/it/polimi/ingsw/am42/json/resource.json";
        String gold = "src/main/resources/it/polimi/ingsw/am42/json/gold.json";
        String starting = "src/main/resources/it/polimi/ingsw/am42/json/starting.json";

        List<String> sources = new ArrayList<>();
        sources.add(resource);
        sources.add(gold);
        sources.add(starting);

        for(String src : sources) {
            try{
                if(src.contains("resource.json")) d = resourceDeck;
                else if(src.contains(("gold"))) d = goldDeck;
                else d = startingDeck;


                FileReader reader = new FileReader(src);
                JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

                for(int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    PlayableCard card = gson.fromJson(jsonObject, PlayableCard.class);
                    d.addCard(card);
                }
            } catch (FileNotFoundException e)   {
                throw new RuntimeException(e);
            }
        }

        String goal = "src/main/resources/it/polimi/ingsw/am42/json/goal.json";

        try {
            FileReader reader = new FileReader(goal);
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            for(int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                GoalCard card = gson.fromJson(jsonObject, GoalCard.class);
                goalDeck.addCard(card);
            }
        } catch (FileNotFoundException e)   {
            throw new RuntimeException(e);
        }
    }


    /**
     * This method allows to recognize who is the current player.
     * @param nickname The nickname of the current player.
     *                 This information is contained in the JsonElement passed to the public method deserialize.
     * @return The Player that has to play right now.
     */
    private Player getPlayer(String nickname) {
        for(Player player : players) {
            if(player.getNickname().equals(nickname)) {
                return player;
            }
        }
        return null;
    }


    /**
     * This method allows to recognize the personal goal of a player.
     * @param id The id of the personal goal.
     * @return The GoalCard that is the personal goal of a player.
     */
    private GoalCard getPersonalGoal(int id) {
        if(id != -1) {
            for(GoalCard card : goalDeck) {
                if(card.getId() == id) {
                    return card;
                }
            }
        }
        return null;
    }


    /**
     * This method allows to recognize the board of a player.
     * @param b The JsonArray that contains all the information about the board of a player.
     * @return The Board of a player.
     */
    private Board getBoard(JsonArray b) {
        Board board = new Board();
        for(int i = 0; i < b.size(); i++) {
            JsonObject jsonObject = b.get(i).getAsJsonObject();
            String srcImage = jsonObject.get("srcImage").getAsString();

            Position pos = new Position(jsonObject.get("x").getAsInt(), jsonObject.get("y").getAsInt());

            PlayableDeck deck;

            if(srcImage.contains("resource.json")) deck = resourceDeck;
            else if(srcImage.contains("gold")) deck = goldDeck;
            else deck = startingDeck;

            //todo: check for possible changes in startingCard.

            for(PlayableCard card : deck) {
                if(card.getFront().getSrcImage().equals(srcImage)){
                    card.getFront().setPosition(pos);
                    board.addFace(card.getFront());
                } else if(card.getBack().getSrcImage().equals(srcImage)){
                    card.getBack().setPosition(pos);
                    board.addFace(card.getBack());
                }
            }
        }
        return board;
    }


    /**
     * This method allows to recognize the hand of a player.
     * @param hand The JsonArray that contains all the information about the hand of a player.
     * @return The hand of a player.
     */
    private List<PlayableCard> getHand(JsonArray hand){
        List<PlayableCard> handPlayer = new ArrayList<>();
        for(int i = 0; i < hand.size(); i++) {
            JsonObject jsonObject = hand.get(i).getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();

            if(id <= 40) {
                for(PlayableCard card : resourceDeck) {
                    if (card.getId() == id) {
                        handPlayer.add(card);
                    }
                }
            } else if(id <= 80) {
                for(PlayableCard card : goldDeck) {
                    if (card.getId() == id) {
                        handPlayer.add(card);
                    }
                }
            } else if(id <= 86) {
                for(PlayableCard card : startingDeck) {
                    if (card.getId() == id) {
                        handPlayer.add(card);
                    }
                }
            }
        }
        return handPlayer;
    }


    /**
     * This method allows to recognize the global goals of the game.
     * @param globalGoals The JsonArray that contains all the information about the global goals of the game.
     * @return The global goals of the game.
     */
    private List<GoalCard> setGlobalGoals(JsonArray globalGoals) {
        List <GoalCard> goals = new ArrayList<>();
        for(int i = 0; i < globalGoals.size(); i++) {
            JsonObject jsonObject = globalGoals.get(i).getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();
            for(GoalCard card : goalDeck) {
                if(card.getId() == id) {
                    goals.add(card);
                }
            }
        }
        return goals;
    }


    /**
     * This method allows to reconstruct the playable decks of the game.
     * @param json The JsonArray that contains all the information about the deck.
     * @param str The string that allows to recognize which deck is being reconstructed.
     * @return The PlayableDeck reconstructed.
     */
    private PlayableDeck setPlayableDeck(JsonArray json, String str){
        List<Integer> ids = new ArrayList<>();
        PlayableDeck result = new PlayableDeck();

        for(int i = 0; i < json.size(); i++) {
            JsonObject jsonObject = json.get(i).getAsJsonObject();
            ids.add(jsonObject.get("id").getAsInt());
        }

        PlayableDeck deck;
        if(str.equals("resource")) deck = resourceDeck;
        else if(str.equals("gold")) deck = goldDeck;
        else deck = startingDeck;

        for(int i : ids) {
            for(PlayableCard card : deck) {
                if(card.getId() == i){
                    result.addCard(card);
                }
            }
        }
        return result;
    }


    /**
     * This method allows to reconstruct the goal deck of the game.
     * @param deck The JsonArray that contains all the information about the goal deck.
     * @return The GoalDeck reconstructed.
     */
    private GoalDeck setGoalDeck(JsonArray deck) {
        List<Integer> ids = new ArrayList<>();
        GoalDeck d = new GoalDeck();

        for(int i = 0; i < deck.size(); i++) {
            JsonObject jsonObject = deck.get(i).getAsJsonObject();
            ids.add(jsonObject.get("id").getAsInt());
        }

        for(int i : ids) {
            for(GoalCard card : goalDeck) {
                if(card.getId() == i){
                    d.addCard(card);
                }
            }
        }
        return d;
    }


    /**
     * This method allows to reconstruct the pickable cards of the game.
     * @param json The JsonArray that contains all the information about the pickable cards.
     * @param str The string that allows to recognize which pickableCards are being reconstructed (gold or resource).
     * @return The List of PlayableCard of a single type reconstructed.
     */
    private List<PlayableCard> setPickable(JsonArray json, String str) {

        List<PlayableCard> result = new ArrayList<>();
        PlayableDeck deck;

        if(str.equals("resource")) deck = resourceDeck;
        else deck = goldDeck;

        for(int i = 0; i < json.size(); i++) {
            JsonObject jsonObject = json.get(i).getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();
            for(PlayableCard card : deck) {
                if(card.getId() == id) {
                    result.add(card);
                }
            }
        }
        return result;
    }
}



