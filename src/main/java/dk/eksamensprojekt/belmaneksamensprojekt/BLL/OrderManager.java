package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.OrdersDAO;

import java.nio.file.*;
import java.util.List;

public class OrderManager {
    private final OrdersDAO ordersDAO;

    public OrderManager() {
        this.ordersDAO = new OrdersDAO();
    }

    public List<Order> getAllOrders() throws Exception {
        return ordersDAO.getAll();
    }

    public void updateOrder(Order order) throws Exception {
        ordersDAO.update(order);
    }

    public static void openCamera(Order order) throws Exception {
        Runtime.getRuntime().exec("cmd.exe /C start microsoft.windows.camera:");

        // Kan være tilfælde hvor brugerens kamera ikke gemmer billeder i denne mappe
        String userHome = System.getProperty("user.home");
        Path cameraRoll = Paths.get(userHome, "Pictures", "Camera Roll");

        WatchService watchService = FileSystems.getDefault().newWatchService();
        cameraRoll.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE && event.context().toString().endsWith(".jpg")) {
                    System.out.println("New photo: " + event.context());
                    order.getImageList().add(new Image(1, "test/image.png", false));
                    Runtime.getRuntime().exec("taskkill /IM WindowsCamera.exe /F");
                    key.reset();
                }
            }
        }
    }
}
