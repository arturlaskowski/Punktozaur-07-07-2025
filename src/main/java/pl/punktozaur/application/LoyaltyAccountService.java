package pl.punktozaur.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.punktozaur.application.dto.CreateLoyaltyAccountDto;
import pl.punktozaur.application.dto.LoyaltyAccountDto;
import pl.punktozaur.application.exception.LoyaltyAccountNotFoundException;
import pl.punktozaur.application.repository.LoyaltyAccountRepository;
import pl.punktozaur.domain.CustomerId;
import pl.punktozaur.domain.LoyaltyAccount;
import pl.punktozaur.domain.LoyaltyAccountId;
import pl.punktozaur.domain.LoyaltyPoints;

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
