package ru.iu3.rpo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.rpo.backend.models.Museum;
import ru.iu3.rpo.backend.repositories.MuseumRepository;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class MuseumController {
    @Autowired
    MuseumRepository museumRepository;

    @GetMapping("/museums")
    public List<Museum> getAllMuseums() {
        return museumRepository.findAll();
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

    @DeleteMapping("/museums/{id}")
    public Map<String, Boolean> deleteMuseum(@PathVariable(value = "id") Long museumId) {
        Optional<Museum> museum = museumRepository.findById(museumId);
        Map<String, Boolean> response = new HashMap<>();
        if (museum.isPresent())
        {
            museumRepository.delete(museum.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
        {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }
}