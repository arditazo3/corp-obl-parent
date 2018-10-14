package com.tx.co.user.resource;

import com.tx.co.common.api.provider.ObjectResult;
import com.tx.co.user.api.model.UserResult;
import com.tx.co.user.domain.User;
import com.tx.co.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.tx.co.common.constants.ApiConstants.*;

@Component
@Path(USER)
public class UserResource extends ObjectResult {

    @Context
    private UriInfo uriInfo;

    private IUserService userService;

    @Autowired
    public UserResource(IUserService userService) {
        this.userService = userService;
    }

    /**
     * @return get all the user with admin role
     */
    @GET
    @Path(USER_LIST)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {

        Iterable<User> userIterable = userService.findAllUsers();
        List<UserResult> queryUserList =
                StreamSupport.stream(userIterable.spliterator(), false)
                        .map(this::toUserResult)
                        .collect(Collectors.toList());

        return Response.ok(queryUserList).build();
    }

    /**
     * @return get all the user except roles...
     */
    @GET
    @Path(USER_LIST_EXCEPT)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getUsersExceptRole(@QueryParam("role") String role) {

        List<User> userList = userService.findAllUsersExceptRole(role);
        List<UserResult> queryDetailsList =
                StreamSupport.stream(userList.spliterator(), false)
                        .map(this::toUserResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
    }

    /**
     * @param username
     * @return get the user with admin role
     */
    @GET
    @Path(USER_BY_USERNAME)
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getUser(@PathParam("username") String username) {

        User user = userService.findByUsername(username);
        if (user == null) {
            throw new NotFoundException();
        }

        UserResult result = toUserResult(user);
        return Response.ok(result).build();
    }

    /**
     * Get details from the current user.
     *
     * @return
     */
    @GET
    @Path(ME)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            UserResult result = new UserResult();
            result.setusername(authentication.getName());
            result.setAuthorities(new HashSet<>());
            return Response.ok(result).build();
        }

        User user = userService.findByUsername(authentication.getName());
        UserResult result = toUserResult(user);
        return Response.ok(result).build();
    }

    
}
