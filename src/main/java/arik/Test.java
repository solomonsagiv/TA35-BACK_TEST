package arik;

import com.google.gson.internal.bind.util.ISO8601Utils;
import org.jfree.data.time.Millisecond;

import java.time.LocalTime;
import java.util.Date;

public class Test {



    public static void main( String[] args ) {
        LocalTime time = LocalTime.now();
        System.out.println(time.getNano() );

        System.out.println(new Millisecond( new Date( time.getNano() ) ) );

    }


}
