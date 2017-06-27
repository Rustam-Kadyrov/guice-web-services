package com.rustam.project.config.module;

import com.google.inject.AbstractModule;
import com.rustam.project.endpoint.AccountEndpoint;
import com.rustam.project.endpoint.TransferEndpoint;
import com.rustam.project.endpoint.UserEndpoint;
import com.rustam.project.service.AccountService;
import com.rustam.project.service.TransferService;
import com.rustam.project.service.UserService;

/**
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
public class ApplicationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class);
        bind(UserEndpoint.class);

        bind(AccountService.class);
        bind(AccountEndpoint.class);

        bind(TransferService.class);
        bind(TransferEndpoint.class);
    }
}
