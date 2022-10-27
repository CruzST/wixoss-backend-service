package com.wixossdeckbuilder.backendservice.startup;

import com.wixossdeckbuilder.backendservice.model.*;
import com.wixossdeckbuilder.backendservice.model.entities.Card;
import com.wixossdeckbuilder.backendservice.model.entities.WixossUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class CardLoader {
    public static final Logger logger = LoggerFactory.getLogger(CardLoader.class);

    public static boolean uploadCardsToDB() {
        boolean flag = false;
        try {
            JSONParser jsonParser = new JSONParser();
            // use the Path from content route
            String pathToFile = "src/main/resources/static/cards.json";
            //File cardFileJson = ResourceUtils.getFile(pathToFile);
            JSONObject jsonob = (JSONObject) jsonParser.parse(new FileReader(pathToFile));
            JSONArray array = (JSONArray) jsonob.get("cardData");
            System.out.println("Array Size: " + array.size());
            JSONObject item = (JSONObject) array.get(0);
            System.out.println("Testing with single jsonob");

            System.out.print("Card Name: ");
            System.out.println(item.get("card_name"));
            System.out.println();

            System.out.print("Rarity: ");
            System.out.println(item.get("rarity"));
            System.out.println();

            System.out.print("Card Type: ");
            System.out.println(item.get("card_type"));
            System.out.println();

            System.out.print("Color: ");
            System.out.println(item.get("color"));
            System.out.println();

            System.out.print("Level: ");
            System.out.println(item.get("level"));
            System.out.println();

            System.out.println("Grow Cost: ");
            Map growCost = (Map) item.get("grow_cost");
            Iterator<Map.Entry> itr1 = growCost.entrySet().iterator();
            while (itr1.hasNext()) {
                Map.Entry pair = itr1.next();
                System.out.println(pair.getKey() + ": " + pair.getValue());
            }

            System.out.print("Cost: ");
            System.out.println(item.get("cost"));
            System.out.println();

            System.out.print("Limit: ");
            System.out.println(item.get("limit"));
            System.out.println();

            System.out.print("Power: ");
            System.out.println(item.get("power"));
            System.out.println();

            System.out.print("Team: ");
            System.out.println(item.get("team"));
            System.out.println();

            System.out.print("Effects: ");
            System.out.println(item.get("effects"));
            System.out.println();

            System.out.print("Life Burst: ");
            System.out.println(item.get("life_burst"));
            System.out.println();

            System.out.print("Coin: ");
            System.out.println(item.get("coin"));
            System.out.println();

            System.out.print("Set Format: ");
            System.out.println(item.get("set_format"));
            System.out.println();

            System.out.print("Timing: ");
            System.out.println(item.get("timing"));
            System.out.println();

            System.out.println("Serial: ");
//            Map serial = (Map) item.get("serial");
//            Iterator<Map.Entry> itr2 = serial.entrySet().iterator();
//            while (itr2.hasNext()) {
//                Map.Entry pair = itr2.next();
//                System.out.println(pair.getKey() + ": " + pair.getValue());
//            }
            JSONObject test = (JSONObject) item.get("serial");
            System.out.println(test.get("serialNumber"));
            System.out.println(test.get("formatSet"));
            System.out.println(test.get("cardSet"));
            System.out.println(test.get("cardNumber"));
            System.out.println();



            System.out.println("Image: ");
            Map image = (Map) item.get("image");
            Iterator<Map.Entry> itr3 = image.entrySet().iterator();
            while (itr3.hasNext()) {
                Map.Entry pair = itr3.next();
                System.out.println(pair.getKey() + ": " + pair.getValue());
            }
            System.out.println();



        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    private Card createWixossCardFromJSON(JSONObject cardDataJSON) {
        String cardName = cardDataJSON.get("card_name").toString();

        // TODO: Rarity

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

        ArrayList<ColorCost> cost = null;
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
        JSONObject teamJSON = (JSONObject) cardDataJSON.get("team");
        if (teamJSON != null) {
            team = Team.fromString(teamJSON.toString());
        }

        Ability effects = null;
        JSONObject effectsJSON = (JSONObject) cardDataJSON.get("effects");
        if (effectsJSON != null) {
            effects = new Ability(true, (String[]) effectsJSON.get("ability"));
        }

        Ability lifeBurst = null;
        JSONObject lifeBurstJSON = (JSONObject) cardDataJSON.get("life_burst");
        if (lifeBurstJSON != null) {
            lifeBurst = new Ability(false, (String[]) lifeBurstJSON.get("ability"));
        }

        String coin = cardDataJSON.get("coin").toString();

        String setFormat = cardDataJSON.toString();
        JSONObject serialJSON = (JSONObject) cardDataJSON.get("serial");
        Serial serial = new Serial(
                serialJSON.get("serialNumber").toString(),
                serialJSON.get("formatSet").toString(),
                CardSet.matchEnum(serialJSON.get("cardSet").toString()),
                Integer.parseInt(serialJSON.get("cardNumber").toString()),
                CardSet.getAsString(serialJSON.get("cardSet").toString()));

        // TODO: Return the card object
        return null;
    }

    public static void main(String[] args) {
        uploadCardsToDB();
    }
}
