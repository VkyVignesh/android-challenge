package com.vky.startwars;

import com.vky.startwars.model.StarWar;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Vignesh on 15-10-2016.
 */

public interface APIService {
    @GET
    Observable<StarWar> downloadStarWarsData(@Url String url);

    @GET
    Observable<StarWar.Film> downloadFilmDetail(@Url String url);
}
