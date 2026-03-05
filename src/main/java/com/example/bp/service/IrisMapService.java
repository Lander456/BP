package com.example.bp.service;

import com.example.bp.api.dto.IrisMapCreateDto;
import com.example.bp.api.dto.IrisMapDetailDto;

public interface IrisMapService {
    IrisMapDetailDto upload(IrisMapCreateDto dto);
}
