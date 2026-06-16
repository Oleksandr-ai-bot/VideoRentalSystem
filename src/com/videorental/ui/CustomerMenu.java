package com.videorental.ui;

import com.videorental.model.media.MediaItem;
import com.videorental.model.rental.Rental;
import com.videorental.model.user.Customer;
import com.videorental.observer.EventManager;
import com.videorental.repository.CopyRepository;
import com.videorental.repository.MediaRepository;
import com.videorental.service.RentalService;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu extends Menu {
    private final Customer customer;
    private final MediaRepository mediaRepo;
    private final CopyRepository copyRepo;
    private final RentalService rentalService;

    public CustomerMenu(Scanner scanner, Customer customer,
                        MediaRepository mediaRepo, CopyRepository copyRepo,
                        RentalService rentalService, EventManager eventManager) {
        super(scanner);
        this.customer = customer;
        this.mediaRepo = mediaRepo;
        this.copyRepo = copyRepo;
        this.rentalService = rentalService;
    }

    @Override
    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\nМеню клієнта: " + customer.getName());
            System.out.println("1. Орендувати медіа");
            System.out.println("2. Повернути оренду");
            System.out.println("3. Мої оренди");
            System.out.println("4. Список бажаного");
            System.out.println("5. Залишити відгук");
            System.out.println("6. Переглянути всі медіа");
            System.out.println("7. Назад");

            switch (readInt("Вибір: ")) {
                case 1 -> rentMedia();
                case 2 -> returnRental();
                case 3 -> listMyRentals();
                case 4 -> manageWishlist();
                case 5 -> leaveReview();
                case 6 -> listAllMedia();
                case 7 -> running = false;
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private void rentMedia() {
        List<MediaItem> all = mediaRepo.findAll();
        if (all.isEmpty()) {
            System.out.println("Медіа відсутні.");
            return;
        }

        System.out.println("\nДоступні медіа:");
        List<MediaItem> available = all.stream()
                .filter(m -> copyRepo.findAvailableCopy(m.getId()).isPresent())
                .toList();

        if (available.isEmpty()) {
            System.out.println("Немає медіа з доступними копіями.");
            return;
        }

        for (int i = 0; i < available.size(); i++) {
            System.out.println((i + 1) + ". " + available.get(i));
        }

        int idx = readInt("Вибір: ") - 1;
        if (idx < 0 || idx >= available.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        int days = readInt("Кількість днів оренди: ");
        if (days <= 0) {
            System.out.println("Кількість днів має бути більше 0.");
            return;
        }

        try {
            Rental rental = rentalService.rentMedia(customer.getId(), available.get(idx).getId(), days);
            System.out.println("Оренду створено: " + rental.getId());
            System.out.println("Повернути до: " + rental.getDueDate());
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private void returnRental() {
        List<Rental> active = customer.getRentals().stream()
                .filter(r -> !r.isReturned())
                .toList();

        if (active.isEmpty()) {
            System.out.println("Немає активних оренд.");
            return;
        }

        System.out.println("\nАктивні оренди:");
        for (int i = 0; i < active.size(); i++) {
            System.out.println((i + 1) + ". " + active.get(i));
        }

        int idx = readInt("Вибір: ") - 1;
        if (idx < 0 || idx >= active.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        try {
            Rental rental = active.get(idx);
            rentalService.returnMedia(rental.getId());
            System.out.println("Медіа повернено.");
            if (rental.isOverdue()) {
                System.out.printf("Нараховано штраф: %.2f грн%n", rental.calculateFine());
            }
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private void listMyRentals() {
        List<Rental> rentals = customer.getRentals();
        if (rentals.isEmpty()) {
            System.out.println("У вас немає оренд.");
            return;
        }
        System.out.println("\nМої оренди:");
        rentals.forEach(r -> System.out.println(r));
    }

    private void manageWishlist() {
        boolean running = true;
        while (running) {
            System.out.println("\nСписок бажаного");
            System.out.println("1. Переглянути список");
            System.out.println("2. Додати медіа");
            System.out.println("3. Видалити медіа");
            System.out.println("4. Назад");

            switch (readInt("Вибір: ")) {
                case 1 -> showWishlist();
                case 2 -> addToWishlist();
                case 3 -> removeFromWishlist();
                case 4 -> running = false;
                default -> System.out.println("Невірний вибір.");
            }
        }
    }

    private void showWishlist() {
        if (customer.getWishlist().isEmpty()) {
            System.out.println("Список бажаного порожній.");
            return;
        }
        System.out.println("\nСписок бажаного:");
        customer.getWishlist().forEach(mediaId -> {
            MediaItem item = mediaRepo.findById(mediaId);
            if (item != null) {
                System.out.println("  " + item);
            }
        });
    }

    private void addToWishlist() {
        List<MediaItem> all = mediaRepo.findAll();
        if (all.isEmpty()) {
            System.out.println("Медіа відсутні.");
            return;
        }
        System.out.println("\nОберіть медіа для додавання:");
        for (int i = 0; i < all.size(); i++) {
            System.out.println((i + 1) + ". " + all.get(i));
        }
        int idx = readInt("Вибір: ") - 1;
        if (idx < 0 || idx >= all.size()) {
            System.out.println("Невірний вибір.");
            return;
        }
        try {
            rentalService.addToWishlist(customer.getId(), all.get(idx).getId());
            System.out.println("Додано до списку бажаного.");
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private void removeFromWishlist() {
        if (customer.getWishlist().isEmpty()) {
            System.out.println("Список бажаного порожній.");
            return;
        }
        List<String> wishIds = List.copyOf(customer.getWishlist());
        System.out.println("\nОберіть медіа для видалення:");
        for (int i = 0; i < wishIds.size(); i++) {
            MediaItem item = mediaRepo.findById(wishIds.get(i));
            String title = item != null ? item.getTitle() : wishIds.get(i);
            System.out.println((i + 1) + ". " + title);
        }
        int idx = readInt("Вибір: ") - 1;
        if (idx < 0 || idx >= wishIds.size()) {
            System.out.println("Невірний вибір.");
            return;
        }
        rentalService.removeFromWishlist(customer.getId(), wishIds.get(idx));
        System.out.println("Видалено зі списку бажаного.");
    }

    private void leaveReview() {
        List<MediaItem> all = mediaRepo.findAll();
        if (all.isEmpty()) {
            System.out.println("Медіа відсутні.");
            return;
        }
        System.out.println("\nОберіть медіа для відгуку:");
        for (int i = 0; i < all.size(); i++) {
            System.out.println((i + 1) + ". " + all.get(i));
        }
        int idx = readInt("Вибір: ") - 1;
        if (idx < 0 || idx >= all.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        int rating = readInt("Рейтинг (1-5): ");
        if (rating < 1 || rating > 5) {
            System.out.println("Рейтинг має бути від 1 до 5.");
            return;
        }
        String comment = readLine("Коментар: ");

        try {
            rentalService.addReview(customer.getId(), all.get(idx).getId(), rating, comment);
            System.out.println("Відгук додано.");
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private void listAllMedia() {
        List<MediaItem> all = mediaRepo.findAll();
        if (all.isEmpty()) {
            System.out.println("Медіа відсутні.");
            return;
        }
        System.out.println("\nУсі медіа:");
        all.forEach(m -> {
            System.out.println(m.getInfo());
            long avail = copyRepo.findByMediaId(m.getId()).stream()
                    .filter(c -> c.getStatus() == com.videorental.model.copy.CopyStatus.AVAILABLE)
                    .count();
            System.out.println("   Доступних копій: " + avail);
        });
    }
}
