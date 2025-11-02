package no.roadaccidentrisk.website.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PredictRequest {

    @NotBlank
    @JsonProperty("road_type")         // outbound
    @JsonAlias("roadType")             // inbound
    private String road_type;

    @NotNull
    @Min(1) @Max(400)
    @JsonProperty("speed_limit")
    @JsonAlias("maxSpeed")
    private Integer speed_limit;

    @NotBlank
    @JsonProperty("time_of_day")
    @JsonAlias("timeOfDay")
    private String time_of_day;

    @NotBlank
    @JsonProperty("weather")           // same both ways
    private String weather;

    public String getRoad_type() {
        return road_type;
    }

    public void setRoad_type(String road_type) {
        this.road_type = road_type;
    }

    public Integer getSpeed_limit() {
        return speed_limit;
    }

    public void setSpeed_limit(Integer speed_limit) {
        this.speed_limit = speed_limit;
    }

    public String getTime_of_day() {
        return time_of_day;
    }

    public void setTime_of_day(String time_of_day) {
        this.time_of_day = time_of_day;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
