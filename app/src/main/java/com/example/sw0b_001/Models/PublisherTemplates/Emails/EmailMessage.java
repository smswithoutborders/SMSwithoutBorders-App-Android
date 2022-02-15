package com.example.sw0b_001.Models.PublisherTemplates.Emails;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.sw0b_001.Helpers.CustomHelpers;

@Entity
public class EmailMessage {
    @ColumnInfo(name="recipient")
    public String recipient;

    @ColumnInfo(name="subject")
    public String subject;

    @ColumnInfo(name="body")
    public String body;

    public String getRecipient() {
        return recipient;
    }

    public EmailMessage setRecipient(String recipient) {
        this.setImage(CustomHelpers.getLetterImage(recipient.charAt(0)));
        this.recipient = recipient;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public EmailMessage setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public EmailMessage setBody(String body) {
        this.body = body;
        return this;
    }

    public String getDatetime() {
        return datetime;
    }

    public EmailMessage setDatetime(String datetime) {
        this.datetime = datetime;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public EmailMessage setStatus(String status) {
        this.status = status;
        return this;
    }

    public int getImage() {
        return image;
    }

    public EmailMessage setImage(int image) {
        this.image = image;
        return this;
    }

    public long getThreadId() {
        return threadId;
    }

    public EmailMessage setThreadId(long threadId) {
        this.threadId = threadId;
        return this;
    }

    public long getId() {
        return id;
    }

    public EmailMessage setId(long id) {
        this.id = id;
        return this;
    }

    @ColumnInfo(name="datetime")
    public String datetime;

    @ColumnInfo(name="status")
    public String status;

    @ColumnInfo(name="image")
    public int image;

    @ColumnInfo(name="thread_id")
    public long threadId;

    @PrimaryKey(autoGenerate = true)
    public long id;
}
