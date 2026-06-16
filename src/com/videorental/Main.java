package com.videorental;

import com.videorental.factory.GameFactory;
import com.videorental.factory.MovieFactory;
import com.videorental.factory.SeriesFactory;
import com.videorental.memento.MementoCaretaker;
import com.videorental.model.copy.Carrier;
import com.videorental.model.copy.DigitalCopy;
import com.videorental.model.copy.PhysicalCopy;
import com.videorental.model.media.MediaItem;
import com.videorental.model.user.Admin;
import com.videorental.observer.EventManager;
import com.videorental.observer.StatisticsCollector;
import com.videorental.repository.CopyRepository;
import com.videorental.repository.MediaRepository;
import com.videorental.repository.RentalRepository;
import com.videorental.repository.UserRepository;
import com.videorental.service.RentalService;
import com.videorental.ui.MainMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MediaRepository mediaRepo = MediaRepository.getInstance();
        CopyRepository copyRepo = CopyRepository.getInstance();
        UserRepository userRepo = UserRepository.getInstance();
        RentalRepository rentalRepo = RentalRepository.getInstance();

        EventManager eventManager = new EventManager();
        StatisticsCollector statisticsCollector = new StatisticsCollector();
        eventManager.subscribe(statisticsCollector);

        MementoCaretaker caretaker = new MementoCaretaker();

        RentalService rentalService = new RentalService(
                mediaRepo, copyRepo, userRepo, rentalRepo, eventManager);

        loadSampleData(mediaRepo, copyRepo, userRepo, rentalService);

        Scanner scanner = new Scanner(System.in);
        new MainMenu(scanner, mediaRepo, copyRepo, userRepo, rentalRepo,
                rentalService, eventManager, statisticsCollector, caretaker)
                .display();
        scanner.close();
    }

    private static void loadSampleData(MediaRepository mediaRepo, CopyRepository copyRepo,
                                       UserRepository userRepo, RentalService rentalService) {
        userRepo.add(new Admin("ADM-001", "Адміністратор", "admin@videorental.ua"));

        MediaItem interstellar = new MovieFactory().create(
                "M-001", "Interstellar", "Sci-Fi", 2014, 8.7, "Christopher Nolan", "169");
        MediaItem breakingBad = new SeriesFactory().create(
                "M-002", "Breaking Bad", "Crime", 2008, 9.5, "5", "13");
        MediaItem gtaV = new GameFactory().create(
                "M-003", "GTA V", "Action", 2013, 9.0, "PC/PS5/Xbox", "Rockstar Games");
        MediaItem inception = new MovieFactory().create(
                "M-004", "Inception", "Sci-Fi", 2010, 8.8, "Christopher Nolan", "148");

        rentalService.addMedia(interstellar);
        rentalService.addMedia(breakingBad);
        rentalService.addMedia(gtaV);
        rentalService.addMedia(inception);

        rentalService.addCopy(new PhysicalCopy("C-001", interstellar, Carrier.BLU_RAY));
        rentalService.addCopy(new PhysicalCopy("C-002", interstellar, Carrier.DVD));
        rentalService.addCopy(new DigitalCopy("C-003", breakingBad, 45000));
        rentalService.addCopy(new PhysicalCopy("C-004", gtaV, Carrier.BLU_RAY));
        rentalService.addCopy(new DigitalCopy("C-005", gtaV, 98000));
        rentalService.addCopy(new PhysicalCopy("C-006", inception, Carrier.DVD));

        rentalService.registerCustomer("U-001", "Олег Коваль", "oleg@mail.ua");
        rentalService.registerCustomer("U-002", "Марія Ткач", "maria@mail.ua");
    }
}
