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

    public SummonerInfos.Participants[] participantsInfos = null;

    private String summonerId = null;
    private String summonerName = null;
    private MainViewModel viewModel;
    private String summonerRegionRoute = null;
    private String keyApi = BuildConfig.API_KEY;
    private int respCodeSumonnerId = -1;
    private int respCodeParticipants = -1;
    private int respCodeVersion = -1;
    private int respCodeChampionsInfos = -1;
    private StringBuilder respSummonerId;
    private StringBuilder respParticipants;
    private StringBuilder championsInfos = null;
    private StringBuilder versionInfos = null;
    private String version = null;
    private static final int ID_RUNE = 8347;
    private String championsWithRune;

    public String getChampionsWithRune() {
        return championsWithRune;
    }

    public String getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    public StringBuilder getChampionsInfos() {
        return championsInfos;
    }

    public void setChampionsInfos(StringBuilder championsInfos) {
        this.championsInfos = championsInfos;
    }

    public MatchApiUtil(MainViewModel viewModel, String summonerName, String summonerRegionRoute) {
        this.summonerName = summonerName;
        this.summonerRegionRoute = summonerRegionRoute;
        this.viewModel = viewModel;
    }


    @Override
    public String doInBackground(Void... voids) {
        championsInfos = viewModel.championsInfos.getValue();
        summonerId = viewModel.summonerId.getValue();
        if (championsInfos == null) {
            getChampions();
        }
        if (summonerId == null) {
            summonerId = getSummonerIdApi();
        }
        if (summonerId != null) {
            return getPaticipants();
        }
        return null;
//        return getPaticipants();

    }

    public String getSummonerIdApi() {
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
                conexao.setConnectTimeout(2000);
                conexao.connect();
                respCodeSumonnerId = conexao.getResponseCode();
                System.out.println("ApiUtil.getSummonerId code conexao:" + respCodeSumonnerId);
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
                summonerId = summonerUser.getId();
                return summonerUser.getId();
            } else
                return null;
        }
        return null;
    }

    public String getPaticipants() {
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
                conexao.setConnectTimeout(2000);
                conexao.connect();
                respCodeParticipants = conexao.getResponseCode();
                System.out.println("ApiUtil.get participants code conexao:" + respCodeParticipants);
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

//                for (int i = 0; i < participants.length; i++) {
//                    for (int j = 0; j < participants[i].getPerks().perkIds.length; j++) {
//                        System.out.println("TimerActivity.onCreate paticipant "+participants[i].getSummonerName()  + " perks:" + participants[i].getPerks().perkIds[j]);
//
//                    }
//                }
                int teamid = -1;
                for (int i = 0; i < 10; i++) {
                    if (participantsInfos[i].getSummonerName().replace(" ","").equalsIgnoreCase(summonerName)) {
                        if (participantsInfos[i].getTeamId() == 100)
                            teamid = 200;
                        else if (participantsInfos[i].getTeamId() == 200)
                            teamid = 100;
                    }

                }
                for (int i = 0; i < participantsInfos.length; i++) {
                    for (int j = 0; j < participantsInfos[i].getPerks().getPerkIds().length; j++) {
//                        System.out.println("TimerActivity.onCreate paticipant " + participantsInfos[i].getSummonerName() + " perks:" + participantsInfos[i].getPerks().getPerkIds()[j]);
                        if(participantsInfos[i].getTeamId()==teamid){
                            if (participantsInfos[i].getPerks().getPerkIds()[j] == ID_RUNE) {
                                if(championsWithRune==null)
                                    championsWithRune="";
                                championsWithRune+=" "+nameChampion(String.valueOf(participantsInfos[i].getChampionId()));
                            }
                        }
                    }
                }
            } else
                return "data";
        }
        return "null";
    }

    public String getCurrentVersion() {

        versionInfos = new StringBuilder();

        try {
            URL url = new URL("https://ddragon.leagueoflegends.com/api/versions.json");
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setRequestProperty("Content-type", "application-json");
            conexao.setConnectTimeout(2000);
            conexao.connect();
            respCodeVersion = conexao.getResponseCode();
            System.out.println("ApiUtil.getCurrenteVersion code  conexao:" + respCodeVersion);
            Scanner scanner = new Scanner(conexao.getInputStream());
            while (scanner.hasNext()) {
                versionInfos.append(scanner.next());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("MatchApiUtil.versionInfos version " + versionInfos);

        version = "";

        char currentChar = ' ';

        int index = 2;
        while (currentChar != '\"') {
            currentChar = versionInfos.charAt(index);
            System.out.println("MatchApiUtil.nameChampion " + versionInfos.charAt(index));
            version += versionInfos.charAt(index);
            index++;
            currentChar = versionInfos.charAt(index);

        }
        System.out.println("MatchApiUtil.version a version é " + version);


        return version;
    }

    public String getChampions() {

        championsInfos = new StringBuilder();
        if (version == null) {
            getCurrentVersion();
        }
        try {
            URL url = new URL("http://ddragon.leagueoflegends.com/cdn/" + version + "/data/en_US/champion.json");
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setRequestProperty("Content-type", "application-json");
            conexao.setConnectTimeout(5000);
            conexao.connect();
            respCodeChampionsInfos = conexao.getResponseCode();
            System.out.println("ApiUtil.getchampionsinfos code champion conexao:" + respCodeChampionsInfos);
            Scanner scanner = new Scanner(conexao.getInputStream());
            while (scanner.hasNext()) {
                championsInfos.append(scanner.next());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("MatchApiUtil.chmapionsInfos " + championsInfos.length());
        System.out.println("MatchApiUtil.getChampionsInfos " + championsInfos);

        return "null";
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    public int getRespCodeSumonnerId() {
        return respCodeSumonnerId;
    }

    public int getRespCodeParticipants() {
        return respCodeParticipants;
    }

    public int getRespCodeVersion() {
        return respCodeVersion;
    }

    public int getRespCodeChampionsInfos() {
        return respCodeChampionsInfos;
    }

    public SummonerInfos.Participants[] getParticipantsInfos() {
        return participantsInfos;
    }

    public String nameChampion(String championId) {
        String name = "";
        String key = "\"key\":\"" + championId + "\"";
        int indexInicial = championsInfos.indexOf(key);
        if (indexInicial >= 0) {
            char currentChar = ' ';

            int index = indexInicial + key.length() + 9;
            while (currentChar != '\"') {
                currentChar = championsInfos.charAt(index);
                name += championsInfos.charAt(index);
                index++;
                currentChar = championsInfos.charAt(index);

            }
//            System.out.println("MatchApiUtil.nameChampion o nome é " + name);
        }
        return name;
    }
}
