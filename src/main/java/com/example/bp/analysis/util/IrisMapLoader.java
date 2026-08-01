package com.example.bp.analysis.util;

import com.example.bp.analysis.model.IrisMapModel;
import tools.jackson.databind.ObjectMapper;

import java.io.File;

public class IrisMapLoader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static IrisMapModel loadLocalMap(String filePath) throws Exception {
        return mapper.readValue(new File(filePath), IrisMapModel.class);
    }
}
