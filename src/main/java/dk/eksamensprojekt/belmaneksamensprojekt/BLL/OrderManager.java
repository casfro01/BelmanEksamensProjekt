package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

import dk.eksamensprojekt.belmaneksamensprojekt.BE.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Order;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.OrderDaoFacade;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.Repository;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.*;
import java.util.List;

public class OrderManager {
    private final Repository<Order, String> ordersDAO;

    public OrderManager() {
        this.ordersDAO = new OrderDaoFacade();
    }

    public List<Order> getAllOrders() throws Exception {
        return ordersDAO.getAll();
    }

    public void updateOrder(Order order) throws Exception {
        if (order == null || order.getImageList().isEmpty())
            throw new Exception("No pictures attached!");
        ordersDAO.update(order);
    }

    public void openCamera(Order order) throws Exception {
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
                    order.getImageList().add(new Image(-1, cameraRoll + "/" + event.context(), Approved.NotReviewed));

                    Runtime.getRuntime().exec("taskkill /IM WindowsCamera.exe /F");
                    key.reset();
                }
            }
        }
    }

    public void addPicFromFolder(Order order) throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File file = fileChooser.showOpenDialog(null);
        order.getImageList().add(new Image(-1, "file:\\" + file.getPath(), Approved.NotReviewed));
    }

    public void submitOrder(Order currentOrder) throws Exception {
        currentOrder.setApproved(Approved.Approved);
        ordersDAO.update(currentOrder);
    }
}
