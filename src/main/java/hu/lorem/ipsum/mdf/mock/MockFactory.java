package hu.lorem.ipsum.mdf.mock;

import java.time.Year;
import java.util.List;

import org.springframework.stereotype.Component;

import hu.lorem.ipsum.mdf.fetch.MockMovieListFetcher;
import hu.lorem.ipsum.mdf.model.Movie;

@Component
public class MockFactory {
    
    public MockMovieListFetcher createMockMovieListFetcher() {
        return new MockMovieListFetcher(createMovieList());
    }

    public List<Movie> createMovieList() {
        return List.of(
                new Movie("Metropolis", Year.of(1927), "Fritz Lang"),
                new Movie("Rashomon", Year.of(1950), "Akira Kurosawa"),
                new Movie("Paths of Glory", Year.of(1957), "Stanley Kubrick"),
                new Movie("12 Angry Men", Year.of(1957), "Sidney Lumet"),
                new Movie("Pickpocket", Year.of(1959), "Robert Bresson"),
                new Movie("Duel", Year.of(1974), "Steven Spielberg"),
                new Movie("The Mirror", Year.of(1975), "Andrey Tarkovsky"),
                new Movie("Fanny and Alexander", Year.of(1982), "Ingmar Bergman"),
                new Movie("Blade Runner", Year.of(1982), "Ridley Scott"),
                new Movie("The Day After", Year.of(1983), "Nicholas Meyer"),
                new Movie("Unforgiven", Year.of(1992), "Clint Eastwood"),
                new Movie("Se7en", Year.of(1995), "David Fincher"),
                new Movie("Children of Men", Year.of(2006), "Alfonso Cuarón"),
                new Movie("The House That Jack Built", Year.of(2018), "Lars von Trier"),
                new Movie("Ad Astra", Year.of(2019), "James Gray"));
    }
    
}
