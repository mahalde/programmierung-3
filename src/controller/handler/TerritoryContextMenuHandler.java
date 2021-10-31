package controller.handler;

import controller.ExceptionObservable;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import model.Invisible;
import model.Plane;
import model.Territory;
import model.exception.SimulatorException;
import view.Sound;
import view.TerritoryPane;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Event handler for the opening of the context menu when clicking on the plane in the territory
 */
public class TerritoryContextMenuHandler implements EventHandler<ContextMenuEvent> {

    private final Territory territory;
    private final TerritoryPane territoryPane;
    private final ExceptionObservable exceptionObservable;
    private final ContextMenu contextMenu;

    public TerritoryContextMenuHandler(Territory territory, TerritoryPane territoryPane, ExceptionObservable exceptionObservable) {
        this.territory = territory;
        this.territoryPane = territoryPane;
        this.exceptionObservable = exceptionObservable;
        this.contextMenu = new ContextMenu();
    }

    @Override
    public void handle(ContextMenuEvent contextMenuEvent) {
        Territory.Tile tile = territoryPane.getTile(contextMenuEvent.getX(), contextMenuEvent.getY());

        if (territory.getPlaneX() != tile.getX() || territory.getPlaneY() != tile.getY()) return;

        List<MenuItem> menuItems = getMethodsFromClass(territory.getPlane());
        contextMenu.getItems().clear();
        contextMenu.getItems().addAll(menuItems);
        contextMenu.show(territoryPane, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());

        contextMenu.setOnAction(selectedEvent -> {
            MenuItem menuItem = (MenuItem) selectedEvent.getTarget();
            Method method = (Method) menuItem.getUserData();

            try {
                // Set accessibility to true to access the method
                method.setAccessible(true);
                method.invoke(territory.getPlane());
                method.setAccessible(false);
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e.getCause() instanceof SimulatorException) {
                    Sound.death();
                    this.exceptionObservable.notifyAboutException(e.getCause());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Gets the methods from the given inheritant of the plane class and transform them into menu items.
     * Filters out the main method, methods with @Invisible, static and private methods.
     *
     * @param plane the given inheritant
     * @return a list of menu items which display the methods
     */
    private List<MenuItem> getMethodsFromClass(Plane plane) {
        List<Method> methods = new ArrayList<>();
        Class<?> clazz = plane.getClass();
        while (!clazz.equals(Plane.class)) {
            methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));

            clazz = clazz.getSuperclass();
        }

        methods.addAll(Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .collect(Collectors.toList()));

        return methods.stream()
                .filter(this::isNotMainMethod)
                .filter(this::isNotInvisible)
                .filter(this::isNotStatic)
                .filter(this::isNotPrivate)
                .map(this::mapToMenuItem)
                .collect(Collectors.toList());
    }

    /**
     * Returns whether the method is not named "main".
     *
     * @param method the given method
     * @return true if the method is not named "main"
     */
    private boolean isNotMainMethod(Method method) {
        return !method.getName().equals("main");
    }

    /**
     * Returns whether the method is not annotated with @Invisible.
     *
     * @param method the given method
     * @return true if the method is not annotated with @Invisible
     */
    private boolean isNotInvisible(Method method) {
        return !method.isAnnotationPresent(Invisible.class);
    }

    /**
     * Returns whether the method is not static.
     *
     * @param method the given method
     * @return true if the method is not static
     */
    private boolean isNotStatic(Method method) {
        return !Modifier.isStatic(method.getModifiers());
    }

    /**
     * Returns whether the method is not private.
     *
     * @param method the given method
     * @return true if the method is not private
     */
    private boolean isNotPrivate(Method method) {
        return !Modifier.isPrivate(method.getModifiers());
    }

    /**
     * Maps a method into a menu item, providing the return type, name and parameter types as text
     * and the actual method as custom user data.
     *
     * @param method the given method
     * @return a menu item
     */
    private MenuItem mapToMenuItem(Method method) {
        MenuItem menuItem = new MenuItem();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(method.getReturnType().getName());
        stringBuilder.append(" ");
        stringBuilder.append(method.getName());
        stringBuilder.append("(");

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            stringBuilder.append(parameterTypes[i].getName());
            if (i != parameterTypes.length - 1) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append(")");

        menuItem.setText(stringBuilder.toString());
        menuItem.setDisable(parameterTypes.length > 0);
        menuItem.setUserData(method);

        return menuItem;
    }
}
