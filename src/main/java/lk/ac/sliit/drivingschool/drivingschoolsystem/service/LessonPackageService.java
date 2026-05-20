package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.LessonPackageDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.LessonPackage;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonPackageService implements ApplicationRunner {

    private final PackageRepository packageRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedDefaultPackagesIfMissing();
    }

    @Transactional(readOnly = true)
    public List<LessonPackageDto> getAllPackages() {
        return packageRepository.findAll().stream()
                .sorted(Comparator.comparingInt(LessonPackage::getSortOrder)
                        .thenComparing(LessonPackage::getPackageName))
                .map(LessonPackageDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<String> getPackageNames() {
        return getAllPackages().stream()
                .map(LessonPackageDto::getPackageName)
                .toList();
    }

    @Transactional
    public void updatePrices(List<Long> packageIds, List<Double> prices) {
        if (packageIds == null || prices == null || packageIds.size() != prices.size()) {
            throw new IllegalArgumentException("Package IDs and prices must match.");
        }
        for (int i = 0; i < packageIds.size(); i++) {
            Long id = packageIds.get(i);
            Double price = prices.get(i);
            if (price == null || price < 0) {
                throw new IllegalArgumentException("Invalid price for package " + id);
            }
            LessonPackage pkg = packageRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Package not found: " + id));
            pkg.setBasePrice(price);
        }
    }

    @Transactional
    public void seedDefaultPackagesIfMissing() {
        for (PackageCatalog.Def def : PackageCatalog.ALL) {
            if (!packageRepository.existsByPackageName(def.name())) {
                packageRepository.save(toEntity(def));
            }
        }
        enrichKnownPackageMetadata();
    }

    private void enrichKnownPackageMetadata() {
        for (LessonPackage pkg : packageRepository.findAll()) {
            PackageCatalog.Def def = PackageCatalog.find(pkg.getPackageName());
            if (def == null) {
                continue;
            }
            boolean changed = false;
            if (isBlank(pkg.getImageUrl()) || pkg.getImageUrl().endsWith(".svg")) {
                pkg.setImageUrl(def.imageUrl());
                changed = true;
            }
            if (isBlank(pkg.getDescription())) {
                pkg.setDescription(def.description());
                changed = true;
            }
            if (pkg.getSortOrder() == 0 && def.sortOrder() > 0) {
                pkg.setSortOrder(def.sortOrder());
                changed = true;
            }
            if (changed) {
                packageRepository.save(pkg);
            }
        }
    }

    private static LessonPackage toEntity(PackageCatalog.Def def) {
        LessonPackage pkg = new LessonPackage();
        pkg.setPackageName(def.name());
        pkg.setDescription(def.description());
        pkg.setImageUrl(def.imageUrl());
        pkg.setNumberOfLessons(def.lessons());
        pkg.setBasePrice(def.price());
        pkg.setSortOrder(def.sortOrder());
        return pkg;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}