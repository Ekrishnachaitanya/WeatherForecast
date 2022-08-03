/* Assignment: InClass08
   File Name: Group40_InClass08
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package edu.uncc.weather;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import edu.uncc.weather.databinding.FragmentCurrentWeatherBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrentWeatherFragment extends Fragment {
    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";
    private DataService.City mCity;
    FragmentCurrentWeatherBinding binding;
    CurrentWeatherListener wlistener;
    OkHttpClient client = new OkHttpClient();
    String API_KEY = "2e71b765d4464c1420ecd4bc8199a3dd", CITY = "q", APP_ID="appid", urlImg = "https://openweathermap.org/img/wn/", png = "@2x.png";

    public CurrentWeatherFragment() {
        // Required empty public constructor
    }

    public static CurrentWeatherFragment newInstance(DataService.City city) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (DataService.City) getArguments().getSerializable(ARG_PARAM_CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false);
        Thread.currentThread().getId();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Weather");
        HttpUrl.Builder builder = new HttpUrl.Builder();
        HttpUrl url = builder.scheme("https")
                .host("api.openweathermap.org")
                .addPathSegment("data")
                .addPathSegment("2.5")
                .addPathSegment("weather")
                .addEncodedQueryParameter(CITY, mCity.getCity())
                .addEncodedQueryParameter(APP_ID, API_KEY)
                .build();
        Request request = new Request.Builder()
                            .url(url)
                            .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    Gson gson = new Gson();
                    String responseBody = response.body().string();
                    Weather weather = gson.fromJson(responseBody, Weather.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Main main = weather.getMain();
                            Wind wind = weather.getWind();
                            Cloud cloud = weather.getClouds();
                            WeatherDescription description = weather.getWeather().get(0);
                            Picasso.get().load(urlImg+description.getIcon()+png).into(binding.imageView);
                            binding.cityTextView.setText(mCity.getCity()+","+mCity.getCountry());
                            binding.temperatureValue.setText(main.temp + getString(R.string.fLabel));
                            binding.temperatureMaxValue.setText(main.temp_max+getString(R.string.fLabel));
                            binding.temperatureMinValue.setText(main.temp_min+getString(R.string.fLabel));
                            binding.descriptionValue.setText(description.getDescription());
                            binding.humidityValue.setText(main.getHumidity()+getString(R.string.percentageSymbol));
                            binding.windSpeedValue.setText(wind.getSpeed()+getString(R.string.milesPerHrLabel));
                            binding.windDegreeValue.setText(wind.getDeg()+getString(R.string.degreesLabel));
                            binding.cloudinessValue.setText(cloud.getAll()+getString(R.string.percentageSymbol));
                            binding.foreCastButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    wlistener.gotoForeCast(mCity);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        wlistener = (CurrentWeatherListener) context;
    }

    interface CurrentWeatherListener{
        void gotoForeCast(DataService.City city);
    }
}