package com.openmusic.api.entities.cache;

import com.openmusic.api.entities.database.AbstractTable;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;

/**
 * @author Ahmad Irfaan Hibatullah
 * @version $Id: SongsTemp.java, v 0.1 2021‐10‐29 18.44 Ahmad Irfaan Hibatullah Exp $$
 */
@RedisHash(value = "playlistsong", timeToLive = 3600)
public class SongsTemp {

    @Id
    private String id;

    private String title;

    private Integer year;

    private String performer;

    private String genre;

    private Integer duration;


    public String getTitle() {
        return title;
    }

    /**
     * Setter method for property title.
     *
     * @param title value to be assigned to property title
     */

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    /**
     * Setter method for property year.
     *
     * @param year value to be assigned to property year
     */

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPerformer() {
        return performer;
    }

    /**
     * Setter method for property performer.
     *
     * @param performer value to be assigned to property performer
     */

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public String getGenre() {
        return genre;
    }

    /**
     * Setter method for property genre.
     *
     * @param genre value to be assigned to property genre
     */

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getDuration() {
        return duration;
    }

    /**
     * Setter method for property duration.
     *
     * @param duration value to be assigned to property duration
     */

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}