package com.rntgroup.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class FileReader {

    public String readResourceAsString(String filePath) throws IOException {
        return Files.readString(new ClassPathResource(filePath).getFile().toPath());
    }

}
