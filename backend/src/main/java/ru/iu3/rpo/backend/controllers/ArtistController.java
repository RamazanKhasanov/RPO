package ru.iu3.rpo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.rpo.backend.models.Artist;
import ru.iu3.rpo.backend.models.Country;
import ru.iu3.rpo.backend.repositories.ArtistRepository;
import ru.iu3.rpo.backend.repositories.CountryRepository;
import ru.iu3.rpo.backend.tools.DataValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    CountryRepository countryRepository;

    @GetMapping("/artists")
    public Page<Artist> getAllArtists(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return artistRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC,"name")));
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable(value = "id") Long artistId)
        throws DataValidationException
    {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(()-> new DataValidationException("Художник с таким индексом не найден"));
        return ResponseEntity.ok(artist);
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@Valid @RequestBody Artist artist){
       Optional<Country> cc = countryRepository.findById(artist.country.id);
       if (cc.isPresent())
       {
           artist.country = cc.get();
       }
       Artist nc = artistRepository.save(artist);
       return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable(value = "id") Long artistId,
                                                 @Valid @RequestBody Artist artistDetails) {
        Artist artist = null;
        Optional<Artist> cc = artistRepository.findById(artistId);
        if (cc.isPresent())
        {
            artist = cc.get();
            artist.name = artistDetails.name;
            artistRepository.save(artist);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "artist not found"
            );
        }
        return ResponseEntity.ok(artist);
    }

    @PostMapping("/deleteartists")
    public ResponseEntity deleteArtists(@Valid @RequestBody List<Artist> artists){
        artistRepository.deleteAll(artists);
        return new ResponseEntity(HttpStatus.OK);
    }
}
