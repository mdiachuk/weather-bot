package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentWeather {

    private List<String> conditions;
    @JsonProperty("temp")
    private double temperature;
    @JsonProperty("feels_like")
    private double feelsLike;
    private int humidity;
    private double windSpeed;

    public List<String> getConditions() {
        return conditions;
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public String toString() {
        return String.format(" %s\n\n" +
                        "\uD83C\uDF21 %d °C (відчувається як %d °C)\n" +
                        "\uD83D\uDCA7 Вологість повітря — %d%%\n" +
                        "\uD83D\uDCA8 Швидкість вітру — %.2f м/с\n",
                conditions, Math.round(temperature), Math.round(feelsLike),
                humidity, windSpeed);
    }
}
