package com.example.sid;

import com.example.sid.DAO.CinemaRepository;
import com.example.sid.DAO.SeanceReposiory;
import com.example.sid.Service.ICinemaInetService;
import com.example.sid.entits.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@SpringBootApplication
public class CinemaProjectApplication  implements CommandLineRunner {

    @Autowired
    private ICinemaInetService iCinemaInetService;

    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private RepositoryRestConfiguration repositoryRestConfiguration;
    public static void main(String[] args) {
        SpringApplication.run(CinemaProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repositoryRestConfiguration.exposeIdsFor(Film.class,Salle.class,Ticket.class,Ville.class,Cinema.class);

      /*  iCinemaInetService.initVilles();
        iCinemaInetService.initCinemas();
        iCinemaInetService.initSalles();
        iCinemaInetService.initPlaces();
        iCinemaInetService.initSeances();
        iCinemaInetService.initCategorise();
        iCinemaInetService.initFilmes();
        iCinemaInetService.initProjection();
        iCinemaInetService.initTicket();*/
    }
}
