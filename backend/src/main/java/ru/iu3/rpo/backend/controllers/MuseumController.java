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
import ru.iu3.rpo.backend.models.Museum;
import ru.iu3.rpo.backend.repositories.MuseumRepository;
import ru.iu3.rpo.backend.tools.DataValidationException;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class MuseumController {
    @Autowired
    MuseumRepository museumRepository;

    @GetMapping("/museums")
    public Page<Museum> getAllMuseums(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return museumRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC,"name")));
    }

    @GetMapping("/museums/{id}")
    public ResponseEntity<Museum> getMuseum(@PathVariable(value = "id") Long museumId)
            throws DataValidationException
    {
        Museum museum = museumRepository.findById(museumId)
                .orElseThrow(()-> new DataValidationException("Музей с таким индексом не найден"));
        return ResponseEntity.ok(museum);
    }

    @PostMapping("/museums")
    public ResponseEntity<Object> createMuseum(@Valid @RequestBody Museum museum) {
        Museum nc = museumRepository.save(museum);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    @PutMapping("/museums/{id}")
    public ResponseEntity<Museum> updateMuseum(@PathVariable(value = "id") Long museumId,
                                                 @Valid @RequestBody Museum museumDetails) {
        Museum museum = null;
        Optional<Museum> cc = museumRepository.findById(museumId);
        if (cc.isPresent())
        {
            museum = cc.get();
            museum.name = museumDetails.name;
            museumRepository.save(museum);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "museum not found"
            );
        }
        return ResponseEntity.ok(museum);
    }

    @PostMapping("/deletemuseums")
    public ResponseEntity deleteMuseums(@Valid @RequestBody List<Museum> museums){
        museumRepository.deleteAll(museums);
        return new ResponseEntity(HttpStatus.OK);
    }
}