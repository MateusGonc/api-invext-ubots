package api.invext.bots.application.controller;

import api.invext.bots.domain.model.Attendant;
import api.invext.bots.domain.service.AttendantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendant")
public class AttendantController {

    private final AttendantService attendantService;

    public AttendantController(AttendantService attendantService) {
        this.attendantService = attendantService;
    }

    @GetMapping
    @Operation(summary = "Get all attendants", description = "Retrieves all attendants.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Attendant.class)))
    })
    public ResponseEntity<List<Attendant>> getAll() {
        return new ResponseEntity<>(attendantService.getAll(), HttpStatus.OK);
    }


    @GetMapping("/{name}")
    @Operation(summary = "Get attendant by name", description = "Retrieves attendant by name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the attendant",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Attendant.class))),
            @ApiResponse(responseCode = "404", description = "Attendant not found")
    })
    public ResponseEntity<Attendant> getByName(@PathVariable String name) {
        return new ResponseEntity<>(attendantService.getByName(name), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new attendant", description = "Creates a new attendant and returns the created attendant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attendant created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Attendant.class)))
    })
    public ResponseEntity<Attendant> create(@RequestBody Attendant attendant) {
        return new ResponseEntity<>(attendantService.create(attendant), HttpStatus.CREATED);
    }
}
