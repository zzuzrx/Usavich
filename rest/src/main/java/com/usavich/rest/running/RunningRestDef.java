package com.usavich.rest.running;

import com.usavich.entity.running.*;
import com.usavich.rest.common.RestDef;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: LeonLu
 * Date: 6/24/13
 * Time: 4:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/running")
@Consumes({"*/xml", MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface RunningRestDef extends RestDef {

    @GET
    @Path("/history/{" + PARAM_USER_ID + "}")
    List<RunningHistory> getRunningHistories(
            @PathParam(PARAM_USER_ID) String userId,
            @QueryParam(PARAM_MISSION_ID) String missionId);

    @GET
    @Path("/ongoing/{" + PARAM_USER_ID + "}")
    List<OnGoingRunning> getOnGoingRunning(
            @PathParam(PARAM_USER_ID) String userId);

    @POST
    @Path("/history/{" + PARAM_USER_ID + "}")
    void createRunningHistory( @PathParam(PARAM_USER_ID) String userId,
                            RunningHistory runningHistory);

    @POST
    @Path("/ongoing/{" + PARAM_USER_ID + "}")
    void createOnGoingRunning( @PathParam(PARAM_USER_ID) String userId,
                               OnGoingRunning onGoingRunning);

}