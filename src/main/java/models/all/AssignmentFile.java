package models.all;

import java.io.Serializable;

public class AssignmentFile implements Serializable {

    private int classID;
    private String assignmentType;
    private String assignmentName;
    private String resultMax;
    private String resultAchieved;
    private String extension;
    private String uploadedDateandTime;
    private byte[] fileData;

    public AssignmentFile(int classID, String assignmentType, String assignmentName, String resultMax, String resultAchieved, String extension, String uploadedDateandTime, byte[] fileData) {
        this.classID = classID;
        this.assignmentType = assignmentType;
        this.assignmentName = assignmentName;
        this.resultMax = resultMax;
        this.resultAchieved = resultAchieved;
        this.extension = extension;
        this.uploadedDateandTime = uploadedDateandTime;
        this.fileData = fileData;
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

    public String getResultMax() {
        return resultMax;
    }

    public String getResultAchieved() {
        return resultAchieved;
    }

    public String getExtension() {
        return extension;
    }

    public String getUploadedDateandTime() {
        return uploadedDateandTime;
    }

    public byte[] getFileData() {
        return fileData;
    }
}
