package CoronaStat;
import javafx.beans.property.SimpleStringProperty;

public class Result {
    private final SimpleStringProperty date = new SimpleStringProperty("");
    private final SimpleStringProperty confirmed = new SimpleStringProperty("");
    private final SimpleStringProperty deaths = new SimpleStringProperty("");
    private final SimpleStringProperty recovered = new SimpleStringProperty("");

    public Result() { this("","","",""); };

    public Result(String datum, String potvrdenych, String smrti, String uzdravenych) {
        setDate(potvrdenych);
        setConfirmed(smrti);
        setDeaths(smrti);
        setRecovered(uzdravenych);

    }

    public String getDate() { return date.get(); }

    public void setDate(String date) { this.date.set(date); }

    public String getConfirmed() { return confirmed.get(); }

    public void setConfirmed(String confirmed) { this.confirmed.set(confirmed); }

    public String getDeaths() { return deaths.get(); }

    public void setDeaths(String deaths) { this.deaths.set(deaths); }

    public String getRecovered() { return recovered.get(); }

    public void setRecovered(String recovered) { this.recovered.set(recovered); }
}
