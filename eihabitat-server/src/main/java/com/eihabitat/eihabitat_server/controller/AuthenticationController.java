package com.eihabitat.eihabitat_server.controller;

import com.eihabitat.eihabitat_server.dto.request.ApiResponse;
import com.eihabitat.eihabitat_server.dto.request.AuthenticationReq;
import com.eihabitat.eihabitat_server.dto.request.IntrospectReq;
import com.eihabitat.eihabitat_server.dto.request.LogoutRequest;
import com.eihabitat.eihabitat_server.dto.request.RefreshRequest;
import com.eihabitat.eihabitat_server.dto.response.AuthenticationResponse;
import com.eihabitat.eihabitat_server.dto.response.IntrospectResponse;
import com.eihabitat.eihabitat_server.dto.response.UserResponse;
import com.eihabitat.eihabitat_server.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @GetMapping("/loginWithGoogle")
    ApiResponse<AuthenticationResponse> getUserProfileByGoogle(OAuth2AuthenticationToken token) {
        ApiResponse<AuthenticationResponse> resp = new ApiResponse<>();
        resp.setCode(1000);
        resp.setData(authenticationService.loginWithGoogle(token));
        return resp;
    }

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationReq request){
       var result =  authenticationService.authenticate(request);
       return ApiResponse.<AuthenticationResponse>builder()
               .code(1000)
               .data(result)
               .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectReq request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody RefreshRequest request) throws JOSEException, ParseException{
       var result =  authenticationService.refreshToken(request);
       return ApiResponse.<AuthenticationResponse>builder()
               .data(result)
               .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .code(1000)
                .message("Successfully logged out")
                .build();
    }
}
