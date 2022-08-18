package com.bootstraptsk4.task4.Controller;

import com.bootstraptsk4.task4.Config.JwtUtil;
import com.bootstraptsk4.task4.Model.JwtRequest;
import com.bootstraptsk4.task4.Model.JwtResponse;
import com.bootstraptsk4.task4.Service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
class UserController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@RequestMapping({ "/products" })
	public String firstPage() {
		return "Hello! Here are your products :)";
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String accessToken = jwtTokenUtil.generateToken(userDetails);
		final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtResponse responce) throws Exception {
		try{
			final String username = jwtTokenUtil.extractUsername(responce.getAccessToken());
			final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if(jwtTokenUtil.validateToken(responce.getRefreshToken(),userDetails)){
				final String accessToken = jwtTokenUtil.generateToken(userDetails);
				final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

				return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
			}
			else{
				return ResponseEntity.ok("Wrong refresh token");
			}
		}
		catch (BadCredentialsException e) {
			throw new Exception("Invalid Refresh Token", e);
		}
	}

}

