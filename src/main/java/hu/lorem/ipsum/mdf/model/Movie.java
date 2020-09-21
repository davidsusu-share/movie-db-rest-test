package hu.lorem.ipsum.mdf.model;

import java.time.Year;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class Movie {

    private final String title;
    
    private final Year year;
    
    private final String directorName;
    

    public Movie(
            @JsonProperty("title") String title,
            @JsonProperty("year") Year year,
            @JsonProperty("directorName") String directorName) {
        this.title = title;
        this.year = year;
        this.directorName = directorName;
    }
    
    
    public String getTitle() {
        return title;
    }

    public Year getYear() {
        return year;
    }

    public String getDirectorName() {
        return directorName;
    }
    

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(title)
                .append(year)
                .append(directorName)
                .build();
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Movie)) {
            return false;
        }
        
        Movie otherMovie = (Movie) other;
        return new EqualsBuilder()
                .append(title, otherMovie.title)
                .append(year, otherMovie.year)
                .append(directorName, otherMovie.directorName)
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("title", title)
                .append("year", year)
                .append("directorName", directorName)
                .toString();
    }
    
}
