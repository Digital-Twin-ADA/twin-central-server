package com.digitaltwin.central.controller;

import com.digitaltwin.central.dto.ArtistResponseDto;
import com.digitaltwin.central.service.ArtistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public List<ArtistResponseDto> getAllArtists() {
        return artistService.getAllArtists();
    }

    @GetMapping("/{id}")
    public ArtistResponseDto getArtistById(@PathVariable Long id) {
        return artistService.getArtistById(id);
    }
}
