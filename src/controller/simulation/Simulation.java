package controller.simulation;

import controller.ExceptionObservable;
import javafx.application.Platform;
import model.Plane;
import model.Territory;
import model.exception.SimulatorException;
import view.Sound;

import java.util.Observable;
import java.util.Observer;

/**
 * Simulation which can be started, stopped or paused.
 * Contains the java code which is written in the textbox as runnable code.
 */
public class Simulation extends Thread implements Observer {

    private final Territory territory;
    private final Plane plane;
    private final SimulationManager simulationManager;
    private final ExceptionObservable exceptionObservable;
    private final Object syncObject = new Object();
    private boolean paused = true;
    private boolean stopped = false;

    public Simulation(Territory territory, SimulationManager simulationManager, ExceptionObservable exceptionObservable) {
        this.territory = territory;
        this.plane = territory.getPlane();
        this.simulationManager = simulationManager;
        this.exceptionObservable = exceptionObservable;
    }

    public void startSimulation() {
        synchronized (this.syncObject) {
            this.paused = false;
            this.start();
        }
    }

    public void resumeSimulation() {
        synchronized (this.syncObject) {
            this.paused = false;
            this.syncObject.notify();
        }
    }

    public void pauseSimulation() {
        synchronized (this.syncObject) {
            this.paused = true;
        }
    }

    public void stopSimulation() {
        synchronized (this.syncObject) {
            this.paused = false;
            this.stopped = true;
            this.interrupt();
            this.syncObject.notify();
        }
    }

    @Override
    public void run() {
        this.territory.addObserver(this);
        try {
            this.plane.main();
        } catch (SimulatorStoppedException ignored) {
        } catch (SimulatorException exception) {
            Sound.death();
            this.exceptionObservable.notifyAboutException(exception);
        } catch (Throwable e) {
            Sound.death();
            e.printStackTrace();
        } finally {
            this.territory.deleteObserver(this);
            this.simulationManager.simulationEnded();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            // It is possible that the thread which updates the territory is the fx application thread
            // which should not be put to sleep
            if (!Platform.isFxApplicationThread()) {
                Thread.sleep(simulationManager.getSpeed());
            }
        } catch (InterruptedException e) {
            this.interrupt();
        }
        synchronized (this.syncObject) {
            while (this.paused) {
                try {
                    this.syncObject.wait();
                } catch (InterruptedException ignored) {
                }
            }

            if (this.stopped) {
                throw new SimulatorStoppedException();
            }
        }
    }
}
