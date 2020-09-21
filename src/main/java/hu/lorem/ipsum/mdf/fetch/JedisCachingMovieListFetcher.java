package hu.lorem.ipsum.mdf.fetch;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.lorem.ipsum.mdf.model.Movie;
import redis.clients.jedis.Jedis;

/**
 * Simple caching decorator for {@link MovieListFetcher} instances, Redis based
 */
public class JedisCachingMovieListFetcher implements MovieListFetcher {
    
    private static final String DEFAULT_CACHE_NAME = "default";
    
    
    private static final Logger logger =
            LoggerFactory.getLogger(JedisCachingMovieListFetcher.class);
    
    
    private final MovieListFetcher innerFetcher;
    
    private final Jedis jedis;
    
    private final String cacheName;

    private final ObjectMapper objectMapper;
    

    private JedisCachingMovieListFetcher(Builder builder) {
        this.innerFetcher = builder.innerFetcher;
        this.jedis = builder.jedis;
        this.cacheName = builder.cacheName.orElse(DEFAULT_CACHE_NAME);
        this.objectMapper = builder.objectMapper.orElseGet(ObjectMapper::new);
    }
    
    
    public static Builder builder() {
        return new Builder();
    }
    
    
    @Override
    public List<Movie> findBySearchText(String searchText) {
        Optional<List<Movie>> existingData = load(searchText);
        if (existingData.isPresent()) {
            return existingData.get();
        }

        List<Movie> newData = innerFetcher.findBySearchText(searchText);
        tryStore(searchText, newData);
        
        return newData;
    }
    
    private Optional<List<Movie>> load(String searchText) {
        String existingDataJson = jedis.get(composeKeyFor(searchText));
        if (existingDataJson == null) {
            return Optional.empty();
        }
        
        try {
            return Optional.of(deserialize(existingDataJson));
        } catch (IOException e) {
            logger.error(String.format("Failed to deserialize content of '%s'", searchText), e);
            return Optional.empty();
        }
    }
    
    private void tryStore(String searchText, List<Movie> data) {
        String serializedData;
        try {
            serializedData = serialize(data);
        } catch (IOException e) {
            logger.error(String.format("Failed to serialize content for '%s'", searchText), e);
            return;
        }

        jedis.set(composeKeyFor(searchText), serializedData);
    }
    
    private String composeKeyFor(String searchText) {
        return String.format("%s/%s", cacheName, searchText);
    }
    
    private List<Movie> deserialize(String serializedContent) throws IOException {
        return List.of(objectMapper.readValue(serializedContent, Movie[].class));
    }

    private String serialize(List<Movie> movies) throws IOException {
        return objectMapper.writeValueAsString(movies);
    }
    
    
    public static final class Builder {
        
        private MovieListFetcher innerFetcher;
        
        private Jedis jedis;
        
        private Optional<String> cacheName = Optional.empty();
        
        private Optional<ObjectMapper> objectMapper = Optional.empty();
        
        
        private Builder() {
            // use builder() instead
        }
        

        public WithInnerFetcher withInnerFetcher(MovieListFetcher innerFetcher) {
            this.innerFetcher = innerFetcher;
            return new WithInnerFetcher();
        }

        
        public final class WithInnerFetcher {

            public Optionals withJedis(Jedis jedis) {
                Builder.this.jedis = jedis;
                return new Optionals();
            }
            
        }

        
        public final class Optionals {

            public Optionals withCacheName(String cacheName) {
                Builder.this.cacheName = Optional.of(cacheName);
                return this;
            }

            public Optionals withObjectMapper(ObjectMapper objectMapper) {
                Builder.this.objectMapper = Optional.of(objectMapper);
                return this;
            }
            
            public JedisCachingMovieListFetcher build() {
                return new JedisCachingMovieListFetcher(Builder.this);
            }
            
        }

    }
    
    
}
