package com.twinedo.themoviecatalogue.api;

import com.twinedo.themoviecatalogue.BuildConfig;
import com.twinedo.themoviecatalogue.object.CreditsResult;
import com.twinedo.themoviecatalogue.object.GenresResult;
import com.twinedo.themoviecatalogue.object.MovieResult;
import com.twinedo.themoviecatalogue.object.TrailersResult;
import com.twinedo.themoviecatalogue.object.TvShowsResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    //get discover movie
    @GET("discover/movie")
    Call<MovieResult> getMovies(@Query("api_key") String apiKey);

    //get discover tv
    @GET("discover/tv")
    Call<TvShowsResult> getTVShows(@Query("api_key") String apiKey);

    //get movie genre
    @GET("movie/{id}")
    Call<GenresResult> getGenresMovie(@Path("id") long id, @Query("api_key") String apiKey);

    //get tv
    @GET("tv/{id}")
    Call<GenresResult> getGenresTV(@Path("id") long id, @Query("api_key") String apiKey);

    //TrailersMovie
    @GET("movie/{id}/videos")
    Call<TrailersResult> getTrailerMovie(@Path("id") long id, @Query("api_key") String apiKey);

    //TrailersTV
    @GET("tv/{id}/videos")
    Call<TrailersResult> getTrailerTV(@Path("id") long id, @Query("api_key") String apiKey);

    //getCreditsMovie
    @GET("movie/{id}/credits")
    Call<CreditsResult> getCreditsMovie(@Path("id") long id, @Query("api_key") String apiKey);

    //getCreditsTV
    @GET("tv/{id}/credits")
    Call<CreditsResult> getCreditsTV(@Path("id") long id, @Query("api_key") String apiKey);

    //search Movie
    @GET("search/movie?api_key="+ BuildConfig.API_KEY)
    Call<MovieResult> getSearchMovies(@Query("query") String movieSearch);

    //search TV
    @GET("search/tv?api_key="+ BuildConfig.API_KEY)
    Call<TvShowsResult> getSearchTV(@Query("query") String tvSearch);

    //get release today
    @GET("discover/movie?sort_by=release_date.asc")
    Call<MovieResult> getReleaseToday(@Query("api_key") String apiKey ,@Query("primary_release_date.gte") String releaseDate, @Query("primary_release_date.lte") String releaseDate2);

}
