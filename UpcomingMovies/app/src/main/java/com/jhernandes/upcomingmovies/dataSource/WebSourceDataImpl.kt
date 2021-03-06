package com.jhernandes.upcomingmovies.dataSource

import com.jhernandes.datamodule.models.MovieGenre
import com.jhernandes.datamodule.repository.DataRepository
import com.jhernandes.upcomingmovies.models.UpcomingMovie
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WebSourceDataImpl(private val webRepository: DataRepository) : MoviesDataSource {

    override fun getUpcomingMovies(): Single<MutableList<UpcomingMovie>> {
        return loadMovies(1)
    }

    override fun loadMore(position: Int): Single<MutableList<UpcomingMovie>> {
        return loadMovies(position)
    }

    override fun getMovieGenresList(): Single<List<MovieGenre>> {
        return webRepository.getMoviesGenreList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap { Observable.just(it.genres) }
            .single(listOf())
    }

    override fun queryMovie(query: String): Single<MutableList<UpcomingMovie>> {
        return searchMovie(query, 1)
    }

    override fun queryMovie(query: String, page: Int): Single<MutableList<UpcomingMovie>> {
        return searchMovie(query, page)
    }

    private fun loadMovies(page: Int): Single<MutableList<UpcomingMovie>> {
        return webRepository.getUpcomingMovies(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapIterable { response -> response.results }
            .map { result -> WebAdapter().getFromResult(result) }
            .toList()
    }

    private fun searchMovie(query: String, page : Int): Single<MutableList<UpcomingMovie>> {
        return webRepository.getMovieQuery(query, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapIterable { response -> response.results }
            .map { result -> WebAdapter().getFromResult(result) }
            .toList()
    }
}

