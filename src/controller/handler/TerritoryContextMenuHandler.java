package controller.handler;

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

public class TerritoryContextMenuHandler implements EventHandler<ContextMenuEvent> {

    private final Territory territory;
    private final TerritoryPane territoryPane;
    private final ContextMenu contextMenu;

    public TerritoryContextMenuHandler(Territory territory, TerritoryPane territoryPane) {
        this.territory = territory;
        this.territoryPane = territoryPane;
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
                method.setAccessible(true);
                method.invoke(territory.getPlane());
                method.setAccessible(false);
            } catch (IllegalAccessException | InvocationTargetException e) {
                if (e.getCause() instanceof SimulatorException) {
                    Sound.death();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

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

    private boolean isNotMainMethod(Method method) {
        return !method.getName().equals("main");
    }

    private boolean isNotInvisible(Method method) {
        return !method.isAnnotationPresent(Invisible.class);
    }

    private boolean isNotStatic(Method method) {
        return !Modifier.isStatic(method.getModifiers());
    }

    private boolean isNotPrivate(Method method) {
        return !Modifier.isPrivate(method.getModifiers());
    }

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
