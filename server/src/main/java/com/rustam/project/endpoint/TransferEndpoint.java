package com.rustam.project.endpoint;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.rustam.project.model.entity.Transfer;
import com.rustam.project.model.response.MessageResponse;
import com.rustam.project.service.TransferService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Report on transfers
 * Created by Rustam_Kadyrov on 25.06.2017.
 */
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Path("/transfer")
public class TransferEndpoint {

    private final TransferService transferService;

    @Inject
    public TransferEndpoint(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Get all transfers
     *
     * @return
     */
    @GET
    public MessageResponse<List<Transfer>> getAllTransfers() {
        return new MessageResponse<>(transferService.findAll());
    }

}
