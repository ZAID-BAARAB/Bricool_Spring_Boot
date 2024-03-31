package com.bricool.security.user;

import com.bricool.security.chat.Conversation;
import com.bricool.security.token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
// the origin is << public class User implements UserDetails >>
public class User implements CustomUserDetails {

  @Getter
  @Id
  @GeneratedValue
  private Integer id;
  private String firstname;
  private String lastname;
  private String email;
  @JsonIgnore
  private String password;
  private String imagePath;
  @Lob
  @Column(name = "about", length = 1000) // Adjust length based on your requirements
  private String about;
  private String job;
  private String country;
  private String city;
  private String address;

  @Enumerated(EnumType.STRING)
  private Role role;


  @OneToMany(mappedBy = "user")
  @JsonIgnore   //this is added to not return user's token
  private List<Token> tokens;

  @Getter
  @Transient // Ignore this field for database persistence
  private String imageBase64;

  //this part is added for chatting functionality
  @OneToMany(mappedBy = "client")
  @JsonIgnore
  private List<Conversation> clientConversations;

  @OneToMany(mappedBy = "serviceProvider")
  @JsonIgnore
  private List<Conversation> serviceProviderConversations;


  public  void setImageBase64(String imageBase64) {
    this.imageBase64 = imageBase64;
  }

//  @OneToMany(mappedBy = "serviceProvider")
//  @JsonIgnoreProperties("serviceProvider")
//  public List<Service> services;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  public Integer getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", email='" + email + '\'' +
            ", role=" + role +
            // Exclude tokens from toString to avoid cyclic reference
            // ", tokens=" + tokens +
            '}';
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
