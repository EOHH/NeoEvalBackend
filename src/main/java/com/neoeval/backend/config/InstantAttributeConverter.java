package com.neoeval.backend.config; // Crea este paquete si no existe

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Timestamp;
import java.time.Instant;

// Con @Converter(autoApply = true), JPA lo usará para todos los campos Instant
@Converter(autoApply = true)
public class InstantAttributeConverter implements AttributeConverter<Instant, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(Instant instant) {
        // Convierte Instant (UTC) directamente a un Timestamp SQL.
        // JPA lo guardará asumiendo que es UTC (que es la configuración de la DB).
        return instant == null ? null : Timestamp.from(instant);
    }

    @Override
    public Instant convertToEntityAttribute(Timestamp timestamp) {
        // Convierte el Timestamp de la DB de vuelta a Instant (UTC).
        return timestamp == null ? null : timestamp.toInstant();
    }
}