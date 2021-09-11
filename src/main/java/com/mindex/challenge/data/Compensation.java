package com.mindex.challenge.data;

import java.lang.Math.*;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.*;

import java.io.IOException;

public class Compensation {
    private Employee employee;
    private double salary;

    @JsonSerialize(using=CalendarSerializer.class)
    @JsonDeserialize(using=CalendarDeserializer.class)
    private Calendar effectiveDate;

    public Compensation() {

    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public double getSalary() {
        return Math.floor(this.salary * 100) / 100;
    }

    public void setSalary(double salary) {
        this.salary = Math.floor(salary * 100) / 100;
    }

    public Calendar getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(Calendar effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

}

class CalendarSerializer extends JsonSerializer<Calendar> {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public void serialize(Calendar calendar, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {

        String dateAsString = formatter.format(calendar.getTime());
        jsonGenerator.writeString(dateAsString);
    }
}

class CalendarDeserializer extends JsonDeserializer<Calendar> {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

    @Override
    public Calendar deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext)
            throws IOException {

        String dateAsString = jsonParser.getText();

        try {
            Date date = formatter.parse(dateAsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}