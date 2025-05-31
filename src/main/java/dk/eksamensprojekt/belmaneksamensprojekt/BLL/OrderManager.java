package dk.eksamensprojekt.belmaneksamensprojekt.BLL;

// Projekt imports
import dk.eksamensprojekt.belmaneksamensprojekt.BE.*;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.Approved;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Enums.ImagePosition;
import dk.eksamensprojekt.belmaneksamensprojekt.BE.Image;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.OrderDaoFacade;
import dk.eksamensprojekt.belmaneksamensprojekt.DAL.Repository;

// Java imports
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Manager til at håndtere ordre
 */
public class OrderManager {
    // Statisk konstant -> til billede mappen. (Desuden ligger en magen til i Image-klassen)
    private static final String IMAGES_PATH = System.getProperty("user.dir") + File.separator + "Images" + File.separator;

    private final Repository<Order, String> ordersDAO;

    public OrderManager() {
        this.ordersDAO = new OrderDaoFacade();
    }

    public List<Order> getAllOrders() throws Exception {
        return ordersDAO.getAll();
    }

    public void updateOrder(Order order) throws Exception {
        ordersDAO.update(order);
    }

    /**
     * Åbner brugerens kamera, så brugeren kan tilføje et billede til en bestemt ordre.
     * @param order Den ordre som brugeren skal tage et billede til.
     * @param user Den bruger som tager billede (bruges til at placere ansvar).
     * @param imagePosition Den vinkel som billedet repræsenterer på produktet.
     * @return Det billede som blev taget.
     * @throws Exception Hvis der sker en fejl, enten fordi den ikke kan åbne kameraet, finde din filmappe eller en anden årsag, så kommer der en Exception.
     */
    public Image openCamera(Order order, User user, ImagePosition imagePosition) throws Exception {
        try {
            // åben kameraet
            Runtime.getRuntime().exec("cmd.exe /C start microsoft.windows.camera:");

            // find brugerens billede mappe, hvor billederne komme hen, når et billede tages med kameraet
            String userHome = System.getProperty("user.home");
            Path cameraRoll = Paths.get(userHome, "Pictures", "Camera Roll");

            // prøver at finde hvilken type mappe der skal bruges -> hvis onedrive er tilsluttet.
            if (!Files.exists(cameraRoll)) {
                cameraRoll = Paths.get(userHome, "OneDrive", "Pictures", "Filmrulle");
            }

            if (!Files.exists(cameraRoll)) {
                throw new Exception("Camera Roll not found");
            }

            // tilføjer en listener for at lytte efter et kommende billede.
            WatchService watchService = FileSystems.getDefault().newWatchService();
            cameraRoll.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            // venter på at der sker noget
            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    // hvis den finder et billede
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE &&
                            event.context().toString().endsWith(".jpg")) {

                        // klargør billede
                        Path imagePath = cameraRoll.resolve((Path) event.context());
                        Path fileName = imagePath.getFileName();
                        Path newLocation = Paths.get(IMAGES_PATH + fileName);
                        // Kopierer billedet over på fælles mappen/floder
                        boolean fileReady = waitForFileReady(imagePath, 500, 10); // 500ms interval, 10 retries
                        if (fileReady) {
                            Files.copy(imagePath, newLocation);
                        } else {
                            throw new Exception("Error accessing picture taken.");
                        }

                        // Generere nyt billede objekt
                        Image image = new Image(
                                -1,
                                fileName.toString(),
                                Approved.NOT_REVIEWED,
                                user,
                                order.getId(),
                                imagePosition
                        );

                        // Luk kamera appen og sende billede til gui-laget
                        Runtime.getRuntime().exec("taskkill /IM WindowsCamera.exe /F");
                        return image;
                    }
                }
                key.reset();
            }
        } catch (Exception e) {
            throw new Exception("Error with camera", e.getCause());
        }

        return null;
    }

    /**
     * Venter på filen på den givne sti til at blive stabil i størrelse, for at sikre at billedet/filen ikke skaber problemer.
     * @param path stien til filen
     * @param waitMillis mængden af tid som der skal ventes mellem hvert interval i millisekunder.
     * @param retries hvor mange forsøg der bliver brugt på at tjekke filens status.
     * @return hvis filen er stabil -> true, hvis filen er ustabil -> false.
     */
    private static boolean waitForFileReady(Path path, long waitMillis, int retries) {
        long previousSize = -1;
        try {
            for (int i = 0; i < retries; i++) {
                if (Files.exists(path)) {
                    long size = Files.size(path);
                    if (size == previousSize) {
                        return true;
                    }
                    previousSize = size;
                }
                Thread.sleep(waitMillis);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gem ordre status.
     */
    public void submitOrder(Order currentOrder) throws Exception {
        currentOrder.setDocumented(true);
        ordersDAO.update(currentOrder);
    }

    public Order getById(String id) throws Exception {
        return ordersDAO.getById(id);
    }
}
