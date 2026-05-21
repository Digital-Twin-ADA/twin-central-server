package com.digitaltwin.central.dto;

import java.time.OffsetDateTime;

public class LineupEventResponseDto {

    private Long id;
    private Long artistId;
    private String artistName;
    private String artistGenre;
    private Long stageId;
    private String stageName;
    private String stageZoneCode;
    private OffsetDateTime startsAt;
    private OffsetDateTime endsAt;
    private String title;
    private String status;

    public LineupEventResponseDto(Long id, Long artistId, String artistName, String artistGenre, Long stageId,
                                  String stageName, String stageZoneCode, OffsetDateTime startsAt,
                                  OffsetDateTime endsAt, String title, String status) {
        this.id = id;
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistGenre = artistGenre;
        this.stageId = stageId;
        this.stageName = stageName;
        this.stageZoneCode = stageZoneCode;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.title = title;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistGenre() {
        return artistGenre;
    }

    public Long getStageId() {
        return stageId;
    }

    public String getStageName() {
        return stageName;
    }

    public String getStageZoneCode() {
        return stageZoneCode;
    }

    public OffsetDateTime getStartsAt() {
        return startsAt;
    }

    public OffsetDateTime getEndsAt() {
        return endsAt;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }
}
