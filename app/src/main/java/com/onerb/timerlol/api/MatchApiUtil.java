package com.onerb.timerlol.api;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onerb.timerlol.BuildConfig;
import com.onerb.timerlol.ui.main.MainViewModel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MatchApiUtil extends AsyncTask<Void, Void, String> {
    public final static String BRAZIL = "Brazil";
    public final static String EUNE = "Europe Nordic & East";
    public final static String EUW1 = "Europe West";
    public final static String JP1 = "Japan";
    public final static String KR = "Korea";
    public final static String LA1 = "Latin America North";
    public final static String LA2 = "Latin America South";
    public final static String NA1 = "North America";
    public final static String OC1 = "Oceania";
    public final static String TR1 = "Turkey";
    public final static String RU = "Russia";

    public final static String REGIONS[] = {BRAZIL, EUNE, EUW1, JP1, KR, LA1, LA2, NA1, OC1, TR1, RU};

    public final static String BRAZIL_ROUTE = "br1.api.riotgames.com";
    public final static String EUNE_ROUTE = "eun1.api.riotgames.com";
    public final static String EUW1_ROUTE = "euw1.api.riotgames.com";
    public final static String JP1_ROUTE = "jp1.api.riotgames.com";
    public final static String KR_ROUTE = "kr.api.riotgames.com";
    public final static String LA1_ROUTE = "la1.api.riotgames.com";
    public final static String LA2_ROUTE = "la2.api.riotgames.com";
    public final static String NA1_ROUTE = "na1.api.riotgames.com";
    public final static String OC1_ROUTE = "oc1.api.riotgames.com";
    public final static String TR1_ROUTE = "tr1.api.riotgames.com";
    public final static String RU_ROUTE = "ru.api.riotgames.com";

    public final static String REGIONS_ROUTES[] = {BRAZIL_ROUTE, EUNE_ROUTE, EUW1_ROUTE, JP1_ROUTE, KR_ROUTE, LA1_ROUTE, LA2_ROUTE, NA1_ROUTE, OC1_ROUTE, TR1_ROUTE, RU_ROUTE};

    public static final int CODE_SUMMONER_ID = 1;
    public static final int CODE_PARTICIPANTS = 2;

    private MainViewModel viewModel;
    private SummonerInfos.Participants[] participantsInfos = null;

    private String summonerId = null;
    private String summonerName = null;
    private String summonerRegionRoute = null;
    private String keyApi = BuildConfig.API_KEY;
    private int respCode = -1;
    private StringBuilder respSummonerId;
    private StringBuilder respParticipants;


    public MatchApiUtil(MainViewModel viewModel, String summonerName, String summonerRegionRoute) {
        this.viewModel = viewModel;
        this.summonerName = summonerName;
        this.summonerRegionRoute = summonerRegionRoute;
    }


    @Override
    protected String doInBackground(Void... voids) {
        if (summonerId == null)
            summonerId = getSummonerId();
        if (summonerId == null)
            return getPaticipants();

        return null;
//        return getPaticipants();

    }

    public String getSummonerId() {
        if (summonerName != null) {
            summonerName = summonerName.toLowerCase();
            summonerName = summonerName.replace(" ", "");
            respSummonerId = new StringBuilder();

            try {
                URL url = new URL("https://" + summonerRegionRoute + "/lol/summoner/v4/summoners/by-name/" + summonerName);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setRequestProperty("X-Riot-Token", keyApi);
                conexao.setRequestProperty("Content-type", "application-json");
                conexao.setDoInput(true);
                conexao.setConnectTimeout(1000);
                conexao.connect();
                respCode = conexao.getResponseCode();
                System.out.println("ApiUtil.getSummonerId code conexao:" + respCode);
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
        return null;
    }

    private String getPaticipants() {
        if (summonerId != null) {
            respParticipants = new StringBuilder();

            try {
                URL url = new URL("https://" + summonerRegionRoute + "/lol/spectator/v4/active-games/by-summoner/" + summonerId);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setRequestProperty("Content-type", "application-json");
                conexao.setRequestProperty("X-Riot-Token", keyApi);
//            conexao.setDoOutput(true);
                conexao.setDoInput(true);
                conexao.setConnectTimeout(1000);
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
                participantsInfos = participants;
                for (int i = 5; i < participants.length; i++) {
                    System.out.println("TimerActivity.onCreate paticipant " + i + " names:" + participants[i].getSummonerName());
                }
                for (int i = 5; i < participants.length; i++) {
                    System.out.println("TimerActivity.onCreate paticipant " + i + " perks:" + participants[i].getPerks().perkIds.toString());
                }
                for (int i = 5; i < participants.length; i++) {
                    System.out.println("TimerActivity.onCreate paticipant " + i + " perks:" + participants[i].getPerks().perkIds.toString());
                }

            } else
                return "null";
        }
        return "null";
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public int getRespCode() {
        return respCode;
    }


    public SummonerInfos.Participants[] getParticipantsInfos() {
        return participantsInfos;
    }

}
