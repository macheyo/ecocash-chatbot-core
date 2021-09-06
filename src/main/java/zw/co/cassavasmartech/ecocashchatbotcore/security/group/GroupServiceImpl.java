package zw.co.cassavasmartech.ecocashchatbotcore.security.group;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.Status;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UserGroup;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Optional<UserGroup> save(UserGroup userGroup) {
        log.info("Save group:{}", userGroup);
        userGroup.setStatus(Status.ACTIVE);
        this.findByName(userGroup.getName()).ifPresent(company1 -> {
            throw new BusinessException("Group With Name:" + userGroup.getName() + " Already Exist");
        });
        return Optional.ofNullable(groupRepository.save(userGroup));
    }

    @Override
    public Optional<UserGroup> update(UserGroup userGroup) {
        log.info("Update group:{}", userGroup);
        return Optional.ofNullable(groupRepository.save(userGroup));
    }

    @Override
    public Optional<List<UserGroup>> findAll() {
        log.info("Find All Groups");
        return groupRepository.findAllByStatusIn();
    }

    @Override
    public Optional<UserGroup> findByName(String name) {
        log.info("Find Group By Name:{}", name);
        return groupRepository.findByName(name);
    }

    @Override
    public Optional<UserGroup> findById(Long id) {
        return groupRepository.findById(id);
    }
}
