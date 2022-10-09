package com.rntgroup;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.rntgroup.dto.EventDto;
import com.rntgroup.dto.TicketDto;
import com.rntgroup.dto.UserDto;
import com.rntgroup.enumerate.Category;
import com.rntgroup.model.Event;
import com.rntgroup.model.Ticket;
import com.rntgroup.model.User;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@UtilityClass
public class TestDataUtil {

    public static final Pageable DEFAULT_PAGE_REQUEST = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

    private static final Faker FAKER = new Faker();

    public Event getRandomEvent() {
        return new Event()
                .setTitle(FAKER.book().title())
                .setDate(LocalDate.now().plusDays(getRandomPositiveLong(1000)))
                .setPrice(getRandomPositiveBigDecimal(BigDecimal.valueOf(1000)));
    }

    public Event getRandomEvent(UUID id) {
        return getRandomEvent().setId(id);
    }

    public EventDto getRandomEventDto() {
        return new EventDto()
                .setTitle(FAKER.book().title())
                .setDate(LocalDate.now().plusDays(getRandomPositiveLong(1000)))
                .setPrice(getRandomPositiveBigDecimal(BigDecimal.valueOf(1000)));
    }

    public EventDto getRandomEventDto(UUID id) {
        return getRandomEventDto().setId(id);
    }

    public User getRandomUser() {
        String fakeName = FAKER.name().firstName();

        return new User()
                .setName(fakeName)
                .setEmail(fakeName + "_" + UUID.randomUUID() + "@fakemail.com");
    }

    public User getRandomUser(UUID id) {
        return getRandomUser().setId(id);
    }

    public UserDto getRandomUserDto() {
        Name fakeName = FAKER.name();

        return new UserDto()
                .setName(fakeName.firstName())
                .setEmail(fakeName + "_" + fakeName.lastName() + "@fakemail.com");
    }

    public UserDto getRandomUserDto(UUID id) {
        return getRandomUserDto().setId(id);
    }

    public Ticket getRandomTicket() {
        int enumPosition = (int) getRandomPositiveLong(Category.values().length - 1);

        return new Ticket()
                .setPlace((int) getRandomPositiveLong(200))
                .setCategory(Category.values()[enumPosition]);
    }

    public TicketDto getRandomTicketDto() {
        int enumPosition = (int) getRandomPositiveLong(Category.values().length - 1);

        return new TicketDto()
                .setPlace((int) getRandomPositiveLong(200))
                .setCategory(Category.values()[enumPosition]);
    }

    public Ticket getRandomBookedTicket(Event event, User user) {
        return getRandomTicket()
                .setEvent(event)
                .setUser(user);
    }

    public TicketDto getRandomBookedTicketDto(UUID eventId, UUID userId) {
        return getRandomTicketDto()
                .setEventId(eventId)
                .setUserId(userId);
    }

    public TicketDto getRandomBookedTicketDto(EventDto eventDto, UserDto userDto) {
        return getRandomTicketDto()
                .setEventId(eventDto.getId())
                .setUserId(userDto.getId());
    }

    private long getRandomPositiveLong(long bound) {
        return (long) (Math.random() * bound);
    }

    private BigDecimal getRandomPositiveBigDecimal(BigDecimal bound) {
        return bound.multiply(BigDecimal.valueOf(Math.random()));
    }
}
