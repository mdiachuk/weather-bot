package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.CurrentWeather;
import model.ForecastElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherServiceImpl implements WeatherService {

    private final static String OPENWEATHERMAP_URL_TEMPLATE;
    private final static String ERROR_MESSAGE = "Сталася помилка. Вибач за незручності \uD83E\uDD7A";

    private final ObjectMapper mapper;

    static {
        OPENWEATHERMAP_URL_TEMPLATE = "http://api.openweathermap.org/data/2.5/"
                + "%s?q=%s&units=metric&lang=ua&appid="
                + System.getenv("OPENWEATHERMAP_APPID");
    }

    public WeatherServiceImpl() {
        mapper = new ObjectMapper();
    }

    @Override
    public String getCurrentWeather(String city) {
        try {
            CurrentWeather currentWeather = convertJsonToCurrentWeather(getJsonWeatherData("weather", city));
            return String.format("У місті *%s*", city) + currentWeather;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_MESSAGE;
        }
    }

    @Override
    public String getWeatherForecast(String city) {
        try {
            List<ForecastElement> weatherForecast = convertJsonToWeatherForecast(getJsonWeatherData("forecast", city));
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Прогноз погоди для міста *%s*\n\n", city));
            weatherForecast.forEach(sb::append);
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_MESSAGE;
        }
    }

    private String getJsonWeatherData(String type, String city) throws Exception {
        URL urlObject = new URL(String.format(OPENWEATHERMAP_URL_TEMPLATE, type, city));

        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 404) {
            throw new IllegalArgumentException();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = in.lines().collect(Collectors.joining());
        in.close();

        connection.disconnect();

        return response;
    }

    private CurrentWeather convertJsonToCurrentWeather(String json) throws IOException {
        JsonNode weatherNode = mapper.readTree(json).get("weather");
        List<String> weatherConditions = new ArrayList<>();
        if (weatherNode.isArray()) {
            weatherNode.forEach(
                    objNode -> weatherConditions.add(objNode.get("description").asText())
            );
        }

        JsonNode mainNode = mapper.readTree(json).get("main");
        CurrentWeather currentWeather = mapper.treeToValue(mainNode, CurrentWeather.class);
        currentWeather.setConditions(weatherConditions);

        JsonNode windNode = mapper.readTree(json).get("wind");
        currentWeather.setWindSpeed(windNode.get("speed").asDouble());

        return currentWeather;
    }

    private List<ForecastElement> convertJsonToWeatherForecast(String json) throws IOException {
        JsonNode arrNode = mapper.readTree(json).get("list");
        List<ForecastElement> forecast = new ArrayList<>();

        if (arrNode.isArray()) {
            for (JsonNode objNode : arrNode) {
                String dateTime = objNode.get("dt_txt").asText();
                if (dateTime.contains("09:00") || dateTime.contains("15:00") || dateTime.contains("21:00")) {
                    forecast.add(convertJsonToForecastElement(objNode.toString()));
                }
            }
        }

        return forecast;
    }

    private ForecastElement convertJsonToForecastElement(String json) throws IOException {
        ForecastElement forecastElement = new ForecastElement();

        forecastElement.setDateTime(
                mapper.readTree(json).get("dt_txt").asText()
        );

        forecastElement.setTemperature(
                mapper.readTree(json).get("main").get("temp").asDouble()
        );

        forecastElement.setFeelsLike(
                mapper.readTree(json).get("main").get("feels_like").asDouble()
        );

        JsonNode weatherNode = mapper.readTree(json).get("weather");
        List<String> weatherConditions = new ArrayList<>();
        if (weatherNode.isArray()) {
            weatherNode.forEach(
                    objNode -> weatherConditions.add(objNode.get("main").asText())
            );
        }
        forecastElement.setConditions(weatherConditions);

        return forecastElement;
    }
}
