package pl.springboot.academy.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vehicles")
public class VehicleApi {

    private List<Vehicle> vehicleList = new ArrayList<>();

    public VehicleApi() {
        this.vehicleList.add(new Vehicle(1L, "Audi","RS6", "white"));
        this.vehicleList.add(new Vehicle(2L, "Citroen", "C-Crosser ", "red"));
        this.vehicleList.add(new Vehicle(3L, "BMW", "i3", "white" ));

    }

    @GetMapping()
    public ResponseEntity<List<Vehicle>> getVehicles(){
        return new ResponseEntity<>(vehicleList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id){
        Optional<Vehicle> firstVehicle = vehicleList.stream().filter(vehicle -> vehicle.getId()==id).findFirst();
        if (firstVehicle.isPresent()){
            return new ResponseEntity(firstVehicle, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/color/{color}")
    public ResponseEntity<List<Vehicle>> getVehicleByColor(@PathVariable String color){
        List<Vehicle> vehicleListByColor = vehicleList
                .stream()
                .filter(vehicle -> vehicle.getColor().equals(color))
                .collect(Collectors.toList());
        if (!vehicleListByColor.isEmpty()){
            return new ResponseEntity<>(vehicleListByColor, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle newVehicle){
        boolean add = vehicleList.add(newVehicle);
        if(add)
            {
                return new ResponseEntity<>( newVehicle ,HttpStatus.CREATED);
            }
        else
            {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    @PutMapping
    public ResponseEntity<Vehicle> updateVehicle(@RequestBody Vehicle newVehicle){
        Optional first = vehicleList.stream().filter(vehicle -> vehicle.getId()== newVehicle.getId()).findFirst();
        if(first.isPresent())
        {
            vehicleList.remove(first.get());
            vehicleList.add(newVehicle);
            return new ResponseEntity(newVehicle, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehiclePole(@PathVariable Long id
                                                     ,@RequestBody Map<String, String> params
                                                    ){

        Optional<Vehicle> firstVehicle = vehicleList.stream().filter(vehicle -> vehicle.getId()==id).findFirst();

        if(firstVehicle.isPresent()) {
            String newMark = (params.containsKey("mark")) ? params.get("mark") : firstVehicle.get().getMark();
            String newModel = (params.containsKey("model")) ? params.get("model") : firstVehicle.get().getModel();
            String newColor = (params.containsKey("color")) ? params.get("color") : firstVehicle.get().getColor();

            Vehicle newVehicle = new Vehicle(id, newMark, newModel, newColor);

            vehicleList.remove(firstVehicle.get());
            vehicleList.add(newVehicle);

            return new ResponseEntity<Vehicle>(newVehicle, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Vehicle> removeVehicle(@PathVariable Long id) {
        Optional<Vehicle> first = vehicleList.stream().filter(vehicle -> vehicle.getId()==id).findFirst();
        if (first.isPresent())
        {
            vehicleList.remove(first.get());
            return new ResponseEntity(first, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}

