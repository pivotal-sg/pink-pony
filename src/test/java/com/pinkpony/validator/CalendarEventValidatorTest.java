package com.pinkpony.validator;

import com.pinkpony.model.CalendarEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CalendarEventValidatorTest {
    CalendarEventValidator validator;

    @Before
    public void setUp() {
        validator = new CalendarEventValidator();
    }

    @Test
    public void acceptsValidEvent() {
        assertValid(makeEvent(), validator);
    }

    @Test
    public void rejectsEmptyName() throws Exception {
        CalendarEvent event = makeEvent();
        event.setName("");
        assertInvalid(event, validator, "name", "calendarEvent.name.field.empty");
    }

    @Test
    public void rejectsNullName() throws Exception {
        CalendarEvent event = makeEvent();
        event.setName(null);
        assertInvalid(event, validator, "name", "calendarEvent.name.field.empty");
    }

    @Test
    public void rejectsEmptyDescription() throws Exception {
        CalendarEvent event = makeEvent();
        event.setDescription("");
        assertInvalid(event, validator, "description", "calendarEvent.description.field.empty");
    }

    @Test
    public void rejectsNullDescription() throws Exception {
        CalendarEvent event = makeEvent();
        event.setDescription(null);
        assertInvalid(event, validator, "description", "calendarEvent.description.field.empty");
    }

    @Test
    public void rejectsEmptyEventDateTime() throws Exception {
        CalendarEvent event = makeEvent();
        event.setCalendarEventDateTimeString("");
        assertInvalid(event, validator, "calendarEventDateTimeString", "calendarEvent.calendarEventDateTime.field.empty");
    }

    @Test
    public void rejectsNullEventDateTime() throws Exception {
        CalendarEvent event = makeEvent();
        event.setCalendarEventDateTimeString(null);
        assertInvalid(event, validator, "calendarEventDateTimeString", "calendarEvent.calendarEventDateTime.field.empty");
    }

    @Test
    public void rejectsEmptyVenue() {
        CalendarEvent event = makeEvent();
        event.setVenue("");
        assertInvalid(event, validator, "venue", "calendarEvent.venue.field.empty");
    }

    @Test
    public void rejectsNullVenue() {
        CalendarEvent event = makeEvent();
        event.setVenue(null);
        assertInvalid(event, validator, "venue", "calendarEvent.venue.field.empty");
    }

    @Test
    public void rejectsEmptyUsername() {
        CalendarEvent event = makeEvent();
        event.setUsername("");
        assertInvalid(event, validator, "username", "calendarEvent.username.field.empty");
    }

    @Test
    public void rejectsNullUsername() {
        CalendarEvent event = makeEvent();
        event.setUsername(null);
        assertInvalid(event, validator, "username", "calendarEvent.username.field.empty");
    }

    @Test
    public void rejectsEventDateTimeWithInvalidFormat() throws Exception {
        CalendarEvent event = makeEvent();
        event.setCalendarEventDateTimeString("2015-03-11T11:00:00"); // missing time zone
        assertInvalid(event, validator, "calendarEventDateTimeString", "calendarEvent.calendarEventDateTime.field.invalid");
    }

    private CalendarEvent makeEvent() {
        CalendarEvent newEvent = new CalendarEvent();
        newEvent.setName("Spring Boot Night");
        newEvent.setDescription("Wanna learn how to boot?");
        newEvent.setVenue("Arrowhead Lounge");
        newEvent.setCalendarEventDateTimeString("2016-03-18T14:33:00+00:00");
        newEvent.setUsername("Holly");

        return newEvent;
    }

    private void assertInvalid(CalendarEvent event, Validator validator, String property, String errorStringKey) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(event, "CalendarEvent");
        validator.validate(event, errors);

        assertEquals(1, errors.getErrorCount());
        assertThat(errors.getFieldError(property).getCodes(), hasItemInArray(errorStringKey));
    }

    private void assertValid(Object object, Validator validator) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(object, "_");
        validator.validate(object, errors);

        List<String> errorStrings = errors.getAllErrors().
                stream().
                map(ObjectError::toString).
                collect(Collectors.toList());
        assertEquals("Validation errors: \n" + String.join("\n",  errorStrings), 0, errors.getErrorCount());
    }
}
