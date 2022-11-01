package com.wixossdeckbuilder.backendservice.startup;

import com.wixossdeckbuilder.backendservice.startup.loaders.AdminLoader;
import com.wixossdeckbuilder.backendservice.startup.loaders.CardLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class AppStartUpListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    CardLoader cardLoader;

    @Autowired
    AdminLoader adminLoader;

    public static final Logger logger = LoggerFactory.getLogger(AppStartUpListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.debug("Begin Database loading after startup");
        /** Loaders already ran in this block START
         *
         * cardLoader.uploadCardsToDB();
         * adminLoader.createAndUploadAdmin();
         *
         *
         *
         *
        Loaders already ran in this block END **/

        /** Loaders that need to run in this block START **/


        /** Loaders that need to run in this block END **/

        logger.debug("End Database loading after startup");
    }
}
