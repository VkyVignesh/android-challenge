package com.vky.startwars.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.vky.startwars.APIService;
import com.vky.startwars.R;
import com.vky.startwars.ServiceGenerator;
import com.vky.startwars.adapter.StarShipsAdapter;
import com.vky.startwars.databinding.ActivityMainBinding;
import com.vky.startwars.model.StarWar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private Subscription subscription;
    private List<StarWar.Results> list = new ArrayList<>();

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        StarShipsAdapter adapter = new StarShipsAdapter(MainActivity.this, list);
        binding.recyclerView.setAdapter(adapter);
        // ProgressDialog initialization
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        // Starts Downloading Data.
        downloadStarWarData(ServiceGenerator.API_BASE_URL);
    }

    // Downloads Starships data's.
    private void downloadStarWarData(String url) {
        Log.d(TAG, "downloadStarWarData: ");
        APIService apiService = ServiceGenerator.createService(APIService.class);

        subscription = apiService.downloadStarWarsData(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StarWar>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(StarWar starWar) {

                        for (StarWar.Results results : starWar.results)
                            if (!results.cost_in_credits.contains("unknown"))
                                list.add(results);
                        // Checks response contains next url.
                        if (list.size() < Integer.parseInt(starWar.count) && starWar.next != null)
                            downloadStarWarData(starWar.next);
                        else {
                            //
                            progressDialog.dismiss();
                            Collections.sort(list);
                            // Loop to remove extra item's more than 15.
                            for (int i = list.size(); list.size() > 15; i--) {
                                list.remove(i - 1);
                            }
                            binding.recyclerView.getAdapter().notifyDataSetChanged();
                            // Method to collect filmnames from sorted items.
                            collectFilmName(list);
                        }
                    }
                });
    }

    // Downloads Film names for sorted ships objects.
    private void collectFilmName(List<StarWar.Results> resultsList) {
        APIService apiService = ServiceGenerator.createService(APIService.class);

        for (final StarWar.Results results : resultsList) {
            apiService.downloadFilmDetail(results.films[0])
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<StarWar.Film>() {
                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted: ");
                            binding.recyclerView.getAdapter().notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: ");
                        }

                        @Override
                        public void onNext(StarWar.Film film) {
                            Log.d(TAG, "onNext: " + film.title);
                            results.title = film.title;
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
