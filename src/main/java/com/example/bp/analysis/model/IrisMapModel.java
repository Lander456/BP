package com.example.bp.analysis.model;

import com.example.bp.api.dto.IrisMapDetailDto;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class IrisMapModel {
    private final List<IrisSectorModel> sectors;

    public IrisMapModel(@NonNull IrisMapDetailDto dto) {
        this.sectors = dto.sectors().stream()
                .map(s -> new IrisSectorModel(s.name(), s.startAngle(), s.endAngle(), s.innerRadius(), s.outerRadius()))
                .toList();
    }

    public String findOrganAt(double angle, double normalizedRadius) {
        return sectors.stream()
                .filter(s -> s.covers(angle, normalizedRadius))
                .map(IrisSectorModel::name)
                .findFirst()
                .orElse("Unknown");
    }
}
