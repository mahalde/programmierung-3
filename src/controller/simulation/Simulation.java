package controller.simulation;

import javafx.application.Platform;
import model.Plane;
import model.Territory;
import model.exception.SimulatorException;
import view.Sound;

import java.util.Observable;
import java.util.Observer;

public class Simulation extends Thread implements Observer {

    private final Territory territory;
    private final Plane plane;
    private final SimulationManager simulationManager;
    private final Object syncObject = new Object();
    private boolean paused = true;
    private boolean stopped = false;

    public Simulation(Territory territory, SimulationManager simulationManager) {
        this.territory = territory;
        this.plane = territory.getPlane();
        this.simulationManager = simulationManager;
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
