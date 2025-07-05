package pl.punktozaur.loyalty;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.punktozaur.common.LoyaltyAccountId;
import pl.punktozaur.common.LoyaltyPoints;

import java.util.List;

@Service
@AllArgsConstructor
public class LoyaltyAccountService implements LoyaltyFacade {

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
    }

    @Override
    @Transactional
    public void subtractPoints(LoyaltyAccountId id, LoyaltyPoints pointsToSubtract) {
        var account = loyaltyAccountRepository.findById(id).orElseThrow(()
                -> new LoyaltyAccountNotFoundException(id));

        account.subtractPoints(pointsToSubtract);
    }

    public List<LoyaltyAccountDto> getAccountByCustomerId(CustomerId customerId) {
        return loyaltyAccountRepository.findByCustomerId(customerId)
                .stream()
                .map(acc -> new LoyaltyAccountDto(
                        acc.getId().id(), acc.getCustomerId().id(), acc.getPoints().points()))
                .toList();
    }
}
