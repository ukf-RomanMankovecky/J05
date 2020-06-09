package CoronaStat;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class Controller {
    @FXML private TableView<Result> tableView;
    @FXML private TextField countryField;
    @FXML private TextField dateField;

    @FXML protected void initialize() {
        nadstavKrajiny();
        stiahniData("", "");
    }

    static HashMap<String, Result> resultHashMap;
    private HashMap<String, String> countries;
    static boolean GB;

    void addResult(Result result) {
        ObservableList<Result> data = tableView.getItems();
        data.add(result);
    }

    @FXML void find(ActionEvent event){
        tableView.getItems().clear();
        stiahniData(countryField.getText(), dateField.getText());
    }

    void stiahniData(String ISO, String datum) {
        ISO = transformIso(ISO);
        if(!CheckD(datum)) {
            datum="";
        }
        resultHashMap = new HashMap<>();
        HttpClient client = HttpClient.newHttpClient();
        String uri = "https://covidapi.info/api/v1/";
        if(ISO.equals("")) {
            uri += "global";}
        else {
            uri += "country/"+ISO;
        }
        if(!datum.equals("")) {
            uri += "/"+datum;
        }
        GB = ISO.equals("");
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Controller::jsonParse)
                .join();

        for (String date : resultHashMap.keySet()) addResult(resultHashMap.get(date));
    }

    void nadstavKrajiny(){
        countries = new HashMap<>();
        for (String ISO2 : Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA2)) {
            Locale l = new Locale("", ISO2);
            String ISO3 = l.getISO3Country();
            countries.put(l.getDisplayCountry(), ISO3);
        }
    }

    String transformIso(String iso){
        for(String i:countries.values()) if(iso.equals(i)) return iso;
        iso = countries.get(iso);
        if(iso!=null) return iso;
        return "";
    }

    boolean CheckD(String datum){
        if(datum.length()!=10) return false;
        for(int i=0;i<=9;i++){
            if(i==4||i==7) {
                if(datum.charAt(i)!='-') {
                    return false;
                }
            }
            if(i!=4&&i!=7) {
                if(datum.charAt(i)<'0'||datum.charAt(i)>'9') {
                    return false;
                }
            }
        }
        return true;
    }

    private static String jsonParse(String responseString) {
        JSONArray results = new JSONArray("[" + responseString + "]");
        JSONArray resultDates = results.getJSONObject(0).getJSONObject("result").names();

        String[] resultDatesSorted = new String[resultDates.length()];

        for (int i = 0; i < resultDates.length(); i++) {
            resultDatesSorted[i] = resultDates.get(i).toString();
        }

        Arrays.sort(resultDatesSorted);

        for (String res : resultDatesSorted) {
            JSONObject result = results.getJSONObject(0).getJSONObject("result");
            if(!GB) result = result.getJSONObject(res);

            String date = res;
            String confirmed = String.valueOf(result.getInt("confirmed"));
            String deaths = String.valueOf(result.getInt("deaths"));
            String recovered = String.valueOf(result.getInt("recovered"));

            resultHashMap.put(res, new Result(res,confirmed,deaths,recovered));
        }

        return null;
    }

}


