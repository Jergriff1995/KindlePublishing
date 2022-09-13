package com.amazon.ata.resources.maps.gpmreminder.model;

/**
 * Class used to send email reminders to GPMs so they update their DevicePart data in AgilePLM before the
 * monthly report is generated.
 *
 * A separate system turns these into emails, using a beautiful templating system.
 */
public class GpmPartReminder {
    private String reminderText;
    private String email;

    /**
     * Constructs a GpmPartReminder from a reminder text and an email address.
     *
     * The reminder is processed into an email later by a different system.
     * @param reminderText Text to be substituted into a template, indicating the subject of the reminder.
     * @param email The destination of the reminder.
     */
    public GpmPartReminder(String reminderText, String email) {
        this.reminderText = reminderText;
        this.email = email;
    }

    public String getReminderText() {
        return reminderText;
    }

    public String getEmail() {
        return email;
    }

}
