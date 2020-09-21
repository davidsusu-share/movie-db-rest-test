# Movie Data Fetcher API

This is a very simple rest service for searching movies by title keywords.

There is a single rest page which can be called in this way:

//movies/{searchText}?apiName={apiName}

This fetches movies matches to `{searchText}` from the api identified by `{apiName}`.

The result is a json array where each item contains `title`, `year` and `directorName`.

There are three possible values of `{apiName}`:

- `omdb`: uses omdbapi.com to fetch the data
- `tmdb`: uses api.themoviedb.org to fetch the data
- `mock`: searches in a small predefined set of movies

There are four environmental dependency:

- omdbapi.com: provides movie data, you can configure the api key via `app.omdbApiKey`
- api.themoviedb.org: provides movie data, you can configure the api key via `app.tmdbApiKey`
- redis server: caches movie list, you can configure its host via `app.redisHost`
- mysql server: collects statistics, you can configure the connection uri in the usual way

Each of them can be mocked:

- the first two by using `apiName=mock`
- redis will be disabled if `app.useCache` is `false` (default is `true`)
- statistics will be disabled if `app.collectStatistics` is `false` (default is `true`)
