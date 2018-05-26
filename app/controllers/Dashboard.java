package controllers;

import models.Member;
import models.Assessment;
import models.Trainer;
import play.Logger;
import play.mvc.Controller;

import java.util.List;

public class Dashboard extends Controller {
  public static void index() {
    Logger.info("Rendering Dashboard");
    Member member = Accounts.getLoggedInMember();
    List<Assessment> assessments = member.assessments;
    double bmi = Utility.calculateBMI(member);
    String bmiCategory = Utility.determineBMICategory(bmi);
    boolean isIdealBodyWeight = Utility.isIdealBodyWeight(member);
    render("dashboard.html", member, assessments, bmi, bmiCategory, isIdealBodyWeight);
  }

  public static void addAssessment(double weight, double chest, double thigh, double upperArm, double waist, double hips) {
    Member member = Accounts.getLoggedInMember();
    Assessment assessment = new Assessment(weight, chest, thigh, upperArm, waist, hips);
    member.assessments.add(assessment);
    member.save();
    Logger.info("Adding Assessment " + assessment);
    redirect("/dashboard");
  }

  public static void deleteAssessment(long assessmentId) {
    Member member = Accounts.getLoggedInMember();
    Assessment assessment = Assessment.findById(assessmentId);
    member.assessments.remove(assessment);
    member.save();
    assessment.delete();
    Logger.info("Deleting " + assessment);
    redirect("/dashboard");
  }

  public static void listAssessments(long memberId) {
    Logger.info("Rendering member assessments");
    Trainer trainer = Accounts.getLoggedInTrainer();
    Member member = Member.findById(memberId);
    double bmi = Utility.calculateBMI(member);
    String bmiCategory = Utility.determineBMICategory(bmi);
    boolean isIdealBodyWeight = Utility.isIdealBodyWeight(member);
    render("listassessments.html", member, bmi, bmiCategory, isIdealBodyWeight);
  }

  public static void addComment(long memberId, long assessmentId, String comment) {
    Trainer trainer = Accounts.getLoggedInTrainer();
    Assessment assessment = Assessment.findById(assessmentId);
    assessment.setComment(comment);
    assessment.save();
    Logger.info("Adding comment to assessment " + assessment);
    redirect("/listassessments/" + memberId);
  }

  public static void deleteMember(long memberId) {
    Trainer trainer = Accounts.getLoggedInTrainer();
    Member member = Member.findById(memberId);
    member.delete();
    Logger.info("Deleting " + member);
    redirect("/trainer/members");
  }
}
