package localchat;

import java.util.ArrayList;

public abstract class MessageSubject {
	private ArrayList<MessageObserver> observers = new ArrayList<>();
	
	public void attach(MessageObserver messageObserver) {
		System.out.println("Observer attached");
		this.observers.add(messageObserver);
	}

	public void notifyAllObservers(Message message) {
		for (MessageObserver observer : this.observers) {
			observer.update(message);
		}
	}
}
