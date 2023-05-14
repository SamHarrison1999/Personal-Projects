package operations.data;

import org.apache.log4j.BasicConfigurator;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Query;
import utils.Series;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static operations.file.CSVFileWriter.createNewCSV;

public class ReadDataFromAPI {

    static Logger logger = LoggerFactory.getLogger(ReadDataFromAPI.class);

    static final String URL = "https://www.bankofcanada.ca/valet/";
    static final String OBSERVATIONS = "observations";
    static final String MALFORMED_URL_ERROR_MESSAGE = "URL is malformed!!";
    static final String FORMAT = "json";

    /**
     * Uses the Bank of Canada API to get a list of all available
     * series or series groups.
     * @param listName - The set of data to return
     * @throws IOException - If the link doesn't exist
     */
    private static void getList(String listName) throws IOException {
        String jsonString = readFromURL(new String[]{"lists", listName, FORMAT});
        System.out.println(jsonString);
    }

    /**
     * Uses the Bank of Canada API to get the details
     * associated with a series.
     * @param seriesName - The series details to be returned
     * @throws IOException - If the link doesn't exist
     */
    public static void getSeries(String seriesName) throws IOException {
        String jsonString = readFromURL(new String[]{"series", seriesName, FORMAT});
        System.out.println(jsonString);
    }

    /**
     * Uses the Bank of Canada API to get the details
     * associated with a group name and all the series it contains.
     * @param groupName - The series group details to be returned
     * @throws IOException - If the link doesn't exist
     */
    public static void getSeriesGroups(String groupName) throws IOException {
        String jsonString = readFromURL(new String[]{"groups", groupName, FORMAT});
        System.out.println(jsonString);
    }

