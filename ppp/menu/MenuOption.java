package ppp.menu;

public class MenuOption extends MenuItem {
	StringTask optValFunctor = null;
	
	public MenuOption(String name, Task task, StringTask optValFunctor) {
		super(name, task);
		this.optValFunctor = optValFunctor;
	}

	public String getName() {
		String display = super.getName() + " ";
		String value = optValFunctor.getString();
		
		return display + value;
	}
}
