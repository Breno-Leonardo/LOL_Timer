package com.onerb.timerlol.api;

import com.onerb.timerlol.api.Perks;

public class SummonerInfos {
    private Participants[] participants;

    public Participants[] getParticipants() {
        return participants;
    }

    public void setParticipants(Participants[] participants) {
        this.participants = participants;
    }

    public class Participants{
        private int teamID;
        private int  championId;
        private String summonerName;
        private Perks perks;

        public Perks getPerks() {
            return perks;
        }

        public void setPerks(Perks perks) {
            this.perks = perks;
        }

        public int getTeamID() {
            return teamID;
        }

        public void setTeamID(int teamID) {
            this.teamID = teamID;
        }

        public int getChampionId() {
            return championId;
        }

        public void setChampionId(int championId) {
            this.championId = championId;
        }

        public String getSummonerName() {
            return summonerName;
        }

        public void setSummonerName(String summonerName) {
            this.summonerName = summonerName;
        }
    }
}
