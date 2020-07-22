package com.example.sid.Service;

import com.example.sid.DAO.*;
import com.example.sid.entits.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Service
@Transactional
public class InetServiceImp implements ICinemaInetService {
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private VilleRepository villeRepository;
    @Autowired
    private SalleRepository salleRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private SeanceReposiory seanceReposiory;
    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ProjectionFilmRepository projectionFilmRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public void initVilles() {
        Stream.of("Casablanca", "Mohammedia", "Rabat", "Tange").forEach(v -> {
            Ville ville = new Ville();
            ville.setName(v);
            villeRepository.save(ville);
        });
    }

    @Override
    public void initCinemas() {
        villeRepository.findAll().forEach(v -> {
            Stream.of("megaraa", "Imax", "chahrazaf").forEach(cin -> {
                Cinema cinema = new Cinema();
                cinema.setName(cin);
                cinema.setNbSalle(3 + (int) (Math.random()) * 7);
                cinema.setVille(v);
                cinemaRepository.save(cinema);
            });
        });
    }

    @Override
    public void initSalles() {
        cinemaRepository.findAll().forEach(v -> {
            for (int i = 0; i < v.getNbSalle(); i++) {
                Salle salle = new Salle();
                salle.setName("salle" + (i + 1));
                salle.setCinema(v);
                salle.setNbPlace(15 + (int) (Math.random() * 20));
                salleRepository.save(salle);
            }
        });
    }

    @Override
    public void initPlaces() {
        salleRepository.findAll().forEach(v -> {
            for (int i = 0; i < v.getNbPlace(); i++) {
                Place place = new Place();
                place.setNumero(i + 1);
                place.setSalle(v);
                placeRepository.save(place);
            }
        });
    }

    @Override
    public void initSeances() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Stream.of("10:30", "11:20", "15:30", "17:00").forEach(date -> {
            Seance seance = new Seance();
            try {
                seance.setHeureDebut(simpleDateFormat.parse(date));
                seanceReposiory.save(seance);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void initCategorise() {
        Stream.of("action", "drama", "Histoir", "Fiction").forEach(v -> {
            Categorie categorie = new Categorie();
            categorie.setName(v);
            categorieRepository.save(categorie);
        });
    }

    @Override
    public void initFilmes() {
        List<Categorie> categories = categorieRepository.findAll();
        Stream.of("the Avengers", "spiderman").forEach(t -> {
            Film film = new Film();
            film.setTitle(t);
            film.setDure(Math.random() * 7);
            film.setDateSortir(new Date());
            film.setDescription(t);
            film.setCategorie(categories.get(new Random().nextInt(categories.size())));
            film.setPhoto(t.trim().replace(" ", ""));
            filmRepository.save(film);
        });

    }

    @Override
    public void initProjection() {
        Long[] a = {Long.valueOf(1), Long.valueOf(2)};
        villeRepository.findAll().forEach(v -> {
            v.getCinemas().forEach(c -> {
                c.getSalles().forEach(s -> {
                    seanceReposiory.findAll().forEach(se -> {
                        ProjectionFilm projectionFilm = new ProjectionFilm();
                        projectionFilm.setDateProjection(new Date());
                        projectionFilm.setPrix(30);
                        projectionFilm.setSalle(s);
                        projectionFilm.setSeance(se);
                        Film f = filmRepository.findById(Long.valueOf (1+((int)Math.random()*2))).get();
                        projectionFilm.setFilm(f);
                        projectionFilmRepository.save(projectionFilm);
                    });
                });
            });
        });
    }

    @Override
    public void initTicket() {
        projectionFilmRepository.findAll().forEach(p -> {
            p.getSalle().getPlaces().forEach(place -> {
                Ticket ticket = new Ticket();
                ticket.setPlace(place);
                ticket.setPrix(p.getPrix());
                ticket.setProjectionFilm(p);
                ticketRepository.save(ticket);
            });
        });
    }
}
