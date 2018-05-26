package controllers;

import models.Assessment;
import models.Member;
import models.Trainer;
import play.Logger;
import play.mvc.Controller;

import java.util.ArrayList;
import java.util.List;

public class Accounts extends Controller
{
  public static void signup()
  {
    render("signup.html");
  }

  public static void login()
  {
    render("login.html");
  }

  public static void register(String firstname, String lastname, String email, String password, String address, String gender,
                              double height, double startingWeight)
  {
    Logger.info("Registering new user " + firstname + " " + lastname);
    Member member = new Member(firstname, lastname, email, password, address, gender, height, startingWeight);
    member.save();
    redirect("/");
  }

  public static void authenticate(String email, String password)
  {
    Logger.info("Attempting to authenticate with " + email + ":" + password);
    Member member = null;
    Trainer trainer = null;
    if(Member.findByEmail(email) != null) {
      member = Member.findByEmail(email);
    } else if (Trainer.findByEmail(email) != null) {
      trainer = Trainer.findByEmail(email);
    }
    if ((member != null) && (member.checkPassword(password) == true)) {
      Logger.info("Authentication successful");
      session.put("logged_in_Memberid", member.id);
      redirect ("/dashboard");
    } else if ((trainer != null) && (trainer.checkPassword(password) == true)) {
      Logger.info("Authentication successful");
      session.put("logged_in_Trainerid", trainer.id);
      redirect ("/trainer/members");
    } else {
      Logger.info("Authentication failed");
      redirect("/login");
    }
  }

  public static void logout()
  {
    session.clear();
    redirect ("/");
  }

  public static Member getLoggedInMember()
  {
    Member member = null;
    if (session.contains("logged_in_Memberid")) {
      String memberId = session.get("logged_in_Memberid");
      member = Member.findById(Long.parseLong(memberId));
    } else {
      login();
    }
    return member;
  }

  public static Trainer getLoggedInTrainer()
  {
    Trainer trainer = null;
    if (session.contains("logged_in_Trainerid")) {
      String trainerId = session.get("logged_in_Trainerid");
      trainer = Trainer.findById(Long.parseLong(trainerId));
    } else {
      login();
    }
    return trainer;
  }

  public static void listMembers()
  {
    Trainer trainer = getLoggedInTrainer();
    List<Member> members = Member.findAll();
    render("listmembers.html", members);
  }

  public static void updateMember()
  {
    Member member = getLoggedInMember();
    render("updatemember.html", member);
  }

  public static void saveUpdate(String firstname, String lastname, String email, String password, String address, String gender,
                                double height, double startingWeight)
  {
    Logger.info("Updating user " + firstname + " " + lastname);
    Member member = getLoggedInMember();
    member.setFirstname(firstname);
    member.setLastname(lastname);
    member.setEmail(email);
    member.setPassword(password);
    member.setAddress(address);
    member.setGender(gender);
    member.setHeight(height);
    member.setStartingWeight(startingWeight);
    member.save();
    redirect("/");
  }
}