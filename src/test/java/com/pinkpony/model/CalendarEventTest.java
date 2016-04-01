package com.pinkpony.model;

import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class CalendarEventTest {

    @Test
    public void testAddRsvp() throws Exception {
        CalendarEvent event = new CalendarEvent();
        Rsvp rsvp = new Rsvp();
        rsvp.setResponse("yes");
        rsvp.setUsername("Hermione");
        event.addRsvp(rsvp);

        assertThat(event.getRsvps(), hasItem(rsvp));
    }
}