package model;

import util.EmojiUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ForecastElement {

    private String dateTime;
    private double temperature;
    private double feelsLike;
    private List<String> conditions;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public List<String> getConditions() {
        return conditions;
    }

    public List<String> getConditionsAsEmoji() {
        return conditions.stream().map(EmojiUtils::getEmoji).collect(Collectors.toList());
    }

    public void setConditions(List<String> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return String.format("%s  %d °C (%d °C)\t%s\n", formatDateTime(), Math.round(temperature),
                Math.round(feelsLike), getConditionsAsEmoji());
    }

    private String formatDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime formattedDateTime = LocalDateTime.parse(dateTime, formatter);

        formatter = DateTimeFormatter.ofPattern("dd MMM HH:mm", Locale.forLanguageTag("uk-UA"));

        return formattedDateTime.format(formatter)
                .replaceAll("09:00", "_ранок_")
                .replaceAll("15:00", "_день_")
                .replaceAll("21:00", "_вечір_");
    }
}
