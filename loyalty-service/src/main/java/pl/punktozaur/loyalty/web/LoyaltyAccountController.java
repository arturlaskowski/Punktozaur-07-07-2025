package pl.punktozaur.loyalty.web;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.punktozaur.domain.CustomerId;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;
import pl.punktozaur.loyalty.application.LoyaltyAccountService;
import pl.punktozaur.loyalty.application.dto.CreateLoyaltyAccountDto;
import pl.punktozaur.loyalty.application.dto.LoyaltyAccountDto;
import pl.punktozaur.loyalty.application.dto.ModifyPointsDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/loyalty-accounts")
@RequiredArgsConstructor
public class LoyaltyAccountController {

    private final LoyaltyAccountService loyaltyAccountService;

    @PostMapping
    public ResponseEntity<Void> createLoyaltyAccount(@Valid @RequestBody CreateLoyaltyAccountDto request) {
        var loyaltyAccountId = loyaltyAccountService.addAccount(request);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(loyaltyAccountId.id())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/add-points")
    public ResponseEntity<Void> addPoints(@PathVariable UUID id, @Valid @RequestBody ModifyPointsDto dto) {
        var loyaltyAccountId = new LoyaltyAccountId(id);
        var loyaltyPoints = new LoyaltyPoints(dto.points());
        loyaltyAccountService.addPoints(loyaltyAccountId, loyaltyPoints);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/subtract-points")
    public ResponseEntity<Void> subtractPoints(@PathVariable UUID id, @Valid @RequestBody ModifyPointsDto dto) {
        var loyaltyAccountId = new LoyaltyAccountId(id);
        var loyaltyPoints = new LoyaltyPoints(dto.points());
        loyaltyAccountService.subtractPoints(loyaltyAccountId, loyaltyPoints);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyAccountDto> getLoyaltyAccount(@PathVariable UUID id) {
        var loyaltyAccountId = new LoyaltyAccountId(id);
        var dto = loyaltyAccountService.getAccount(loyaltyAccountId);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<LoyaltyAccountDto>> getLoyaltyAccountsByCustomerId(@RequestParam UUID customerId) {
        var accounts = loyaltyAccountService.getAccountByCustomerId(new CustomerId(customerId));
        var responses = accounts.stream().map(dto -> new LoyaltyAccountDto(dto.id(), dto.customerId(), dto.points())).toList();

        return ResponseEntity.ok(responses);
    }
}
