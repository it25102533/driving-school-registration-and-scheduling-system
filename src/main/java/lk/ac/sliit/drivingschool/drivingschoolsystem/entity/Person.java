package lk.ac.sliit.drivingschool.drivingschoolsystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass

public abstract class Person {
    @Column(nullable = false)
    private String name;
    private String phone;

}