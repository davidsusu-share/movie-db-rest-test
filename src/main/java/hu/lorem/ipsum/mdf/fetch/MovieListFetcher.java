package hu.lorem.ipsum.mdf.fetch;

import java.util.List;

import hu.lorem.ipsum.mdf.model.Movie;

/**
 * Interface for movie list fetchers
 */
public interface MovieListFetcher {

    /**
     * Searches for movies with a matching title
     * 
     * @param searchText Matches to movie title words
     * @return list of the matching movies
     */
    public List<Movie> findBySearchText(String searchText);
    
}
