package com.digitaltwin.central.dto;

public class ArtistResponseDto {

    private Long id;
    private String name;
    private String genre;
    private String bio;
    private String country;
    private String imageUrl;

    public ArtistResponseDto(Long id, String name, String genre, String bio, String country, String imageUrl) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.bio = bio;
        this.country = country;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getBio() {
        return bio;
    }

    public String getCountry() {
        return country;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
