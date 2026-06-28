package lk.ac.sliit.drivingschool.drivingschoolsystem.service;

import lk.ac.sliit.drivingschool.drivingschoolsystem.dto.VehicleDto;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Vehicle;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.VehicleRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.repository.InstructorRepository;
import lk.ac.sliit.drivingschool.drivingschoolsystem.entity.Instructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final InstructorRepository instructorRepository;

    public VehicleService(VehicleRepository vehicleRepository, InstructorRepository instructorRepository) {
        this.vehicleRepository = vehicleRepository;
        this.instructorRepository = instructorRepository;
    }

    public void addVehicle(VehicleDto dto) {
        if (dto.getPlateNumber() != null && vehicleRepository.existsByPlateNumber(dto.getPlateNumber().trim())) {
            throw new IllegalArgumentException("A vehicle with plate number " + dto.getPlateNumber() + " is already registered.");
        }
        Vehicle vehicle = new Vehicle();
        mapDtoToEntity(dto, vehicle);
        vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Transactional
    public void deleteVehicle(Long id) {
        List<Instructor> instructors = instructorRepository.findByAssignedVehicle_Id(id);
        for (Instructor inst : instructors) {
            inst.setAssignedVehicle(null);
            instructorRepository.save(inst);
        }
        vehicleRepository.deleteById(id);
    }

    public VehicleDto getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id: " + id));

        VehicleDto dto = new VehicleDto();
        dto.setId(vehicle.getId());
        dto.setModel(vehicle.getModel());
        dto.setPlateNumber(vehicle.getPlateNumber());
        dto.setType(vehicle.getType());
        return dto;
    }

    public void updateVehicle(VehicleDto dto) {
        Vehicle vehicle = vehicleRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle Id: " + dto.getId()));

        if (dto.getPlateNumber() != null && !dto.getPlateNumber().trim().equalsIgnoreCase(vehicle.getPlateNumber())
                && vehicleRepository.existsByPlateNumber(dto.getPlateNumber().trim())) {
            throw new IllegalArgumentException("A vehicle with plate number " + dto.getPlateNumber() + " is already registered.");
        }

        mapDtoToEntity(dto, vehicle);
        vehicleRepository.save(vehicle);
    }

    private void mapDtoToEntity(VehicleDto dto, Vehicle vehicle) {
        vehicle.setModel(dto.getModel());
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setType(dto.getType());
    }
}