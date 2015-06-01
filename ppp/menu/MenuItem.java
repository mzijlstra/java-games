package ppp.menu;

public class MenuItem {
	private String name;
	private Task task;
	
	public MenuItem(String name, Task task) {
		this.name = name;
		this.task = task;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void doOption() {
		task.doTask();
	}
}
