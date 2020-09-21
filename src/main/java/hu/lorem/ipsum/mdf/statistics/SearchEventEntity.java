package hu.lorem.ipsum.mdf.statistics;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="search_event")
public class SearchEventEntity {

    @Id
    @GeneratedValue
    @Column(name = "search_event_id", updatable = false)
    private Long id;

    @Column(name = "search_term")
    private String searchTerm;

    @Column(name = "api_name")
    private String apiName;

    @CreationTimestamp
    @Column(name = "served_at", updatable = false)
    private Calendar servedAt;
    

    public SearchEventEntity() {
    }
    
    public SearchEventEntity(String searchTerm, String apiName) {
        this.searchTerm = searchTerm;
        this.apiName = apiName;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public Calendar getServedAt() {
        return servedAt;
    }

    public void setServedAt(Calendar servedAt) {
        this.servedAt = servedAt;
    }

}
