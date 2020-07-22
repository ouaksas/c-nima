package com.example.sid.entits;

import org.springframework.data.rest.core.config.Projection;

import java.util.Collection;
import java.util.Date;

@Projection(name = "p1",types = {ProjectionFilm.class})
public interface ProjectionFilmProjection {
    public Long getId();
    public double getPrix();
    public Date getDateProjection();
    public Salle getSalle();
    public Film getFilm();
    public Seance getSeance();
    public Collection<Ticket> getTickets();
}
