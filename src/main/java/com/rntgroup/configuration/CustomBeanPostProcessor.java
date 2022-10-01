package com.rntgroup.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.db.EventDatabase;
import com.rntgroup.db.TicketDatabase;
import com.rntgroup.db.UserDatabase;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Map;

@Slf4j
public class CustomBeanPostProcessor implements BeanPostProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

    @Setter
    private String eventDataFilePath;
    @Setter
    private String ticketDataFilePath;
    @Setter
    private String userDataFilePath;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        log.debug("Bean with name '{}' successfully created", beanName);

        Class<?> beanClass = bean.getClass();

        if (beanClass.equals(EventDatabase.class)) {
            loadEventData(bean);
            log.debug("Data loaded successfully in {} from {}", beanClass.getSimpleName(), eventDataFilePath);
        }

        if (beanClass.equals(TicketDatabase.class)) {
            loadTicketData(bean);
            log.debug("Data loaded successfully in {} from {}", beanClass.getSimpleName(), ticketDataFilePath);
        }

        if (beanClass.equals(UserDatabase.class)) {
            loadUserData(bean);
            log.debug("Data loaded successfully in {} from {}", beanClass.getSimpleName(), userDataFilePath);
        }

        return bean;
    }

    private void loadEventData(Object bean) {
        Map<Long, Event> data;

        try {
            data = objectMapper.readValue(
                    readResourceAsString(eventDataFilePath),
                    new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("data")) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, data);
            }
        }
    }

    private void loadTicketData(Object bean) {
        Map<Long, Ticket> data;

        try {
            data = objectMapper.readValue(
                    readResourceAsString(ticketDataFilePath),
                    new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("data")) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, data);
            }
        }
    }

    private void loadUserData(Object bean) {
        Map<Long, User> data;

        try {
            data = objectMapper.readValue(
                    readResourceAsString(userDataFilePath),
                    new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals("data")) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, bean, data);
            }
        }
    }

    private String readResourceAsString(String filePath) {
        try {
            return Files.readString(new ClassPathResource(filePath).getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
