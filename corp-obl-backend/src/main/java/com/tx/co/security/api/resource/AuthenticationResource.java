package com.tx.co.security.api.resource;

import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.model.AuthenticationTokenUser;
import com.tx.co.security.api.model.UserCredentials;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.exception.GeneralException;
import com.tx.co.security.service.AuthenticationTokenService;
import com.tx.co.user.domain.User;
import com.tx.co.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.tx.co.common.constants.AppConstants.AUTH;
import static com.tx.co.common.constants.AppConstants.REFRESH;

/**
 * JAX-RS resource class for authentication. The username is exchanged for an authentication token.
 * The user need to be exist on the table and to have at least one role
 *
 * @author Ardit Azo
 */
@Component
@Path(AUTH)
public class AuthenticationResource implements IUserManagementDetails {

    private final IUserService userService;

    private final AuthenticationTokenService authenticationTokenService;

    @Autowired
    public AuthenticationResource(IUserService userService, AuthenticationTokenService authenticationTokenService) {
        this.userService = userService;
        this.authenticationTokenService = authenticationTokenService;
    }

    /**
     * Validate user credentials and issue a token for the user.
     *
     * @param credentials
     * @return the Response of authentification
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(UserCredentials credentials) {

        String username = credentials.getUsername();
        User userLogIn = null;

        if(!StringUtils.isEmpty(username)) {

            userLogIn = userService.findByUsername(username);

            if(userLogIn == null) {
                throw new GeneralException("The user doesn't exist");
            } else if(userLogIn.getAuthorities().isEmpty()) {
                throw new GeneralException("The user should contain at least one role");
            }

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            throw new GeneralException("Empty field");
        }

        String token = authenticationTokenService.issueToken(userLogIn);
        AuthenticationTokenUser authenticationTokenUser = new AuthenticationTokenUser();
        authenticationTokenUser.setToken(token);
        authenticationTokenUser.setUser(userLogIn);
        authenticationTokenUser.setLangList(userService.getLang());
        authenticationTokenUser.setLangNotAvailableList(userService.getLangNotAvailable());

        return Response.ok(authenticationTokenUser).build();
    }

    /**
     * Refresh the authentication token for the current user.
     *
     * @return the Response of refreshing Token
     */
    @POST
    @Path(REFRESH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response refresh() {

        AuthenticationTokenUserDetails tokenUserDetails = getTokenUserDetails();

        String token = authenticationTokenService.refreshToken(tokenUserDetails);
        AuthenticationTokenUser authenticationTokenUser = new AuthenticationTokenUser();
        authenticationTokenUser.setToken(token);
        User user = userService.findByUsername(tokenUserDetails.getUser().getUsername());
        authenticationTokenUser.setUser(user);

        return Response.ok(authenticationTokenUser).build();
    }

    @Override
    public AuthenticationTokenUserDetails getTokenUserDetails() {
        return (AuthenticationTokenUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();
    }
}
