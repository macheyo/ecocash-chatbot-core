package zw.co.cassavasmartech.ecocashchatbotcore.security.partner;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.Status;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.RecordExistException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.RecordNotFound;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Partner;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<Partner> create(Partner partner) {

        if(partnerRepository.findByPartnerId(partner.getPartnerId()).isPresent()){
            throw new RecordExistException("Partner With That Id Exist");
        }
        partner.setPartnerKey(passwordEncoder.encode(partner.getPartnerKey()));
        partner.setStatus(Status.ACTIVE);
        return Optional.of(partnerRepository.save(partner));
    }

    @Override
    public Optional<Partner> update(Partner partner) {
        final Partner old = this.findByPartnerId(partner.getPartnerId()).orElseThrow(() -> new RecordNotFound("Cannot Update a Nonexisting PartnerRecord"));

        old.setPartnerId(partner.getPartnerId());
        old.setEmail(partner.getEmail());
        old.setPartnerName(partner.getPartnerName());
        old.setStatus(partner.getStatus());

        return Optional.ofNullable(partnerRepository.save(old));
    }

    @Override
    public List<Partner> findAll(int page, int size) {
        return partnerRepository.findAll(PageRequest.of(page,size, Sort.by("partnerName"))).getContent();
    }

    @Override
    public Optional<Partner> findByPartnerId(String partnerId) {
        return partnerRepository.findByPartnerId(partnerId);
    }

    @Override
    public void resetPartnerKey(String partnerId, String newPassword) {

        Partner partner = this.findByPartnerId(partnerId).orElseThrow(()->new RecordNotFound("Error Resetting "));
        partner.setPartnerKey(passwordEncoder.encode(newPassword));
        partnerRepository.save(partner);
    }
}
