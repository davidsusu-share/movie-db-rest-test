package hu.lorem.ipsum.mdf.fetch;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import hu.lorem.ipsum.mdf.model.Movie;

/**
 * Mock implementation of {@link MovieListFetcher} with a predefined set of movies
 */
public class MockMovieListFetcher implements MovieListFetcher {
    
    private final List<Movie> movies;
    
    private final TitleMatcher titleMatcher;
    

    public MockMovieListFetcher(Collection<Movie> movies) {
        this(movies, MockMovieListFetcher::matchTitleDefault);
    }

    public MockMovieListFetcher(Collection<Movie> movies, TitleMatcher titleMatcher) {
        this.movies = new ArrayList<>(movies);
        this.titleMatcher = titleMatcher;
    }
    
    
    @Override
    public List<Movie> findBySearchText(String searchText) {
        return movies.stream()
                .filter(movie -> titleMatcher.match(movie.getTitle(), searchText))
                .collect(Collectors.toList());
    }
    
    
    private static boolean matchTitleDefault(String title, String searchText) {
        // Pattern.compile() is slow, and pattern caching would be too heavy for this dumb class
        return normalize(title).contains(normalize(searchText));
    }
    
    private static String normalize(String text) {
        return " " + text.toLowerCase() + " ";
    }
    
    
    public static interface TitleMatcher {
        
        public boolean match(String title, String searchText);
        
    }

}
