package com.example.bp.analysis.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public record IrisSectorModel(
        @JsonProperty("name") String name,
        @JsonProperty("startAngle") double startAngle,
        @JsonProperty("endAngle") double endAngle,
        @JsonProperty("innerRadius") double innerRadius,
        @JsonProperty("outerRadius") double outerRadius
) {
    @Contract(pure = true)
    public @NonNull Boolean covers(double angle, double normalizedRadius) {
        boolean angleMatch = (startAngle > endAngle)
                ? (angle >= startAngle || angle <= endAngle)
                : (angle >= startAngle && angle <= endAngle);

        return angleMatch && (normalizedRadius >= innerRadius && normalizedRadius <= outerRadius);
    }
}
