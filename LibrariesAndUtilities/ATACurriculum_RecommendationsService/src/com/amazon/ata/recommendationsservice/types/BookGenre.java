package com.amazon.ata.recommendationsservice.types;

/**
 * This is a direct copy from the ATACurriculumKindlePublishingServiceModel. If this is not kept in sync, we will fail
 * to submit books for publishing if their genre is not included here.
 */
public enum BookGenre {
    FANTASY, ROMANCE, MYSTERY, SCIENCE_FICTION, TRAVEL, COOKING, AUTOBIOGRAPHY, ACTION, ADVENTURE,
    HISTORICAL_FICTION, HORROR, LITERARY_FICTION, HISTORY;
}
