package com.example.coursemanager.services;

public class Course {
    private String courseName;
    private String time;
    private String instructor;
    private String daysOfWeek;
    private String professor;
    private String classSection;
    private String location;
    private String roomNumber;

    public Course(String courseName, String time, String instructor, String daysOfWeek, String professor, String classSection, String location, String roomNumber) {
        this.courseName = courseName;
        this.time = time;
        this.instructor = instructor;
        this.daysOfWeek = daysOfWeek;
        this.professor = professor;
        this.classSection = classSection;
        this.location = location;
        this.roomNumber = roomNumber;
    }
    public String getCourseName() {
        return courseName;
    }

    public String getTime() {
        return time;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public String getProfessor() {
        return professor;
    }

    public String getClassSection() {
        return classSection;
    }

    public String getLocation() {
        return location;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}