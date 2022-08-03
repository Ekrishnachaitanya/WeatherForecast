/* Assignment: InClass08
   File Name: Group40_InClass08
   Student Names: Krishna Chaitanya Emmala, Naga Sivaram Mannam
*/
package edu.uncc.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import edu.uncc.weather.databinding.FragmentWeatherForecastBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherForecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherForecastFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";
    private DataService.City mCity;
    FragmentWeatherForecastBinding binding;
    LinearLayoutManager layoutManager;
    OkHttpClient client = new OkHttpClient();
    String API_KEY = "2e71b765d4464c1420ecd4bc8199a3dd", CITY = "q", APP_ID="appid", urlImg = "https://openweathermap.org/img/wn/", png = "@2x.png";

    public WeatherForecastFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WeatherForecastFragment newInstance(DataService.City city) {
        WeatherForecastFragment fragment = new WeatherForecastFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentWeatherForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity activity = getActivity();
        if(activity != null)
            activity.setTitle(getString(R.string.weatherForecast));
        HttpUrl.Builder builder = new HttpUrl.Builder();
        HttpUrl url = builder.scheme("https")
                .host("api.openweathermap.org")
                .addPathSegment("data")
                .addPathSegment("2.5")
                .addPathSegment("forecast")
                .addQueryParameter(CITY, mCity.getCity())
                .addQueryParameter(APP_ID, API_KEY)
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
                if(response.isSuccessful()) {
                    Gson gson = new Gson();
                    String responseBody = response.body().string();
                    WeatherForecasts weatherForecasts = gson.fromJson(responseBody, WeatherForecasts.class);
                    List<Forecast> forecasts = weatherForecasts.getList();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ForecastAdapter adapter = new ForecastAdapter(forecasts);
                            binding.recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
        binding.cityAndCountryValue.setText(mCity.getCity()+","+mCity.getCountry());
        binding.recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(activity);
        binding.recyclerView.setLayoutManager(layoutManager);
    }

    class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder>{
        List<Forecast> weatherForecasts;

        public ForecastAdapter(List<Forecast> weatherForecasts){
            this.weatherForecasts = weatherForecasts;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_weather_cell, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Forecast forecast = weatherForecasts.get(position);
            holder.dateTextView.setText(forecast.getDt_txt());
            Main main = forecast.getMain();
            WeatherDescription description = forecast.getWeather().get(0);
            holder.temperatureTextView.setText(main.getTemp()+getString(R.string.fLabel));
            holder.maxTempValue.setText(main.getTemp()+getString(R.string.fLabel));
            holder.minTempValue.setText(main.getTemp()+getString(R.string.fLabel));
            holder.descriptionTextView.setText(description.getDescription());
            Picasso.get().load(urlImg+description.getIcon()+png).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return this.weatherForecasts.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView dateTextView, temperatureTextView, maxTempValue, minTempValue, descriptionTextView;
            ImageView imageView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                dateTextView = itemView.findViewById(R.id.dateTextView);
                temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
                maxTempValue = itemView.findViewById(R.id.maxTempValue);
                minTempValue = itemView.findViewById(R.id.minTempValue);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                imageView = itemView.findViewById(R.id.weatherImageView);
            }
        }

    }
}