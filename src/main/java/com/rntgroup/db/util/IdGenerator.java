package com.rntgroup.db.util;

import com.rntgroup.model.Entity;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class IdGenerator {

    public static <T extends Entity<Long>> long generate(Map<Long, T> data) {
        long generatedId = data.size();

        for (long i = 0; i < data.size(); i++) {
            if (!data.containsKey(i)) {
                generatedId = i;
                break;
            }
        }

        return generatedId;
    }
}
