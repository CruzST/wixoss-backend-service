package com.wixossdeckbuilder.backendservice.startup.loaders;

import com.wixossdeckbuilder.backendservice.model.dto.Ability;
import com.wixossdeckbuilder.backendservice.model.dto.ColorCost;
import com.wixossdeckbuilder.backendservice.model.dto.Image;
import com.wixossdeckbuilder.backendservice.model.dto.Serial;
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
import java.util.Arrays;
import java.util.List;

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

        List<Rarity> rarity = getRaritiesAsArray(cardDataJSON.get("rarity").toString());

        CardType cardType = CardType.fromString(cardDataJSON.get("card_type").toString());

        LrigTypeOrClass lrigTypeOrClass = LrigTypeOrClass.fromString(cardDataJSON.get("lrig_type_or_class").toString());

        ArrayList<Colors> colors = new ArrayList<>();
        JSONArray colorsJSON = (JSONArray) cardDataJSON.get("color");
        for (int i = 0; i < colorsJSON.size(); i++ ){
            colors.add(Colors.matchEnum(colorsJSON.get(i).toString()));
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
            cost = new ArrayList<>();
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
                serialJSON.get("formatSet").toString(),
                CardSet.matchEnum(serialJSON.get("cardSet").toString()),
                serialJSON.get("cardNumber").toString(),
                CardSet.getAsString(serialJSON.get("cardSet").toString()));
        String id = serialJSON.get("serialNumber").toString();

        Image image = null;

        String timing = cardDataJSON.get("timing") != null ? (String) cardDataJSON.get("timing") : null;



        CardRequest newCardRequest = new CardRequest(
                id,
                cardName,
                rarity,
                cardType,
                lrigTypeOrClass,
                colors,
                level,
                growCost,
                cost,
                limit,
                power,
                team,
                effects,
                lifeBurst,
                coin,
                setFormat,
                serial,
                timing
        );
        return newCardRequest;
    }

    // Certain cards ahve rarities that are in the form X_X_(P)
    private List<Rarity> splitRaritiesThatEndInP(String input) {
        List<Rarity> rarityArrayList = new ArrayList<>();
        String inputNoPromo = input.replace("(P)", "");
        // if string size is 4
        String rarity = null;
        if (inputNoPromo.length() == 4) {
            rarity = inputNoPromo.substring(0, 2);
        } else {
            // else it is 2
            rarity = inputNoPromo.substring(0, 1);
        }
        String rarityPromo = rarity.concat("(P)");
        rarityArrayList.add(Rarity.fromString(rarity));
        rarityArrayList.add(Rarity.fromString(rarityPromo));
        return rarityArrayList;
    }

    private List<Rarity> splitRaritiesThatEndInDiR(String input) {
        List<Rarity> rarityArrayList = new ArrayList<>();
        //pop off the dir
        rarityArrayList.add(Rarity.DIVA_RARE);
        String withoutDir = input.replace("DiR", "");
        // check the length, if greater than 2 there is an SCR
        if (withoutDir.length() > 2 && withoutDir.endsWith("SCR")) {
            rarityArrayList.add(Rarity.SECRET_RARE);
            String remainder = withoutDir.replace("SCR", "");
            rarityArrayList.add(Rarity.fromString(remainder));
        } else {
            rarityArrayList.add(Rarity.fromString(withoutDir));
        }
        return rarityArrayList;
    }

    private List<Rarity> splitRaritiesThatEndInSCR(String input) {
        List<Rarity> rarityArrayList = new ArrayList<>();
        rarityArrayList.add(Rarity.SECRET_RARE);
        String withoutSCR = input.replace("SCR", "");
        rarityArrayList.add(Rarity.fromString(withoutSCR));
        return rarityArrayList;
    }

    private List<Rarity> splitRaritiesThatEndInPR() {
        List<Rarity> rarityList = Arrays.asList(Rarity.STRUCTURE_DECK, Rarity.PROMO);
        return rarityList;
    }

    private List<Rarity> splitRaritiesThatEndInPrime() {
        // RR(P)R(P')
        List<Rarity> rarityList = Arrays.asList(Rarity.RARE, Rarity.RARE_PROMO, Rarity.RARE_PROMO_PRIME);
        return rarityList;
    }



    private List<Rarity> getRaritiesAsArray(String array) {
        List<Rarity> returnList = new ArrayList<>();

        /*
        // Very Special Cases, belongs in the IF block below
        if (array.equals("PRPR(P)")) {
            returnList.add(Rarity.PROMO);
            returnList.add(Rarity.PROMO_PRIME);
        } else if (array.equals("LL(P)")) {
            returnList.add(Rarity.LRIG);
            returnList.add(Rarity.LRIG_PROMO);
        } else if (array.equals("STST(P)")) {
            returnList.add(Rarity.STRUCTURE_DECK);
            returnList.add(Rarity.STRUCTURE_DECK_PROMO);
        } else if (array.equals("CC(P)")) {
            returnList.add(Rarity.COMMON);
            returnList.add(Rarity.COMMON_PROMO);
        } else if (array.equals("RR(P)")) {
            returnList.add(Rarity.RARE);
            returnList.add(Rarity.RARE_PROMO);
        } else if (array.equals("STDiR")) {
            returnList.add(Rarity.STRUCTURE_DECK);
            returnList.add(Rarity.DIVA_RARE);
        } else if (array.equals("PIDiR")) {
            returnList.add(Rarity.PIECE);
            returnList.add(Rarity.DIVA_RARE);
        } else if (array.equals("LSCRDiR")) {
            returnList.add(Rarity.LRIG);
            returnList.add(Rarity.SECRET_RARE);
            returnList.add(Rarity.DIVA_RARE);
        } else if (array.equals("LSCR")) {
            returnList.add(Rarity.LRIG);
            returnList.add(Rarity.SECRET_RARE);
        } else if (array.equals("STSCR")) {
            returnList.add(Rarity.STRUCTURE_DECK);
            returnList.add(Rarity.SECRET_RARE);
        }

        // STPR
        // RR(P)R(P')
         */

        // Create an array using found delimiter
        // If no delimiter found, just add it to the returnList
        String delimiter = getDelimiter(array);
        if (delimiter == null) {
            if (array.endsWith("(P)")) {
                returnList = splitRaritiesThatEndInP(array);
            } else if (array.endsWith("DiR")) {
                returnList = splitRaritiesThatEndInDiR(array);
            } else if (array.endsWith("SCR")) {
                returnList = splitRaritiesThatEndInSCR(array);
            } else if (array.equals("STPR")) {
                returnList = splitRaritiesThatEndInPR();
            } else if (array.equals("RR(P)R(P')")) {
                returnList = splitRaritiesThatEndInPrime();
            }
            else {
                returnList.add(Rarity.fromString(array));
            }
        } else {
            String[] rarities = array.split(delimiter);
            for (String rarity: rarities) {
                returnList.add(Rarity.fromString(rarity));
            }
        }
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
