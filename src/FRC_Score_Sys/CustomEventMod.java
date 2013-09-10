package FRC_Score_Sys;
import java.util.EventObject;
import java.util.EventListener;

class MessageCap extends EventObject{
	private static final long serialVersionUID = 1L;
	public String Msg;
	public String LongMsg;
	public MessageCap(Object source, String Message, String LongMessage){
		super(source);
		this.Msg = Message;
		this.LongMsg = LongMessage;
	}
}
interface MyEventListener extends EventListener {
	public void myEventOccurred(MessageCap evt);
}