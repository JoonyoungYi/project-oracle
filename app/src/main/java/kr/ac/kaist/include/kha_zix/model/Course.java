package kr.ac.kaist.include.kha_zix.model;


public class Course {

    private int priority = 0;
    private String code = null;
    private String name = null;
    private String from = null;


    private int grade = 0;

    private String semester = null;

    private String[] whyCourses_code = new String[3];
    private String[] fromCourses_code = new String[3];
    private int[] fromCourses_priority = new int[3];
    private String[] toCourses_code = new String[3];
    private int[] toCourses_priority = new int[3];

    private int[] shares = new int[4];

    /**
     * Init
     */

    public Course() {

    }

    /**
     * Getter and Setter
     *
     * @return
     */

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String[] getWhyCourses_code() {
        return whyCourses_code;
    }

    public void setWhyCourses_code(String[] whyCourses_code) {
        this.whyCourses_code = whyCourses_code;
    }

    public int[] getShares() {
        return shares;
    }

    public void setShares(int[] shares) {
        this.shares = shares;
    }

    public String[] getFromCourses_code() {
        return fromCourses_code;
    }

    public void setFromCourses_code(String[] fromCourses_code) {
        this.fromCourses_code = fromCourses_code;
    }

    public int[] getFromCourses_priority() {
        return fromCourses_priority;
    }

    public void setFromCourses_priority(int[] fromCourses_priority) {
        this.fromCourses_priority = fromCourses_priority;
    }

    public String[] getToCourses_code() {
        return toCourses_code;
    }

    public void setToCourses_code(String[] toCourses_code) {
        this.toCourses_code = toCourses_code;
    }

    public int[] getToCourses_priority() {
        return toCourses_priority;
    }

    public void setToCourses_priority(int[] toCourses_priority) {
        this.toCourses_priority = toCourses_priority;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }


}
