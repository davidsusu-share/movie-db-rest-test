package hu.lorem.ipsum.mdf.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.lorem.ipsum.mdf.fetch.MovieListFetcher;
import hu.lorem.ipsum.mdf.model.Movie;
import hu.lorem.ipsum.mdf.statistics.SearchEventEntity;
import hu.lorem.ipsum.mdf.statistics.SearchEventRepository;

@RestController
@RequestMapping("/movies")
public class MoviesRestController {
    
    @Value("${app.collectStatistics:true}")
    private boolean collectStatistics;

    @Autowired
    private SearchEventRepository searchEventRepository;
    
    @Autowired
    @Qualifier("supportedMovieListFetchers")
    private Map<String, MovieListFetcher> supportedMovieListFetchers;
    
    
    @GetMapping("/{searchText}")
    public List<Movie> getMovies(
            @PathVariable("searchText") String searchText,
            @RequestParam("apiName") String apiName) {
        
        checkApiName(apiName);
        pushStatIfEnabled(searchText, apiName);
        return supportedMovieListFetchers.get(apiName).findBySearchText(searchText);
    }
    
    private void checkApiName(String apiName) {
        if (!supportedMovieListFetchers.containsKey(apiName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
    
    private void pushStatIfEnabled(String searchText, String apiName) {
        if (collectStatistics) {
            searchEventRepository.saveAndFlush(new SearchEventEntity(searchText, apiName));
        }
    }
    
}
