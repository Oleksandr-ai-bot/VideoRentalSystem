package com.videorental.ui;

import com.videorental.exception.CopyNotAvailableException;
import com.videorental.exception.MediaNotFoundException;
import com.videorental.factory.GameFactory;
import com.videorental.factory.MediaFactory;
import com.videorental.factory.MovieFactory;
import com.videorental.factory.SeriesFactory;
import com.videorental.model.copy.Carrier;
import com.videorental.model.copy.DigitalCopy;
import com.videorental.model.copy.MediaCopy;
import com.videorental.model.copy.PhysicalCopy;
import com.videorental.model.media.MediaItem;
import com.videorental.model.rental.Rental;
import com.videorental.model.user.User;
import com.videorental.observer.EventManager;
import com.videorental.repository.CopyRepository;
import com.videorental.repository.MediaRepository;
import com.videorental.repository.RentalRepository;
import com.videorental.repository.UserRepository;
import com.videorental.service.RentalService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class AdminMenu extends Menu {
    private final MediaRepository mediaRepo;
    private final CopyRepository copyRepo;
    private final UserRepository userRepo;
    private final RentalRepository rentalRepo;
    private final RentalService rentalService;

    public AdminMenu(Scanner scanner, MediaRepository mediaRepo, CopyRepository copyRepo,
                     UserRepository userRepo, RentalRepository rentalRepo,
                     RentalService rentalService, EventManager eventManager) {
        super(scanner);
        this.mediaRepo = mediaRepo;
        this.copyRepo = copyRepo;
        this.userRepo = userRepo;
        this.rentalRepo = rentalRepo;
        this.rentalService = rentalService;
    }

    @Override
    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\nМеню адміністратора");
            System.out.println("1. Додати медіа");
            System.out.println("2. Додати копію медіа");
            System.out.println("3. Зареєструвати клієнта");
            System.out.println("4. Переглянути всі медіа");
            System.out.println("5. Переглянути всі оренди");
            System.out.println("6. Переглянути клієнтів");
            System.out.println("7. Назад");

            switch (readInt("Вибір: ")) {
                case 1 -> addMedia();
                case 2 -> addCopy();
                case 3 -> registerCustomer();
                case 4 -> listMedia();
                case 5 -> listRentals();
                case 6 -> listCustomers();
                case 7 -> running = false;
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private void addMedia() {
        System.out.println("\nТип медіа:");
        System.out.println("1. Фільм");
        System.out.println("2. Серіал");
        System.out.println("3. Гра");
        int type = readInt("Вибір: ");
        if (type < 1 || type > 3) {
            System.out.println("Невірний вибір.");
            return;
        }

        String id = "M-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String title = readLine("Назва: ");
        String genre = readLine("Жанр: ");
        int year = readInt("Рік випуску: ");
        double rating = readDouble("Рейтинг (0-10): ");

        MediaFactory factory;
        String param1;
        String param2;

        switch (type) {
            case 1 -> {
                factory = new MovieFactory();
                param1 = readLine("Режисер: ");
                param2 = String.valueOf(readInt("Тривалість (хв): "));
            }
            case 2 -> {
                factory = new SeriesFactory();
                param1 = String.valueOf(readInt("Кількість сезонів: "));
                param2 = String.valueOf(readInt("Серій на сезон: "));
            }
            default -> {
                factory = new GameFactory();
                param1 = readLine("Платформа: ");
                param2 = readLine("Видавець: ");
            }
        }

        MediaItem media = factory.create(id, title, genre, year, rating, param1, param2);
        rentalService.addMedia(media);
        System.out.println("Додано: " + media.getInfo());
    }

    private void addCopy() {
        List<MediaItem> allMedia = mediaRepo.findAll();
        if (allMedia.isEmpty()) {
            System.out.println("Немає медіа в системі.");
            return;
        }

        System.out.println("\nОберіть медіа:");
        for (int i = 0; i < allMedia.size(); i++) {
            System.out.println((i + 1) + ". " + allMedia.get(i));
        }
        int idx = readInt("Вибір: ") - 1;
        if (idx < 0 || idx >= allMedia.size()) {
            System.out.println("Невірний вибір.");
            return;
        }
        MediaItem media = allMedia.get(idx);

        System.out.println("\nТип копії:");
        System.out.println("1. Фізична");
        System.out.println("2. Цифрова");
        int type = readInt("Вибір: ");

        String copyId = "C-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        MediaCopy copy;

        if (type == 1) {
            System.out.println("Носій: 1. DVD  2. BLU_RAY  3. CARTRIDGE");
            int carrierIdx = readInt("Вибір: ");
            Carrier carrier = switch (carrierIdx) {
                case 2 -> Carrier.BLU_RAY;
                case 3 -> Carrier.CARTRIDGE;
                default -> Carrier.DVD;
            };
            copy = new PhysicalCopy(copyId, media, carrier);
        } else {
            int sizeMb = readInt("Розмір файлу (MB): ");
            copy = new DigitalCopy(copyId, media, sizeMb);
        }

        rentalService.addCopy(copy);
        System.out.println("Додано копію: " + copy);
    }

    private void registerCustomer() {
        String id = "U-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String name = readLine("Ім'я клієнта: ");
        String email = readLine("Email: ");
        rentalService.registerCustomer(id, name, email);
        System.out.println("Клієнта зареєстровано: " + name + " [" + id + "]");
    }

    private void listMedia() {
        List<MediaItem> all = mediaRepo.findAll();
        if (all.isEmpty()) {
            System.out.println("Медіа відсутні.");
            return;
        }
        System.out.println("\nУсі медіа:");
        all.forEach(m -> {
            System.out.println(m);
            List<com.videorental.model.copy.MediaCopy> copies = copyRepo.findByMediaId(m.getId());
            long available = copies.stream()
                    .filter(c -> c.getStatus() == com.videorental.model.copy.CopyStatus.AVAILABLE)
                    .count();
            System.out.printf("   Копій: %d, доступних: %d%n", copies.size(), available);
        });
    }

    private void listRentals() {
        List<Rental> all = rentalRepo.findAll();
        if (all.isEmpty()) {
            System.out.println("Оренди відсутні.");
            return;
        }
        System.out.println("\nУсі оренди:");
        all.forEach(r -> System.out.println(r));
    }

    private void listCustomers() {
        List<User> all = userRepo.findAll().stream()
                .filter(u -> u.getRole().name().equals("CUSTOMER"))
                .toList();
        if (all.isEmpty()) {
            System.out.println("Клієнти відсутні.");
            return;
        }
        System.out.println("\nКлієнти:");
        all.forEach(u -> System.out.println(u));
    }
}
