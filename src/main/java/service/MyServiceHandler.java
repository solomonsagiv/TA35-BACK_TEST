package service;

import serverObjects.BASE_CLIENT_OBJECT;
import threads.MyThread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServiceHandler extends MyThread implements Runnable {

    final int sleep = 100;
    int sleepCount = 0;
    // Variables
    private List< MyBaseService > servies = new ArrayList<>( );
    private ExecutorService executor;

    // Constructor
    public MyServiceHandler( BASE_CLIENT_OBJECT client ) {
        super( client );
    }

    @Override
    public void run() {
        init( );
    }

    private void init() {

        executor = Executors.newCachedThreadPool( );

        while ( isRun( ) ) {
            try {

                Thread.sleep( sleep );

                executServices( );

                initSleepCount( );

            } catch ( InterruptedException e ) {
                e.printStackTrace( );
                executor.shutdownNow( );
                break;
            } catch ( Exception e ) {
                e.printStackTrace( );
            }
        }
    }

    public void addService( MyBaseService newService ) {
        if ( !isExist( newService ) ) {
            servies.add( newService );
        }
    }

    public boolean isExist( MyBaseService newService ) {
        boolean exist = false;

        for ( MyBaseService service : servies ) {
            if ( service.equals( newService ) ) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public void removeService( MyBaseService service ) {
        servies.remove( service );
    }

    private void initSleepCount() {
        if ( sleepCount == 300000 ) {
            sleepCount = 0;
        }
        sleepCount += sleep;
    }

    private void executServices() {
        for ( MyBaseService service : servies ) {
            service.execute( sleepCount );
        }
    }

    @Override
    public void initRunnable() {
        setRunnable( this );
    }
}
