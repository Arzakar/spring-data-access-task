package com.rntgroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;

@UtilityClass
public class TestUtil {

    @SneakyThrows
    public String readResourceAsString(String filePath) {
        return Files.readString(new ClassPathResource(filePath).getFile().toPath());
    }

    public Date createDate(int year, int month, int day) {
        return new Calendar.Builder().setDate(year, month, day).build().getTime();
    }

    @SneakyThrows
    public <T> T deepCopy(T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        return (T) objectMapper.readValue(objectMapper.writeValueAsString(object), object.getClass());
    }

}
