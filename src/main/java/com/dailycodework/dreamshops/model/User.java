package com.dailycodework.dreamshops.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.hibernate.annotations.NaturalId;
import org.modelmapper.ModelMapper;

import com.dailycodework.dreamshops.dto.UserDto;
import com.dailycodework.dreamshops.exceptions.UpdateException;
import com.dailycodework.dreamshops.request.UpdateUserRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String firstName;
  private String lastName;

  @NaturalId // Natural identifiers are unique within the context
  private String email;
  private String password;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  private Cart cart = new Cart(this);

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Order> orders = new ArrayList<>();

  @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
      CascadeType.DETACH })
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Collection<Role> roles = new HashSet<>();

  public User(String firstName, String lastName, String email, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }

  public UserDto convertToDto() {
    return new ModelMapper().map(this, UserDto.class);
  }

  public void updateUser(UpdateUserRequest request) {
    // loop through all fields in UpdateUserRequest and set the value to the user
    Field[] fields = UpdateUserRequest.class.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object value = field.get(request);
        if (value != null) {
          Field userField = User.class.getDeclaredField(field.getName());
          userField.setAccessible(true);
          userField.set(this, value);
        }
      } catch (IllegalAccessException | NoSuchFieldException e) {
        throw new UpdateException("Failed to update user", e);
      }
    }
    // use ModelMapper to map the fields
    // ModelMapper modelMapper = new ModelMapper();
    // modelMapper.getConfiguration().setPropertyCondition(context ->
    // context.getSource() != null);
    // modelMapper.map(request, this);
  }
}
