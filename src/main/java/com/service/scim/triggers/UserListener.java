package com.service.scim.triggers;

import com.service.scim.models.User;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserListener {

    private final static Logger logger = LoggerFactory.getLogger(UserListener.class);

    @PostPersist
    private void afterCreation(User user) {
        //Todo: connect to external service to notify the creation of a new user
        System.out.println("User Created: id:" + user.id);
        logger.info("User Created: id:" + user.id);
    }

    @PostUpdate
    private void afterUpdate(User user) {
        //Todo: connect to external service to notify the update of a user
        if (!user.getStatusChanged()) {
            System.out.println("There are no changes in the user's status. id:" + user.id);
            logger.info("There are no changes in the user's status. id:" + user.id);
            return;
        }

        if (user.active) {
            System.out.println("User Activated: id:" + user.id);
            logger.info("User Activated: id:" + user.id);
        }
        if (!user.active) {
            System.out.println("User Deactivated: id:" + user.id);
            logger.info("User Deactivated: id:" + user.id);
        }
    }
}
