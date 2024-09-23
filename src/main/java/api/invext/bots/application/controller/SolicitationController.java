package api.invext.bots.application.controller;

import api.invext.bots.domain.model.Solicitation;
import api.invext.bots.domain.service.SolicitationService;
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
@RequestMapping("/solicitation")
public class SolicitationController {

    private final SolicitationService solicitationService;

    public SolicitationController(SolicitationService solicitationService) {
        this.solicitationService = solicitationService;
    }

    @GetMapping
    @Operation(summary = "Get all solicitation waiting", description = "Retrieves all solicitation waiting in the queue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Solicitation.class)))
    })
    public ResponseEntity<List<Solicitation>> getAll() {
        return new ResponseEntity<>(solicitationService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a new solicitation", description = "Creates a new solicitation and returns the created solicitation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solicitation created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Solicitation.class)))
    })
    public ResponseEntity<Solicitation> create(@RequestBody Solicitation solicitation) {
        return new ResponseEntity<>(solicitationService.create(solicitation), HttpStatus.CREATED);
    }
}
