package com.plcoding.cmp_pagination

fun MovieDetailsApi.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        overview = overview,
        posterPath = posterPath.orEmpty(),
        releaseDate = releaseDate,
    )
}

fun MovieResponse.toEntity(): MoviesEntity {
    return MoviesEntity(
        page = page,
        movieDetailApi = movieDetailApis.map { it.toEntity() },
        totalPages = totalPages,
    )
}