package com.service.scim.triggers;

import com.service.scim.models.User;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserTrailListener {

    private final static Logger logger = LoggerFactory.getLogger(UserTrailListener.class);

    //cunado se genere la creacion de un usuario en la base de datos interna se lanzara este trigger como respuesta
    @PostPersist
    private void afterCreation(User user)
    {
        System.out.println("Ususario creado: id:"+user.id);
        logger.info("Ususario Creado: id:"+user.id);
    }

    //cunado se genere la actualizacion de un usuario en la base de datos interna se lanzara este trigger como respuesta
    @PostUpdate
    private void afterUpdate(User user)
    {
        if (!user.getStatusChanged()) {
            System.out.println("sin cambios de estado en el usuario: id:" + user.id);
            logger.info("sin cambios de estado en el usuario: id:" + user.id);
            return;
        }

            if (user.active) {
                System.out.println("Ususario Activado: id:" + user.id);
                logger.info("Ususario Activado: id:" + user.id);
            }
            if (!user.active) {
                System.out.println("Ususario desactivado: id:" + user.id);
                logger.info("Ususario desactivado: id:" + user.id);
            }
    }
}
