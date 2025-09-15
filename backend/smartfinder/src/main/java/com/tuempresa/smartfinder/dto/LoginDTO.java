package com.tuempresa.smartfinder.dto;

public class LoginDTO {
  private String emailOrUsername;
  private String password;

  public String getEmailOrUsername() { return emailOrUsername; }
  public void setEmailOrUsername(String v) { this.emailOrUsername = v; }

  public String getPassword() { return password; }
  public void setPassword(String v) { this.password = v; }
}
