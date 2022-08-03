/* Assignment: InClass08
   File Name: Group40_InClass08
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package edu.uncc.weather;

import java.util.List;

public class WeatherForecasts {
    List<Forecast> list;

    public List<Forecast> getList() {
        return list;
    }

    public void setList(List<Forecast> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "WeatherForecasts{" +
                "list=" + list +
                '}';
    }
}
