package lk.ac.sliit.drivingschool.drivingschoolsystem.dto;

import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.LessonPackage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonPackageDto {
    private Long id;
    private String packageName;
    private String description;
    private String imageUrl;
    private int numberOfLessons;
    private double basePrice;
    private int sortOrder;

    public static LessonPackageDto from(LessonPackage pkg) {
        return new LessonPackageDto(
                pkg.getId(),
                pkg.getPackageName(),
                pkg.getDescription(),
                pkg.getImageUrl(),
                pkg.getNumberOfLessons(),
                pkg.getBasePrice(),
                pkg.getSortOrder()
        );
    }
}