package com.ylizma.coronatracker2.services;

import com.ylizma.coronatracker2.models.LocationStates;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
    private List<LocationStates> allstates = new ArrayList<>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchData() throws IOException, InterruptedException {
        List<LocationStates> newstate = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvdata = new StringReader(response.body());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvdata);
        for (CSVRecord record : records) {

            LocationStates state = new LocationStates();
            state.setState(record.get("Province/State"));
            state.setCountry(record.get("Country/Region"));
            state.setTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            System.out.println(state);
            if (state.getState().isEmpty()) state.setState(state.getCountry());
            newstate.add(state);
        }
        this.allstates = sorted(newstate);
    }

    private List<LocationStates> sorted(List<LocationStates> newstate) {
        Collections.sort(newstate, (o1, o2) -> {
            if (o1.getTotalCases() > o2.getTotalCases()) return -1;
            if (o1.getTotalCases() < o2.getTotalCases()) return 1;
            else return 0;
        });
        return newstate;
    }

    public List<LocationStates> search(String s) {
        List<LocationStates> searched = new ArrayList<>();
        this.allstates.forEach(locationStates -> {
            if (locationStates.getState()
                    .toLowerCase()
                    .contains(s.toLowerCase())) searched.add(locationStates);
        });
        return searched;
    }

    public List<LocationStates> getAllstates() {
        return allstates;
    }
}
