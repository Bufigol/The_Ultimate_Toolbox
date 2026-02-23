package com.the_ultimate_toolbox.util.date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Date Math Tests")
class DateMathTest {
    private Date fechaUno;
    private Date fechaDos;
    private LocalDate localDateUno;
    private LocalDate localDateDos;
    private String dateString;
    private SimpleDateFormat formatter;

    @BeforeEach
    void setUp(){
        this.formatter = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Test
    @DisplayName("dias entre fechas")
    void testDiasEntreFechas(){
        try {
            this.dateString = "01/01/2015";
            this.fechaUno = this.formatter.parse(dateString);
            this.dateString = "02/01/2015";
            this.fechaDos = this.formatter.parse(dateString);
            assertEquals(1, DateMath.diasEntreFechas(this.fechaUno, this.fechaDos));
            assertEquals(-1, DateMath.diasEntreFechas(this.fechaDos,this.fechaUno));
            this.localDateUno = LocalDate.of(2025,1, 1);
            this.localDateDos = LocalDate.of(2025,1, 2);
            assertEquals(1, DateMath.diasEntreFechas(this.localDateUno, this.localDateDos));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }
}
