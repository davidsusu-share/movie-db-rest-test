package hu.lorem.ipsum.mdf.fetch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;

import hu.lorem.ipsum.mdf.model.Movie;
import hu.lorem.ipsum.mdf.util.ParseUtil;

/**
 * {@link MovieListFetcher} implementation which fetches data from omdbapi.com.
 * 
 * <p>By default the following URLs will be used:</p>
 * 
 * <ul>
 *   <li>Search results: http://www.omdbapi.com/?s={searchText}&apikey={apiKey}</li>
 *   <li>Movie details: http://www.omdbapi.com/?t={title}&apikey={apiKey}</li>
 * </ul>
 * 
 * <p>Meaning of the template variables:</p>
 * 
 * <ul>
 *   <li>apiKey: the API key for omdbapi.com</li>
 *   <li>searchText: The given search text</li>
 *   <li>imdbId: The movie's ID on the IMDB</li>
 * </ul>
 * 
 * <p>
 *   Currently, paging is not supported explicitly.
 *   When using the default URLs, a limited number of results will be fetched.
 *   This limit is implicit, not defined in this implementation.
 *   Result order is also implicit.
 * </p>
 */
public class OmdbMovieListFetcher implements MovieListFetcher {
    
    private static final String DEFAULT_LIST_URL_PATTERN =
            "http://www.omdbapi.com/?s={searchText}&apikey={apiKey}";
    
    private static final String DEFAULT_DETAILS_URL_PATTERN =
            "http://www.omdbapi.com/?i={imdbId}&apikey={apiKey}";
    
    private static final String RESULT_FIELD = "Search";
    
    private static final String IMDB_ID_FIELD = "imdbID";
    
    private static final String TITLE_FIELD = "Title";
    
    private static final String YEAR_FIELD = "Year";
    
    private static final String DIRECTOR_NAME_FIELD = "Director";


    private final String listUrlPattern;

    private final String detailsUrlPattern;
    
    private final RestTemplate restTemplate;
    

    /**
     * Constructs a new fetcher with the default URLs for omdbapi.com
     * 
     * @param apiKey The API key for omdbapi.com
     */
    public OmdbMovieListFetcher(String apiKey) {
        this(DEFAULT_LIST_URL_PATTERN, DEFAULT_DETAILS_URL_PATTERN, apiKey);
    }

    /**
     * Constructs a new fetcher with user defined URLs
     * 
     * The following template parameters should be present:
     * <ul>
     *   <li>apiKey</li>
     *   <li>searchText</li>
     *   <li>imdbId</li>
     * </ul>
     * 
     * See the class documentation for more details.
     * 
     * @param listUrlPattern Search list fetch URL
     * @param detailsUrlPattern Movie details fetch URL
     * @param apiKey The API key for omdbapi.com
     */
    public OmdbMovieListFetcher(String listUrlPattern, String detailsUrlPattern, String apiKey) {
        this.listUrlPattern = listUrlPattern;
        this.detailsUrlPattern = detailsUrlPattern;
        this.restTemplate = new RestTemplate();
        
        this.restTemplate.setDefaultUriVariables(Map.of("apiKey", apiKey));
    }
    
    
    @Override
    public List<Movie> findBySearchText(String searchText) {
        OmdbResult searchResult = restTemplate.getForEntity(
                listUrlPattern, OmdbResult.class, Map.of("searchText", searchText)).getBody();
        
        return searchResult.getItems().stream()
                .map(OmdbResultItem::getImdbId)
                .map(this::findOneByImdbId)
                .collect(Collectors.toList());
    }

    private Movie findOneByImdbId(String imdbId) {
        OmdbMovie movieData = restTemplate.getForEntity(
                detailsUrlPattern, OmdbMovie.class, Map.of("imdbId", imdbId)).getBody();
        
        return movieData.toMovie();
    }
    
    
    private static class OmdbResult {
        
        private final List<OmdbResultItem> items;
        
        @SuppressWarnings("unused") // this is for jackson
        public OmdbResult(@JsonProperty(RESULT_FIELD) List<OmdbResultItem> items) {
            this.items = items == null ? List.of() : new ArrayList<>(items);
        }
        
        public List<OmdbResultItem> getItems() {
            return new ArrayList<>(items);
        }
        
    }
    
    
    private static class OmdbResultItem {
        
        private final String imdbId;

        @SuppressWarnings("unused") // this is for jackson
        public OmdbResultItem(@JsonProperty(IMDB_ID_FIELD) String imdbId) {
            this.imdbId = imdbId;
        }
        
        public String getImdbId() {
            return imdbId;
        }
        
    }
    
    
    private static class OmdbMovie {
        
        private final String title;
        
        private final String year;
        
        private final String directorName;
        

        @SuppressWarnings("unused") // this is for jackson
        public OmdbMovie(
                @JsonProperty(TITLE_FIELD) String title,
                @JsonProperty(YEAR_FIELD) String year,
                @JsonProperty(DIRECTOR_NAME_FIELD) String directorName) {
            this.title = title;
            this.year = year;
            this.directorName = directorName;
        }
        
        
        public Movie toMovie() {
            return new Movie(title, ParseUtil.parseYear(year), directorName);
        }
        
    }
    
}
