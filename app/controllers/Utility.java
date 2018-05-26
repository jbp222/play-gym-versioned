package controllers;

import models.Assessment;
import models.Member;

public class Utility {

  public static double calculateBMI(Member member) {
    // last index is element size - 1 to get last index
    if(member.assessments.size() > 0){
      int lastIndex = member.assessments.size() - 1;
      Assessment lastAssessment = member.assessments.get(lastIndex);
      return lastAssessment.getWeight() / (member.getHeight() * member.getHeight());
    } else {
      // uses starting weight if no prior assessment
      return member.getStartingWeight() / (member.getHeight() * member.getHeight());
    }
  }

  public static String determineBMICategory(double bmiValue) {
    String category = "";
    if (bmiValue < 16) {
      category = "SEVERELY UNDERWEIGHT";
    } else if (bmiValue >= 16 && bmiValue < 18.5) {
      category = "UNDERWEIGHT";
    } else if (bmiValue >= 18.5 && bmiValue < 25) {
      category = "NORMAL";
    } else if (bmiValue >= 25 && bmiValue < 30) {
      category = "OVERWEIGHT";
    } else if (bmiValue >= 30 && bmiValue < 35) {
      category = "MODERATELY OBESE";
    } else if (bmiValue >= 35) {
      category = "SEVERELY OBESE";
    }
    return category;
  }

  public static boolean isIdealBodyWeight(Member member) {
    float maleIdealWeight = 50;
    double femaleIdealWeight = 45.5;
    // converting meters to inches
    double heightInInches = member.getHeight() * 39.3701;
    double inchesOver5feet = 0;
    if (heightInInches > 60) {
      inchesOver5feet = heightInInches - 60;
    }
    double idealWeight;
    if(member.getGender().equals("MALE")) {
      idealWeight = maleIdealWeight + (2.3 * inchesOver5feet);
    } else {
      idealWeight = femaleIdealWeight + (2.3 * inchesOver5feet);
    }
    boolean isIdealWeight;
    if(member.assessments.size() > 0) {
      int lastIndex = member.assessments.size() - 1;
      // if weight between these 2 numbers
      if (member.assessments.get(lastIndex).getWeight() >= (idealWeight - 0.2) &&
          member.assessments.get(lastIndex).getWeight() <= (idealWeight + 0.2)) {
        isIdealWeight = true;
      } else {
        isIdealWeight = false;
      }
    } else {
      if (member.getStartingWeight() >= (idealWeight - 0.2) && member.getStartingWeight() <= (idealWeight + 0.2)) {
        isIdealWeight = true;
      } else {
        isIdealWeight = false;
      }
    }
    return isIdealWeight;
  }
}