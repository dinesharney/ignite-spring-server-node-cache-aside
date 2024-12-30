package com.example.ignite.server.service;

import com.example.ignite.server.entity.User;
import com.example.ignite.server.repository.UserRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.ScanQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.cache.Cache;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.example.ignite.server.constants.AppConstants.USER_CACHE;

/**
 * Service to handle business logic for User entities.
 */
@Service
public class UserCacheService {


    @Autowired
    Ignite ignite;

    @Autowired
    UserRepository userRepository;

    private Cache<Long, User> getUserCache() {
        return ignite.cache(USER_CACHE);
    }

    public User getUser(Long id) {
        // Check cache first
        Cache<Long, User> cache = getUserCache();
        User user = cache.get(id);

        if (user == null) {
            // Fetch from the database if not in cache
            user = userRepository.findById(id).orElse(null);

            // Put the data into the cache if found in DB
            if (user == null) {
                cache.put(id, user);
            }
        }
        return user;
    }

    public void invalidateUserCache(Long id) {
        getUserCache().remove(id);
    }

    public void updateUserCache(Long id, User user) {
        getUserCache().put(id, user);
    }

    public User saveUser(User user) {
        getUserCache().put(user.getId(), user);
        return user;
    }

}