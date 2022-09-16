package ExerTest;

public class Student {
	private int flowID;
	private int type;
	private String IDCard;
	private String examCard;
	private String name;
	private String location;
	private int grade;
	public Student() {
		super();
	}
	public Student(int flowID, int type, String iDCard, String examCard, String name, String location, int grade) {
		super();
		this.flowID = flowID;
		this.type = type;
		IDCard = iDCard;
		this.examCard = examCard;
		this.name = name;
		this.location = location;
		this.grade = grade;
	}
	public int getFlowID() {
		return flowID;
	}
	public void setFlowID(int flowID) {
		this.flowID = flowID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getIDCard() {
		return IDCard;
	}
	public void setIDCard(String iDCard) {
		IDCard = iDCard;
	}
	public String getExamCard() {
		return examCard;
	}
	public void setExamCard(String examCard) {
		this.examCard = examCard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	@Override
	public String toString() {
		System.out.println("=========��ѯ���===========");
		return info();
	}
	private String info() {
		return "��ˮ�ţ�" + flowID + "\n�ļ�/������" + type + "\n���֤�ţ�" + IDCard + "\n׼��֤�ţ�" + examCard + 
				"\nѧ��������" + name + "\n����" + location + "\n�ɼ���" + grade;
	}
}
