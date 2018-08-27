package com.tx.co.user.resource;

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

import static com.tx.co.common.constants.AppConstants.*;

@Component
@Path(USER)
public class UserResource {

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
    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getUsers() {

        Iterable<User> userIterable = userService.findAllUsers();
        List<UserResult> queryDetailsList =
                StreamSupport.stream(userIterable.spliterator(), false)
                        .map(this::toQueryResult)
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

        UserResult result = toQueryResult(user);
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
        UserResult result = toQueryResult(user);
        return Response.ok(result).build();
    }

    /**
     * Map a {@link User} instance to a {@link UserResult} instance.
     *
     * @param user
     * @return UserResult
     */
    private UserResult toQueryResult(User user) {
        UserResult result = new UserResult();
        result.setusername(user.getUsername());
        result.setFullName(user.getFullName());
        result.setEmail(user.getEmail());
        result.setLang(user.getLang());
        result.setAuthorities(user.getAuthorities());
        result.setEnabled(user.isEnabled());
        return result;
    }
}
