package com.g2.t5;

import org.apache.commons.csv.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.g2.Model.Game;

public class GameDataWriter {
    private final HttpClient httpClient = HttpClientBuilder.create().build();

    private static String CSV_FILE_PATH = "AUTName/StudentLogin/";
    private static String CSV_FILE_NAME = "/GameData.csv";

    // AGGIUNTA IN 2.0
    public long getGameId() {
        long gameId = -1;

        try {
            Reader reader = new FileReader(CSV_FILE_PATH);
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            List<CSVRecord> records = csvParser.getRecords();

            if (records.size() > 1) {
                CSVRecord lastRecord = records.get(records.size() - 1);
                gameId = Long.parseLong(lastRecord.get(0));
            }

            csvParser.close();
            reader.close();
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del file CSV");
            e.printStackTrace();
        }

        return gameId;
    }

    // MODIFICATA IN 2.0
    public JSONObject saveGame(Game game) {
        try {
            String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
            JSONObject obj = new JSONObject();

            obj.put("difficulty", game.getDifficulty());
            obj.put("name", game.getName());
            obj.put("description", game.getDescription());
            obj.put("startedAt", time);

            JSONArray playersArray = new JSONArray();
            playersArray.put(String.valueOf(game.getPlayerId()));

            obj.put("players", playersArray);

            HttpPost httpPost = new HttpPost("http://t4-g18-app-1:3000/games");
            StringEntity jsonEntity = new StringEntity(obj.toString(), ContentType.APPLICATION_JSON);

            httpPost.setEntity(jsonEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode > 299) {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
                return null;
            }

            HttpEntity responseEntity = httpResponse.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);
            JSONObject responseObj = new JSONObject(responseBody);

            Long gameID = responseObj.getLong("id");

            JSONObject round = new JSONObject();
            round.put("gameId", gameID);
            round.put("testClassId", game.getTestedClass());
            round.put("startedAt", time);

            httpPost = new HttpPost("http://t4-g18-app-1:3000/rounds");
            jsonEntity = new StringEntity(round.toString(), ContentType.APPLICATION_JSON);

            httpPost.setEntity(jsonEntity);

            httpResponse = httpClient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode > 299) {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
                return null;
            }

            responseEntity = httpResponse.getEntity();
            responseBody = EntityUtils.toString(responseEntity);
            responseObj = new JSONObject(responseBody);

            // salvo il round id che l'Api mi restituisce
            Integer roundID = responseObj.getInt("id");

            JSONObject turn = new JSONObject();

            turn.put("players", playersArray);
            turn.put("roundId", roundID);
            turn.put("startedAt", time);

            httpPost = new HttpPost("http://t4-g18-app-1:3000/turns");
            jsonEntity = new StringEntity(turn.toString(), ContentType.APPLICATION_JSON);

            httpPost.setEntity(jsonEntity);

            httpResponse = httpClient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode > 299) {
                System.err.println(EntityUtils.toString(httpResponse.getEntity()));
                return null;
            }

            responseEntity = httpResponse.getEntity();
            responseBody = EntityUtils.toString(responseEntity);

            JSONArray responseArrayObj = new JSONArray(responseBody);

            // salvo il turn id che l'Api mi restituisce
            Integer turnID = responseArrayObj.getJSONObject(0).getInt("id");

            JSONObject resp = new JSONObject();
            resp.put("game_id", gameID);
            resp.put("round_id", roundID);
            resp.put("turn_id", turnID);

            return resp;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveGameCSV(Game game, int tid) {
        long pid = game.getPlayerId();
        long gid = game.getId();
        int rid = game.getRound();

        String playerID = "Player" + pid;
        String gameID = "Game" + gid;
        String roundID = "Round" + rid;
        String turnID = "Turn" + tid;
        
        // Al path bisogna aggiungere PlayerID/GameID/RoundID/TurnID e poi il nome del file
        String fileName = CSV_FILE_PATH + playerID + "/" + gameID + "/" + roundID + "/" + turnID + CSV_FILE_NAME;

        Path path = Paths.get(fileName);
 
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                System.out.println("Errore durante la creazione della directory.");
                e.printStackTrace();
            }
        }

        try {
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL);

            csvPrinter.printRecord(
                    "GameID",
                    "Name",
                    "Round",
                    "Class",
                    "Description",
                    "Difficulty",
                    "CreatedAt",
                    "UpdatedAt",
                    "StartedAt",
                    "ClosedAt",
                    "PlayerID",
                    "Robot"
                );

            csvPrinter.printRecord(
                game.getId(),
                game.getName(),
                game.getRound(),
                game.getTestedClass(),
                game.getDescription(),
                game.getDifficulty(),
                game.getCreatedAt(),
                game.getUpdateAt(),
                game.getStartedAt(),
                game.getClosedAt(),
                game.getPlayerId(),
                game.getRobot()      
            );

            csvPrinter.flush();
            csvPrinter.close();
            writer.close();

            System.out.println(gameID + " " + turnID + " salvato correttamente in File System.");

            return true;
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura del file CSV.");
            e.printStackTrace();

            return false;
        }
    }

    public boolean updateGameCSV(Game game, int tid) {
        long pid = game.getPlayerId();
        long gid = game.getId();
        int rid = game.getRound();

        String playerID = "Player" + pid;
        String gameID = "Game" + gid;
        String roundID = "Round" + rid;
        String turnID = "Turn" + tid;

        String fileName = CSV_FILE_PATH + playerID + "/" + gameID + "/" + roundID + "/" + turnID + CSV_FILE_NAME;

        Path path = Paths.get(fileName);
 
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                System.out.println("Errore durante la creazione della directory.");
                e.printStackTrace();
            }
        }

        try {
            File file = new File(fileName);

            FileWriter writer = new FileWriter(file);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL);

            csvPrinter.printRecord(
                    "GameID",
                    "Name",
                    "Round",
                    "Class",
                    "Description",
                    "Difficulty",
                    "CreatedAt",
                    "UpdatedAt",
                    "StartedAt",
                    "ClosedAt",
                    "PlayerID",
                    "Robot"
                );

            csvPrinter.printRecord(
                game.getId(),
                game.getName(),
                game.getRound(),
                game.getTestedClass(),
                game.getDescription(),
                game.getDifficulty(),
                game.getCreatedAt(),
                game.getUpdateAt(),
                game.getStartedAt(),
                game.getClosedAt(),
                game.getPlayerId(),
                game.getRobot()      
            );

            csvPrinter.flush();
            csvPrinter.close();
            writer.close();

            System.out.println(gameID + " " + turnID + " aggiornato correttamente in File System.");

            return createNextTurnCSV(game, tid + 1);
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura del file CSV.");
            e.printStackTrace();

            return false;
        }
    }

    private boolean createNextTurnCSV(Game game, int nextTurn) {
        return saveGameCSV(game, nextTurn);
    }
    // FINE MODIFICHE 2.0
}