package hu.lorem.ipsum.mdf.fetch;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;

import hu.lorem.ipsum.mdf.model.Movie;
import hu.lorem.ipsum.mdf.util.ParseUtil;

/**
 * {@link MovieListFetcher} implementation which fetches data from api.themoviedb.org.
 * 
 * <p>By default the following URLs will be used:</p>
 * 
 * <ul>
 *   <li>Search results: https://api.themoviedb.org/3/search/
           movie?query={searchText}&amp;include_adult=true&amp;api_key={apiKey}</li>
 *   <li>Crew details: https://api.themoviedb.org/3/movie/{id}/credits?api_key={apiKey}</li>
 * </ul>
 * 
 * <p>Meaning of the template variables:</p>
 * 
 * <ul>
 *   <li>apiKey: the API key for api.themoviedb.org</li>
 *   <li>searchText: The given search text</li>
 *   <li>id: Unique movie ID</li>
 * </ul>
 * 
 * <p>
 *   Currently, paging is not supported explicitly.
 *   When using the default URLs, a limited number of results will be fetched.
 *   This limit is implicit, not defined in this implementation.
 *   Result order is also implicit.
 * </p>
 */
public class TmdbMovieListFetcher implements MovieListFetcher {
    
    private static final String DEFAULT_LIST_URL_PATTERN =
            "https://api.themoviedb.org/3/search/movie?" +
                    "query={searchText}&include_adult=true&api_key={apiKey}";
    
    private static final String DEFAULT_CREDITS_URL_PATTERN =
            "https://api.themoviedb.org/3/movie/{id}/credits?api_key={apiKey}";
    
    private static final String RESULT_FIELD = "results";
    
    private static final String ID_FIELD = "id";
    
    private static final String TITLE_FIELD = "title";
    
    private static final String DATE_FIELD = "release_date";
    
    private static final String CREW_FIELD = "crew";
    
    private static final String JOB_FIELD = "job";
    
    private static final String NAME_FIELD = "name";
    
    private static final String DIRECTOR_JOB_VALUE = "Director";


    private final String listUrlPattern;

    private final String creditsUrlPattern;
    
    private final RestTemplate restTemplate;
    

    /**
     * Constructs a new fetcher with the default URLs for api.themoviedb.org
     * 
     * @param apiKey The API key for api.themoviedb.org
     */
    public TmdbMovieListFetcher(String apiKey) {
        this(DEFAULT_LIST_URL_PATTERN, DEFAULT_CREDITS_URL_PATTERN, apiKey);
    }

    /**
     * Constructs a new fetcher with user defined URLs
     * 
     * The following template parameters should be present:
     * <ul>
     *   <li>apiKey</li>
     *   <li>searchText</li>
     *   <li>id</li>
     * </ul>
     * 
     * See the class documentation for more details.
     * 
     * @param listUrlPattern Search list fetch URL
     * @param creditsUrlPattern Credits fetch URL
     * @param apiKey The API key for api.themoviedb.org
     */
    public TmdbMovieListFetcher(String listUrlPattern, String creditsUrlPattern, String apiKey) {
        this.listUrlPattern = listUrlPattern;
        this.creditsUrlPattern = creditsUrlPattern;
        this.restTemplate = new RestTemplate();
        
        this.restTemplate.setDefaultUriVariables(Map.of("apiKey", apiKey));
    }
    
    
    @Override
    public List<Movie> findBySearchText(String searchText) {
        TmdbResult searchResult = restTemplate.getForEntity(
                listUrlPattern, TmdbResult.class, Map.of("searchText", searchText)).getBody();
        
        return searchResult.getItems().stream()
                .map(item -> new Movie(
                        item.getTitle(),
                        item.getYear(),
                        findDirectorNameById(item.getId()).orElseGet(() -> "")))
                .collect(Collectors.toList());
    }

    private Optional<String> findDirectorNameById(String id) {
        return findCreditsById(id).getItems().stream()
            .filter(item -> item.getJob().equals(DIRECTOR_JOB_VALUE))
            .map(TmdbCrewItem::getName)
            .findAny();
    }
    
    private TmdbCredits findCreditsById(String id) {
        return restTemplate.getForEntity(
                creditsUrlPattern, TmdbCredits.class, Map.of("id", id)).getBody();
    }
    
    
    private static class TmdbResult {
        
        private final List<TmdbResultItem> items;
        
        @SuppressWarnings("unused") // this is for jackson
        public TmdbResult(@JsonProperty(RESULT_FIELD) List<TmdbResultItem> result) {
            this.items = result == null ? List.of() :new ArrayList<>(result);
        }
        
        public List<TmdbResultItem> getItems() {
            return new ArrayList<>(items);
        }
        
    }
    
    
    private static class TmdbResultItem {
        
        private final String id;
        
        private final String title;
        
        private final String releaseDate;

        
        @SuppressWarnings("unused") // this is for jackson
        public TmdbResultItem(
                @JsonProperty(ID_FIELD) String id,
                @JsonProperty(TITLE_FIELD) String title,
                @JsonProperty(DATE_FIELD) String releaseDate) {
            this.id = id;
            this.title = title;
            this.releaseDate = releaseDate;
        }
        

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }
        public Year getYear() {
            return ParseUtil.parseYear(releaseDate);
        }
        
    }
    

    private static class TmdbCredits {

        private final List<TmdbCrewItem> crewItems;
        
        @SuppressWarnings("unused") // this is for jackson
        public TmdbCredits(@JsonProperty(CREW_FIELD) List<TmdbCrewItem> crewItems) {
            this.crewItems = crewItems == null ? List.of() :new ArrayList<>(crewItems);
        }
        
        public List<TmdbCrewItem> getItems() {
            return new ArrayList<>(crewItems);
        }
        
    }
    
    
    private static class TmdbCrewItem {
        
        private final String job;
        
        private final String name;
        

        @SuppressWarnings("unused") // this is for jackson
        public TmdbCrewItem(
                @JsonProperty(JOB_FIELD) String job,
                @JsonProperty(NAME_FIELD) String name) {
            this.job = job;
            this.name = name;
        }
        

        public String getJob() {
            return job;
        }

        public String getName() {
            return name;
        }
        
    }
    
}
