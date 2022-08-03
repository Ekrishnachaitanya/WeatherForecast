/* Assignment: InClass08
   File Name: Group40_InClass08
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package edu.uncc.weather;

import java.util.ArrayList;

public class Weather {

    Main main;
    Wind wind;
    Cloud clouds;
    ArrayList<WeatherDescription> weather;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Cloud getClouds() {
        return clouds;
    }

    public void setClouds(Cloud clouds) {
        this.clouds = clouds;
    }

    public ArrayList<WeatherDescription> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<WeatherDescription> weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", weather=" + weather +
                '}';
    }
}
