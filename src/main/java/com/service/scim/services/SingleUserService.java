package com.service.scim.services;

import com.service.scim.models.mapper.AbstractEntityMapper;
import com.service.scim.repositories.IUserRepository;
import com.service.scim.models.User;
import com.service.scim.utils.MapConverter;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class SingleUserService implements ISingleUserService {

    private final IUserRepository userRepository;
    private final AbstractEntityMapper<User> userEntityMapper;

    public SingleUserService(IUserRepository userRepository, AbstractEntityMapper<User> userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public Map singeUserGet(String id) {
            //System.out.println(headers);
            Optional<User> user = userRepository.findById(id);
        return user.map(User::toScimResource).orElse(Map.of());

    }

    @Override
    public Map singleUserPut(Map<String, Object> payload, String id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return Map.of();
        }

        user.get().update(payload, userEntityMapper);
        user.get().updatedAt = LocalDateTime.now(ZoneOffset.UTC);
        userRepository.save(user.get());
        return user.get().toScimResource();
    }

    @Override
    public Map singleUserPatch(Map<String, Object> payload, String id) {

        //Find user for update
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return Map.of();
        }

        Map<String, Object> userMapOperations = MapConverter.getMapOperations(payload);
        user.get().update(userMapOperations, userEntityMapper);
        user.get().updatedAt = LocalDateTime.now(ZoneOffset.UTC);
        userRepository.save(user.get());

        return user.get().toScimResource();
    }

    @Override
    public void singleUserDelete(String id) {
        userRepository.deleteById(id);
    }
}
