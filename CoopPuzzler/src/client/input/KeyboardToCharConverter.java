package client.input;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import util.Queue;

public class KeyboardToCharConverter {
	public static char NO_MATCH = '%';
	public static char BACKSPACE = '';
	
	private static char[] validCharacters = new char[]{'', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' ', '?', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
	
	private static final Queue<Integer> eventKeyCodes = new Queue<Integer>();
	private static final Queue<Character> eventCharacters = new Queue<Character>();
	private static boolean haveKeyboardEventsOccurred = false;
	
	public static boolean haveKeyboardEventsOccurred() {
		boolean temp = haveKeyboardEventsOccurred;
		haveKeyboardEventsOccurred = false;
		return temp;
	}
	
	public static void update() {
		while(Keyboard.next()) {
			char character = Keyboard.getEventCharacter();
			int eventKeyCode = Keyboard.getEventKey();
			
			eventCharacters.enqueue(character);
			eventKeyCodes.enqueue(eventKeyCode);
			
			haveKeyboardEventsOccurred = true;
		}
	}
	
	public static char getKeyCharValue()
	{	
		while(!eventCharacters.isEmpty()) {
			
			char character = eventCharacters.dequeue();	
			character = Character.toLowerCase(character);
			
			int eventKeyCode = eventKeyCodes.dequeue();
			updatePressedKeys(eventKeyCode);
			
			int index = indexOf(character);
			if(index != -1){
				if(!isKeyDown(eventKeyCode)) {
					pressedKeys.add(eventKeyCode);
					return character;
				}
			}
		}
		
		return NO_MATCH;
	}

	private static void updatePressedKeys(int eventKeyCode) {
		if(isKeyDown(eventKeyCode)) {
			for(int i = 0; i < pressedKeys.size(); i++) {
				if(eventKeyCode == pressedKeys.get(i).intValue())
					pressedKeys.remove(i);
			}
		}
	}

	private static boolean isKeyDown(int eventKey) {
		for(Integer keyCode : pressedKeys) {
			if(keyCode.intValue() == eventKey) return true;
		}
		return false;
	}

	private static int indexOf(char character) {
		for(int i = 0; i < validCharacters.length; i++) {
			char pressedKey = validCharacters[i];
			if(pressedKey == character) {
				return i;
			}
		}
		return -1;
	}
}
