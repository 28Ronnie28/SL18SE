package models.all;

import java.io.Serializable;

public class AssignmentDetails implements Serializable {

    private int assignmentID;
    private int classID;
    private String assignmentType;
    private String assignmentName;
    private int resultMax;
    private int resultAchieved;
    //private String handInDate;

    public AssignmentDetails(int assignmentID, int classID, String assignmentType, String assignmentName, int resultMax, int resultAchieved/*, String handInDate*/) {
        this.assignmentID = assignmentID;
        this.classID = classID;
        this.assignmentType = assignmentType;
        this.assignmentName = assignmentName;
        this.resultMax = resultMax;
        this.resultAchieved = resultAchieved;
        //this.handInDate = handInDate;
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public int getClassID() {
        return classID;
    }

    public String getAssignmentType() {
        return assignmentType;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public int getResultMax() {
        return resultMax;
    }

    public int getResultAchieved() {
        return resultAchieved;
    }

   // public String getHandInDate() {
      //  return handInDate;
    //}
}
