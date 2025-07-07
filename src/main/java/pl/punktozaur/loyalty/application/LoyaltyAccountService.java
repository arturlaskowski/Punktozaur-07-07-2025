package pl.punktozaur.loyalty.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.punktozaur.common.domain.CustomerId;
import pl.punktozaur.common.domain.LoyaltyAccountId;
import pl.punktozaur.common.domain.LoyaltyPoints;
import pl.punktozaur.loyalty.application.dto.CreateLoyaltyAccountDto;
import pl.punktozaur.loyalty.application.dto.LoyaltyAccountDto;
import pl.punktozaur.loyalty.application.exception.LoyaltyAccountNotFoundException;
import pl.punktozaur.loyalty.domain.LoyaltyAccount;

import java.util.List;

@Service
@AllArgsConstructor
public class LoyaltyAccountService {

    private final LoyaltyAccountRepository loyaltyAccountRepository;

    public LoyaltyAccountDto getAccount(LoyaltyAccountId id) {
        return loyaltyAccountRepository.findById(id)
                .map(acc -> new LoyaltyAccountDto(acc.getId().id(), acc.getCustomerId().id(), acc.getPoints().points()))
                .orElseThrow(() -> new LoyaltyAccountNotFoundException(id));
    }

    public LoyaltyAccountId addAccount(CreateLoyaltyAccountDto createDto) {
        var account = new LoyaltyAccount(new CustomerId(createDto.customerId()));
        return loyaltyAccountRepository.save(account).getId();
    }

    @Transactional
    public void addPoints(LoyaltyAccountId id, LoyaltyPoints pointsToAdd) {
        var account = loyaltyAccountRepository.findById(id).orElseThrow(()
                -> new LoyaltyAccountNotFoundException(id));

        account.addPoints((pointsToAdd));
        loyaltyAccountRepository.save(account);
    }

    @Transactional
    public void subtractPoints(LoyaltyAccountId id, LoyaltyPoints pointsToSubtract) {
        var account = loyaltyAccountRepository.findById(id).orElseThrow(()
                -> new LoyaltyAccountNotFoundException(id));

        account.subtractPoints(pointsToSubtract);
        loyaltyAccountRepository.save(account);
    }

    public List<LoyaltyAccountDto> getAccountByCustomerId(CustomerId customerId) {
        return loyaltyAccountRepository.findByCustomerId(customerId)
                .stream()
                .map(acc -> new LoyaltyAccountDto(
                        acc.getId().id(), acc.getCustomerId().id(), acc.getPoints().points()))
                .toList();
    }
}
