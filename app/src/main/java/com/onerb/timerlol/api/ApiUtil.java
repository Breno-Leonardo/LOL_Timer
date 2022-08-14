package com.onerb.timerlol.api;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onerb.timerlol.ui.main.MainViewModel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class ApiUtil extends AsyncTask<Void, Void, String> {
    public static final int CODE_SUMMONER_ID = 1;
    public static final int CODE_PARTICIPANTS = 2;

    private MainViewModel viewModel;
    private String summonerId = null;
    private String summonerName = null;
    private String name = "mainmidbr";
    private String keyApi = "RGAPI-055128c3-7447-4bf0-a0be-ab1f69624c66";

    public ApiUtil(MainViewModel viewModel, String summonerName) {
        this.viewModel = viewModel;

        this.summonerName = summonerName;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    private StringBuilder respSummonerId;
    private StringBuilder respParticipants;

    @Override
    protected String doInBackground(Void... voids) {
        if (summonerId == null)
            summonerId = getSummonerId();
        return getPaticipants();
//        return getPaticipants();

    }

    public String getSummonerId() {

        name = summonerName.toLowerCase();
        name = summonerName.replace(" ", "");
        respSummonerId = new StringBuilder();

        try {
            URL url = new URL("https://br1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + name);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setRequestProperty("X-Riot-Token", keyApi);
            conexao.setRequestProperty("Content-type", "application-json");
            conexao.setDoInput(true);
            conexao.setConnectTimeout(5000);
            conexao.connect();
            int responseCode = conexao.getResponseCode();
            System.out.println("ApiUtil.getSummonerId code conexao:" + responseCode);
            Scanner scanner = new Scanner(conexao.getInputStream());
            while (scanner.hasNext()) {
                respSummonerId.append(scanner.next());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (respSummonerId.length() != 0) {
            Gson gsonResp = new GsonBuilder().setPrettyPrinting().create();
            SummonerUser summonerUser = gsonResp.fromJson(respSummonerId.toString(), SummonerUser.class);
            return summonerUser.getId();
        } else
            return null;

    }

    public String getPaticipants() {

        respParticipants = new StringBuilder();

        try {
            URL url = new URL("https://br1.api.riotgames.com/lol/spectator/v4/active-games/by-summoner/" + summonerId);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setRequestProperty("Content-type", "application-json");
            conexao.setRequestProperty("X-Riot-Token", keyApi);
//            conexao.setDoOutput(true);
            conexao.setDoInput(true);
            conexao.setConnectTimeout(5000);
            conexao.connect();
            Scanner scanner = new Scanner(conexao.getInputStream());
            while (scanner.hasNext()) {
                respParticipants.append(scanner.next());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (respParticipants.length() != 0) {
            Gson gsonResp = new GsonBuilder().setPrettyPrinting().create();
            SummonerInfos.Participants[] participants = gsonResp.fromJson(respParticipants.toString(), SummonerInfos.class).getParticipants();
            for (int i = 0; i < participants.length; i++) {
                System.out.println("TimerActivity.onCreate paticipant " + i + " names:" + participants[i].getSummonerName());
            }
            for (int i = 0; i < participants.length; i++) {
                System.out.println("TimerActivity.onCreate paticipant " + i + " perks:" + participants[i].getPerks().perkIds.toString());
            }

        } else
            return "null";

        return "null";
    }

}
