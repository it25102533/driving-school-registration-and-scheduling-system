package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Single source of truth for default lesson packages (seed data + image paths).
 */
public final class PackageCatalog {

    public record Def(
            String name,
            String description,
            String imageUrl,
            int lessons,
            double price,
            int sortOrder
    ) {}

    public static final List<Def> ALL = List.of(
            new Def("Beginner Essentials",
                    "First-time drivers: basics, controls, and quiet-road practice.",
                    "/images/packages/beginner.jpg", 5, 25000, 1),
            new Def("Standard Course",
                    "Balanced programme for learners building confidence toward the test.",
                    "/images/packages/standard.jpg", 10, 45000, 2),
            new Def("Intensive Pass",
                    "Accelerated sessions for students with a test date approaching.",
                    "/images/packages/intensive.jpg", 15, 62000, 3),
            new Def("Parallel Parking Master",
                    "Focused bay parking, alignment, and mirror-reference drills.",
                    "/images/packages/parallel-parking.jpg", 3, 12000, 4),
            new Def("Highway & Expressway",
                    "Merging, lane discipline, and higher-speed awareness.",
                    "/images/packages/highway.jpg", 4, 18000, 5),
            new Def("Night Driving Skills",
                    "Headlight use, visibility, and low-light hazard awareness.",
                    "/images/packages/night-driving.jpg", 3, 15000, 6),
            new Def("Reverse & Hill Start",
                    "Reversing lines, clutch control, and incline starts without rollback.",
                    "/images/packages/reverse-hill.jpg", 3, 13500, 7),
            new Def("Three-Wheeler (B1)",
                    "Tuk / three-wheel handling for B1 licence candidates.",
                    "/images/packages/three-wheeler.jpg", 6, 22000, 8),
            new Def("Motorcycle Training (A)",
                    "Balance, cornering, and road craft for motorcycle licences.",
                    "/images/packages/motorcycle.jpg", 5, 20000, 9),
            new Def("Corporate Fleet",
                    "Bulk training for company drivers with progress reporting.",
                    "/images/packages/corporate.jpg", 20, 95000, 10)
    );

    private static final Map<String, Def> BY_NAME = ALL.stream()
            .collect(Collectors.toMap(Def::name, Function.identity()));

    public static Def find(String packageName) {
        return BY_NAME.get(packageName);
    }

    private PackageCatalog() {}
}