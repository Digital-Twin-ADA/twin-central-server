package com.digitaltwin.central.service;

import com.digitaltwin.central.dto.ArtistResponseDto;
import com.digitaltwin.central.model.Artist;
import com.digitaltwin.central.repository.ArtistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<ArtistResponseDto> getAllArtists() {
        return artistRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ArtistResponseDto getArtistById(Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + id));

        return toResponse(artist);
    }

    private ArtistResponseDto toResponse(Artist artist) {
        return new ArtistResponseDto(
                artist.getId(),
                artist.getName(),
                artist.getGenre(),
                artist.getBio(),
                artist.getCountry(),
                artist.getImageUrl()
        );
    }
}
