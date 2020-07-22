package com.example.sid.Controllers;

import com.example.sid.DAO.*;
import com.example.sid.entits.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CinemaController {
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private ProjectionFilmRepository projectionFilmRepository;
    @Autowired
    private SeanceReposiory seanceReposiory;



    @GetMapping(value = "/imageFilm/{id}",produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] image(@PathVariable(name = "id") Long id) throws IOException {
        Film film=filmRepository.findById(id).get();
        String photo=film.getPhoto();
        File file=new File(System.getProperty("user.home")+"\\cinema\\"+photo+".jpg");
        System.out.println(System.getProperty("user.home")+"\\cinema\\"+photo+".jpg");
        Path path=Paths.get(file.toURI());
        return Files.readAllBytes(path);
    }

    @PostMapping("/payerTicker")
    @Transactional
    public List<Ticket> payerTicket(@RequestBody TicketForm ticketForm){
        List<Ticket> tick = new ArrayList();
        System.out.println(ticketForm.toString());
        ticketForm.getListTicket().forEach(v->{
           System.out.println(v);
            Ticket ticket=ticketRepository.findById(v).get();
            ticket.setNomClient(ticketForm.getNomClent());
            ticket.setReservee(true);
            ticket.setCodePayement(ticketForm.getCodePayment());
            tick.add(ticket);
            ticketRepository.save(ticket);

        });
        return tick;
    }
    @PostMapping("/ajouterCinema")
    @Transactional
    public void ajouterCinema(@RequestBody CinemaAdd cinemaAdd){
        Ville v=villeRepository.findById(cinemaAdd.getVille()).get();
        Cinema c=new Cinema();
        c.setName(cinemaAdd.getName());
        c.setNbSalle(cinemaAdd.getNbSalle());
        c.setVille(v);
        cinemaRepository.save(c);
        v.getCinemas().add(c);
        villeRepository.save(v);
    }
    @PostMapping("/ajouterSalle")
    @Transactional
    public void ajouterSalle(@RequestBody SalleAdd salleAdd){
        Cinema cinema=cinemaRepository.getOne(salleAdd.getCinema());
        cinema.setNbSalle(cinema.getNbSalle()+1);
        Salle s=new Salle();
        s.setNbPlace(salleAdd.getNbPlace());
        s.setName(salleAdd.getName());
        Collection<Place> places=new ArrayList<>();
        for(int i=0;i<salleAdd.getNbPlace();i++){
            Place p=new Place();
            p.setSalle(s);
            p.setNumero(i+1);
           places.add(p);
            placeRepository.save(p);
        }
        s.setPlaces(places);
        s.setCinema(cinema);
        salleRepository.save(s);
        cinema.getSalles().add(s);
    }
    @PostMapping("/addProj")
    @Transactional
    public void addProjection(@RequestBody AddProjection addProjection){
        Salle s=salleRepository.getOne(addProjection.getSalle());
        Film f=filmRepository.getOne(addProjection.getFilm());
        Seance se=new Seance();
        se.setHeureDebut(addProjection.getDateProjection());
        ProjectionFilm p=new ProjectionFilm();
        p.setFilm(f);
        p.setSalle(s);
        p.setDateProjection(addProjection.getDateProjection());
        p.setPrix(addProjection.getPrix());
        Collection<Ticket> tickets=new ArrayList<>();
        for (Place pl:s.getPlaces()) {
            Ticket t=new Ticket();
            t.setNomClient("");
            t.setPrix(addProjection.getPrix());
            t.setReservee(false);
            t.setPlace(pl);
            pl.getTickets().add(t);
            t.setProjectionFilm(p);
            ticketRepository.save(t);
            placeRepository.save(pl);
        }
        p.setSeance(se);
        seanceReposiory.save(se);
        salleRepository.save(s);
        filmRepository.save(f);
        projectionFilmRepository.save(p);



    }

}
@Data
class AddProjection{
    private Date dateProjection;
    private Date HeureDebut;
    private double prix;
    private Long Film;
    private Long salle;
}

@Data
class SalleAdd{
    private String name;
    private int nbPlace;
    private Long Cinema;
}
@Data
class CinemaAdd{
    private String name;
    private int nbSalle;
    private Long ville;
}

@Data
class TicketForm{
    private List<Long> listTicket=new ArrayList<>();
    private String nomClent;
    private int codePayment;

}
