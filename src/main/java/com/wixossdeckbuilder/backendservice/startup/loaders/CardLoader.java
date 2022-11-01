package com.wixossdeckbuilder.backendservice.startup.loaders;

import com.wixossdeckbuilder.backendservice.model.*;
import com.wixossdeckbuilder.backendservice.model.enums.*;
import com.wixossdeckbuilder.backendservice.model.payloads.CardRequest;
import com.wixossdeckbuilder.backendservice.service.CardService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.util.ArrayList;

@Component
public class CardLoader {
    public static final Logger logger = LoggerFactory.getLogger(CardLoader.class);

    @Autowired
    private CardService cardService;

    @Value("${cardsJSONLink}")
    private String linkToCardsJSON; // use the Path from content route

    public void uploadCardsToDB() {
        boolean flag = false;
            try {
            JSONParser jsonParser = new JSONParser();
            String pathToFile = linkToCardsJSON;
            JSONObject jsonob = (JSONObject) jsonParser.parse(new FileReader(pathToFile));  // Get the file as an jsonob
            JSONArray array = (JSONArray) jsonob.get("cardData");
            System.out.println("Array Size from json file: " + array.size());

            for (int i = 0; i < array.size(); i++) {
            //for (int i = 0; i < 5; i++) {
                JSONObject cardToProcess = (JSONObject) array.get(i);
                cardService.createNewCard(createWixossCardFromJSON(cardToProcess)); // update this to have a saveAll method
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CardRequest createWixossCardFromJSON(JSONObject cardDataJSON) {
        String cardName = cardDataJSON.get("card_name").toString();

        ArrayList<Rarity> rarity = getRaritiesAsArray(cardDataJSON.get("rarity").toString());

        CardType cardType = CardType.fromString(cardDataJSON.get("card_type").toString());

        LrigTypeOrClass lrigTypeOrClass = LrigTypeOrClass.fromString(cardDataJSON.get("lrig_type_or_class").toString());

        ArrayList<Colors> colors = new ArrayList<>();
        Object colorsJSON =cardDataJSON.get("color");
        if (colorsJSON.getClass().isArray()) {
            JSONArray colorsArray = (JSONArray) colorsJSON;
            for (int i = 0; i < colorsArray.size(); i++ ){
                colors.add(Colors.matchEnum(colorsArray.get(i).toString()));
            }
        } else {
            colors.add(Colors.matchEnum(colorsJSON.toString()));
        }

        int level = Integer.valueOf(cardDataJSON.get("level").toString());

        ColorCost growCost = null;
        JSONObject growCostJSON = (JSONObject) cardDataJSON.get("grow_cost");
        if (growCostJSON != null) {
            Colors growColor = Colors.matchEnum(growCostJSON.get("color").toString());
            int growAmount = Integer.valueOf(growCostJSON.get("amount").toString());
            growCost = new ColorCost(growColor, growAmount);
        }

        ArrayList<ColorCost> cost = new ArrayList<>();
        JSONArray costJSON = (JSONArray) cardDataJSON.get("cost");
        if (costJSON != null) {
            for (Object obj : costJSON) {
                JSONObject objAsJSONObj = (JSONObject) obj;
                ColorCost colorCost = new ColorCost(
                        Colors.matchEnum(objAsJSONObj.get("color").toString()),
                        Integer.parseInt(objAsJSONObj.get("amount").toString())
                );
                cost.add(colorCost);
            }
        }

        String limit = cardDataJSON.get("limit").toString();

        int power = Integer.parseInt(cardDataJSON.get("power").toString());

        Team team = null;
        String teamJSON = (String) cardDataJSON.get("team");
        if (teamJSON != null) {
            team = Team.fromString(teamJSON);
        }

        Ability effects = null;
        JSONObject effectsJSON = (JSONObject) cardDataJSON.get("effects");
        if (effectsJSON != null) {
            //JSONArray effectsArr = (JSONArray) effectsJSON.get("ability");

            effects = new Ability(true, (ArrayList<String>) effectsJSON.get("ability"));
        }

        Ability lifeBurst = null;
        JSONObject lifeBurstJSON = (JSONObject) cardDataJSON.get("life_burst");
        if (lifeBurstJSON != null) {
            lifeBurst = new Ability(false, (ArrayList<String>) lifeBurstJSON.get("ability"));
        }

        String coin = cardDataJSON.get("coin") != null ? (String) cardDataJSON.get("coin") : null;

        String setFormat = cardDataJSON.get("set_format") != null ? (String) cardDataJSON.get("set_format") : null;

        JSONObject serialJSON = (JSONObject) cardDataJSON.get("serial");
        Serial serial = new Serial(
                serialJSON.get("serialNumber").toString(),
                serialJSON.get("formatSet").toString(),
                CardSet.matchEnum(serialJSON.get("cardSet").toString()),
                serialJSON.get("cardNumber").toString(),
                CardSet.getAsString(serialJSON.get("cardSet").toString()));

        Image image = null;

        String timing = cardDataJSON.get("timing") != null ? (String) cardDataJSON.get("timing") : null;

        CardRequest newCardRequest = new CardRequest(
                cardName, rarity,
                cardType, lrigTypeOrClass,
                colors, level,
                growCost, cost,
                limit, power,
                team, effects,
                lifeBurst, coin,
                setFormat, serial,
                timing

        );
        return newCardRequest;
    }

    static ArrayList<Rarity> getRaritiesAsArray(String array) {
        ArrayList<Rarity> returnList = new ArrayList<>();
        // Check what kind of delimiter it is if any
        String delimiter = getDelimiter(array);

        if (delimiter == null) {
            returnList.add(Rarity.fromString(array));
        } else {
            String[] rarities = array.split(delimiter);
            for (String rarity: rarities) {
                returnList.add(Rarity.fromString(rarity));
            }
        }
        // Create an array using found delimiter
        // If no delimiter found, just add it to the returnList
        return returnList;
    }

    static String getDelimiter(String array) {
        String comma = ",";
        String spaceSlashSpace = " / ";
        String slash = "/";
        String space = " ";
        String[] delimiters = new String[]{comma, spaceSlashSpace, slash, space};

        for (String delim: delimiters) {
            if (array.contains(delim)) {
                return delim;
            }
        }
        return null;
    }


/** For testing **/
//    public static void main(String[] args) {
//        uploadCardsToDB();
//    }
}
