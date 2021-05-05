package ru.iu3.rpo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.iu3.rpo.backend.models.Painting;
import ru.iu3.rpo.backend.repositories.PaintingRepository;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class PaintingController {
    @Autowired
    PaintingRepository paintingRepository;

    @GetMapping("/paintings")
    public List<Painting> getAllPaintings() {
        return paintingRepository.findAll();
    }

    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@Valid @RequestBody Painting painting) {
        Painting nc = paintingRepository.save(painting);
        return new ResponseEntity<Object>(nc, HttpStatus.OK);
    }

    @PutMapping("/paintings/{id}")
    public ResponseEntity<Painting> updatePainting(@PathVariable(value = "id") Long paintingId,
                                               @Valid @RequestBody Painting paintingDetails) {
        Painting painting = null;
        Optional<Painting> cc = paintingRepository.findById(paintingId);
        if (cc.isPresent())
        {
            painting = cc.get();
            painting.name = paintingDetails.name;
            paintingRepository.save(painting);
        }
        else
        {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "painting not found"
            );
        }
        return ResponseEntity.ok(painting);
    }

    @DeleteMapping("/painting/{id}")
    public Map<String, Boolean> deletePainting(@PathVariable(value = "id") Long paintingId) {
        Optional<Painting> painting = paintingRepository.findById(paintingId);
        Map<String, Boolean> response = new HashMap<>();
        if (painting.isPresent())
        {
            paintingRepository.delete(painting.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
        {
            response.put("deleted", Boolean.FALSE);
        }
        return response;
    }
}
