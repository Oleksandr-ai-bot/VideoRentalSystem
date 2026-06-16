package com.videorental.ui;

import com.videorental.memento.MementoCaretaker;
import com.videorental.memento.SystemMemento;
import com.videorental.model.user.Customer;
import com.videorental.model.user.User;
import com.videorental.observer.EventManager;
import com.videorental.observer.EventType;
import com.videorental.observer.RentalEvent;
import com.videorental.observer.StatisticsCollector;
import com.videorental.repository.CopyRepository;
import com.videorental.repository.MediaRepository;
import com.videorental.repository.RentalRepository;
import com.videorental.repository.UserRepository;
import com.videorental.service.RentalService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MainMenu extends Menu {
    private final MediaRepository mediaRepo;
    private final CopyRepository copyRepo;
    private final UserRepository userRepo;
    private final RentalRepository rentalRepo;
    private final RentalService rentalService;
    private final EventManager eventManager;
    private final StatisticsCollector statisticsCollector;
    private final MementoCaretaker caretaker;

    public MainMenu(Scanner scanner, MediaRepository mediaRepo, CopyRepository copyRepo,
                    UserRepository userRepo, RentalRepository rentalRepo,
                    RentalService rentalService, EventManager eventManager,
                    StatisticsCollector statisticsCollector, MementoCaretaker caretaker) {
        super(scanner);
        this.mediaRepo = mediaRepo;
        this.copyRepo = copyRepo;
        this.userRepo = userRepo;
        this.rentalRepo = rentalRepo;
        this.rentalService = rentalService;
        this.eventManager = eventManager;
        this.statisticsCollector = statisticsCollector;
        this.caretaker = caretaker;
    }

    @Override
    public void display() {
        boolean running = true;
        while (running) {
            System.out.println("\nСистема прокату відеоматеріалів");
            System.out.println("1. Увійти як адміністратор");
            System.out.println("2. Увійти як клієнт");
            System.out.println("3. Статистика");
            System.out.println("4. Зберегти стан системи");
            System.out.println("5. Відновити стан системи");
            System.out.println("6. Вихід");

            switch (readInt("Вибір: ")) {
                case 1 -> openAdminMenu();
                case 2 -> selectCustomer();
                case 3 -> statisticsCollector.printStatistics(mediaRepo);
                case 4 -> saveState();
                case 5 -> restoreState();
                case 6 -> running = false;
                default -> System.out.println("Невірний вибір.");
            }
        }
        System.out.println("До побачення!");
    }

    private void openAdminMenu() {
        new AdminMenu(scanner, mediaRepo, copyRepo, userRepo, rentalRepo, rentalService, eventManager)
                .display();
    }

    private void selectCustomer() {
        List<User> customers = userRepo.findAll().stream()
                .filter(u -> u.getRole().name().equals("CUSTOMER"))
                .toList();

        if (customers.isEmpty()) {
            System.out.println("Клієнтів немає. Зареєструйте клієнта через меню адміністратора.");
            return;
        }

        System.out.println("\nОберіть клієнта:");
        for (int i = 0; i < customers.size(); i++) {
            System.out.println((i + 1) + ". " + customers.get(i));
        }

        int idx = readInt("Вибір: ") - 1;
        if (idx < 0 || idx >= customers.size()) {
            System.out.println("Невірний вибір.");
            return;
        }

        new CustomerMenu(scanner, (Customer) customers.get(idx),
                mediaRepo, copyRepo, rentalService, eventManager).display();
    }

    private void saveState() {
        SystemMemento memento = new SystemMemento(
                mediaRepo.findAll(),
                copyRepo.findAll(),
                userRepo.findAll(),
                rentalRepo.findAll()
        );
        caretaker.save(memento);
        eventManager.notify(new RentalEvent(EventType.STATE_SAVED, "Стан системи збережено", null));
        System.out.println("Стан збережено: " + memento);
    }

    private void restoreState() {
        Optional<SystemMemento> result = caretaker.restore();
        if (result.isEmpty()) {
            System.out.println("Немає збережених станів.");
            return;
        }
        SystemMemento state = result.get();
        mediaRepo.clear();
        copyRepo.clear();
        userRepo.clear();
        rentalRepo.clear();
        state.getMedia().forEach(mediaRepo::add);
        state.getCopies().forEach(copyRepo::add);
        state.getUsers().forEach(userRepo::add);
        state.getRentals().forEach(rentalRepo::add);
        eventManager.notify(new RentalEvent(EventType.STATE_RESTORED,
                "Стан системи відновлено (збережено: " + state.getSavedAt() + ")", null));
        System.out.println("Стан відновлено: " + state);
    }
}