    /**
     * Uses the Bank of Canada API to filter by series.
     * @param seriesNames - a comma separated list of one or more series names.
     * @param query - The search query
     * @throws IOException - If the link doesn't exist
     */
    public static void getObservationsBySeries(String[] seriesNames, Optional<Query> query) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String seriesName: seriesNames) {
            stringBuilder.append(seriesName);
            if (seriesNames.length > 1) {
                stringBuilder.append(",");
            }
        }
        if (seriesNames.length > 1) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        if (query.isPresent()) {
            String jsonString = readFromURL(new String[]{OBSERVATIONS, stringBuilder.toString(), FORMAT}, query);
            List<List<Series>> result = parseData(jsonString);
            createNewCSV(result, "ExchangeRate.csv");
        } else {
            String jsonString = readFromURL(new String[]{OBSERVATIONS, stringBuilder.toString(), FORMAT});
            List<List<Series>> result = parseData(jsonString);
            createNewCSV(result, "ExchangeRate.csv");
        }
    }

    /**
     * Uses the Bank of Canada API to geta group of series filtered
     * by group name.
     * @param groupName - a group of series bundled together for convenience.
     * @param query - The search query
     * @throws IOException - If the link doesn't exist
     */
    public static void getObservationsBySeriesGroup(String groupName, Optional<Query> query) throws IOException {
        if (query.isPresent()) {
            String jsonString = readFromURL(new String[]{OBSERVATIONS, "group", groupName, FORMAT}, query);
            System.out.println(jsonString);
        } else {
            String jsonString = readFromURL(new String[]{OBSERVATIONS, "group", groupName, FORMAT});
            System.out.println(jsonString);
        }
    }

    /**
     * Uses the Bank of Canada API to get exchange rate observations
     * filtered by series name.
     * @param seriesNames - a comma separated list of one or more
     *                  series names as FX[currency1][currency2]
     * @throws IOException - If the link doesn't exist
     */
    public static void getForeignExchangeRatesInRSS(String[] seriesNames) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String seriesName: seriesNames) {
            stringBuilder.append(seriesName);
            if (seriesNames.length > 1) {
                stringBuilder.append(",");
            }
        }
        if (seriesNames.length > 1) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        String jsonString = readFromURL(new String[]{"fx_rss", stringBuilder.toString()});
        System.out.println(jsonString);
    }

    /**
     * Read data from a given url
     * @param urlPath - The url path
     * @throws IOException - If the link doesn't exist
     */
    private static String readFromURL(String[] urlPath) throws IOException {
        BasicConfigurator.configure();
        String jsonString = "";
        StringBuilder stringBuilder = new StringBuilder();
        for (String path: urlPath) {
            stringBuilder.append(path);
            stringBuilder.append("/");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        URL url = new URL(URL + stringBuilder);
        InputStream inputStream =  url.openStream();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString += line + '\n';
            }
            return jsonString;
        }
        catch (MalformedURLException e ) {
            e.printStackTrace();
            logger.error(MALFORMED_URL_ERROR_MESSAGE);
            throw new MalformedURLException(MALFORMED_URL_ERROR_MESSAGE);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Read data from a given url
     * @param urlPath - The url path
     * @param query - - The search query
     * @throws IOException - If the link doesn't exist
     */
    private static String readFromURL(String[] urlPath, Optional<Query> query) throws IOException {
        String jsonString = "";
        BasicConfigurator.configure();
        StringBuilder stringBuilder = new StringBuilder();
        for (String path: urlPath) {
            stringBuilder.append(path);
            stringBuilder.append("/");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        stringBuilder.append("?");
        if (query.flatMap(Query::getStartDate).isPresent() && query.flatMap(Query::getEndDate).isPresent()) {
            stringBuilder.append("start_date="+query.get().getStartDate().get()+"&end_date="+query.get().getEndDate().get());
        }
        else if (query.flatMap(Query::getStartDate).isPresent() && query.flatMap(Query::getEndDate).isEmpty()) {
            stringBuilder.append("start_date="+query.get().getStartDate().get());
        }
        else if (query.flatMap(Query::getStartDate).isEmpty() && query.flatMap(Query::getEndDate).isPresent()) {
            stringBuilder.append("end_date=").append(query.get().getEndDate().get());
        }
        else if (query.flatMap(Query::getInterval).isPresent() && query.flatMap(Query::getTimePeriod).isPresent()) {
            stringBuilder.append(query.get().getInterval().get()).append("=").append(query.get().getTimePeriod().get());
        }
        else if (query.flatMap(Query::getOrderDirection).isPresent()) {
            stringBuilder.append("order_dir=").append(query.get().getOrderDirection().get().toString().toLowerCase());
        }
        URL url = new URL(URL + stringBuilder);
        InputStream inputStream =  url.openStream();
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonString += line + '\n';
            }
            return jsonString;
        }
        catch (MalformedURLException e ) {
            e.printStackTrace();
            logger.error(MALFORMED_URL_ERROR_MESSAGE);
            throw new MalformedURLException(MALFORMED_URL_ERROR_MESSAGE);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Converts json data from api to csv data
     * @param jsonString - json data
     * @return a list containing all the series data
     */
    private static List<List<Series>> parseData(String jsonString) {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        List<List<Series>> allData = new LinkedList<>();


        jsonObject.getAsJsonArray("observations").forEach(observation -> {
            LocalDate date = LocalDate.parse(observation.getAsJsonObject().get("d").getAsString());
            observation.getAsJsonObject().entrySet().stream().filter(entry -> entry.getKey().startsWith("FX")).forEach(entry -> {
                String seriesName = entry.getKey();
                JsonObject seriesDetail = jsonObject.getAsJsonObject("seriesDetail").getAsJsonObject(seriesName);
                String label = seriesDetail.get("label").getAsString();
                String description = seriesDetail.get("description").getAsString();
                double value = Double.parseDouble(entry.getValue().getAsJsonObject().get("v").getAsString());

                List<Series> individualResult = new LinkedList<>();
                individualResult.add(new Series(date, seriesName, label, description, value));
                allData.add(individualResult);
            });
        });
        return allData;
    }

}
