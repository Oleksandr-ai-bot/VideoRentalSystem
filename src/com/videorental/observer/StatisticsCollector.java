package com.videorental.observer;

import com.videorental.model.media.MediaItem;
import com.videorental.repository.MediaRepository;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatisticsCollector implements EventObserver {
    private final Map<EventType, Integer> eventCounts = new EnumMap<>(EventType.class);
    private final Map<String, Integer> mediaRentCount = new LinkedHashMap<>();
    private final List<RentalEvent> eventLog = new ArrayList<>();
    private double totalFinesCharged = 0;

    @Override
    public void onEvent(RentalEvent event) {
        eventCounts.merge(event.getType(), 1, Integer::sum);
        eventLog.add(event);

        if (event.getType() == EventType.RENTAL_CREATED && event.getSourceId() != null) {
            mediaRentCount.merge(event.getSourceId(), 1, Integer::sum);
        }

        if (event.getType() == EventType.FINE_CHARGED) {
            totalFinesCharged += event.getFineAmount();
        }
    }

    public void printStatistics(MediaRepository mediaRepo) {
        System.out.println("\nСтатистика системи");

        System.out.println("\nКількість подій за типом:");
        if (eventCounts.isEmpty()) {
            System.out.println("  Немає даних.");
        } else {
            eventCounts.forEach((type, count) ->
                    System.out.printf("  %-25s %d%n", type + ":", count));
        }

        System.out.printf("%nЗагальна сума штрафів: %.2f грн%n", totalFinesCharged);

        System.out.println("\nТоп медіа за кількістю оренд:");
        if (mediaRentCount.isEmpty()) {
            System.out.println("  Немає даних.");
        } else {
            final int[] rank = {1};
            mediaRentCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(5)
                    .forEach(e -> {
                        MediaItem item = mediaRepo.findById(e.getKey());
                        String title = item != null ? item.getTitle() : e.getKey();
                        System.out.printf("  %d. %-30s %d оренд%n", rank[0]++, title, e.getValue());
                    });
        }

        System.out.printf("%nУсього подій зафіксовано: %d%n",
                eventLog.size());
    }

    public Map<EventType, Integer> getEventCounts() {
        return eventCounts;
    }

    public Map<String, Integer> getMediaRentCount() {
        return mediaRentCount;
    }

    public double getTotalFinesCharged() {
        return totalFinesCharged;
    }

    public List<RentalEvent> getEventLog() {
        return eventLog;
    }
}
