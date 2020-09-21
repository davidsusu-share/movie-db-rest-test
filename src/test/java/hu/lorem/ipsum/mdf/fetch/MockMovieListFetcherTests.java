package hu.lorem.ipsum.mdf.fetch;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hu.lorem.ipsum.mdf.mock.MockFactory;
import hu.lorem.ipsum.mdf.model.Movie;

@SpringBootTest
class MockMovieListFetcherTests {
    
    @Autowired
    private MockFactory mockFactory;

    
    @Test
    void defaultFilterWorksProperly() {
        MockMovieListFetcher mockFetcher = mockFactory.createMockMovieListFetcher();
        
        List<String> filteredMovieTitles = mockFetcher.findBySearchText("men").stream()
                .map(Movie::getTitle)
                .collect(Collectors.toList());
                
        assertThat(filteredMovieTitles)
                .containsExactlyInAnyOrder("12 Angry Men", "Children of Men");
    }
    
}
