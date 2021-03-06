package com.pinkpony.validator;

import com.pinkpony.model.Rsvp;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;

import static org.hamcrest.Matchers.hasItemInArray;
import static org.junit.Assert.*;

public class RsvpBaseValidatorTest {
    RsvpBaseValidator validator;

    @Before
    public void setUp() {
        validator = new RsvpBaseValidator();
    }

    @Test
    public void validateBlanks() throws Exception {
        Rsvp rsvp = makeRsvp("", "");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(rsvp, "Rsvp");
        validator.validate(rsvp, errors);

        assertTrue(errors.getErrorCount() > 0);
        assertThat(errors.getFieldError("username").getCodes(), hasItemInArray("rsvp.username.field.empty"));
        assertThat(errors.getFieldError("response").getCodes(), hasItemInArray("rsvp.response.field.empty"));
    }

    @Test
    public void validateMissingResponse() throws Exception {
        Rsvp rsvp = new Rsvp();
        rsvp.setUsername("Hermione");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(rsvp, "Rsvp");
        validator.validate(rsvp, errors);

        assertTrue(errors.getErrorCount() > 0);
        assertNull(errors.getFieldError("username"));
        assertThat(errors.getFieldError("response").getCodes(), hasItemInArray("rsvp.response.field.empty"));
    }

    @Test
    public void validateInvalidResponse() throws Exception {
        Rsvp rsvp = makeRsvp("Hermione", "invalid");

        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(rsvp, "Rsvp");
        validator.validate(rsvp, errors);

        assertTrue(errors.getErrorCount() > 0);
        assertNull(errors.getFieldError("username"));
        assertThat(errors.getFieldError("response").getCodes(), hasItemInArray("rsvp.response.field.invalidValue"));

        rsvp.setResponse("yes");
        assertValid(rsvp, validator);

        rsvp.setResponse("no");
        assertValid(rsvp, validator);
    }

    private Rsvp makeRsvp(String username, String response) {
        Rsvp rsvp = new Rsvp();
        rsvp.setUsername(username);
        rsvp.setResponse(response);
        return rsvp;
    }

    private void assertValid(Object object, Validator validator) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(object, "_");
        validator.validate(object, errors);
        assertEquals(0, errors.getErrorCount());
    }
}
