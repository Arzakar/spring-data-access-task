package com.rntgroup;

import com.rntgroup.model.Event;
import lombok.experimental.UtilityClass;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;

@UtilityClass
public class TestDataUtil {

    public Event getRandomEvent() {
        var parameters = new EasyRandomParameters();
        parameters.excludeField(FieldPredicates.named("id").and(FieldPredicates.inClass(Event.class)));

        return new EasyRandom(parameters).nextObject(Event.class);
    }

}
