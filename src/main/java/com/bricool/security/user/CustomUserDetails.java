
package com.bricool.security.user;

import org.springframework.security.core.userdetails.UserDetails;
public interface CustomUserDetails extends UserDetails {

    Integer getId();
}