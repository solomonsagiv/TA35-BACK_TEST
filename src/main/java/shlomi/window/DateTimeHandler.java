package shlomi.window;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DateTimeHandler {

    LocalTime time;
    LocalDate date;

    List< Day > days = new ArrayList<>( );

    Day day;

    public LocalTime getTime() {
        return time;
    }

    public void setTime( String time ) {
        this.time = LocalTime.parse( time );
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate( String date ) {
        this.date = LocalDate.parse( date );
    }

    public Day getDay() {
        return day;
    }

}
