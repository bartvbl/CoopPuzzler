package client.input;

import org.lwjgl.input.Keyboard;

public class KeyboardToCharConverter {
	public static char NO_MATCH = '%';

	public static char getKeyCharValue()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			return 'a';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_B))
		{
			return 'b';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_C))
		{
			return 'c';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			return 'd';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			return 'e';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			return 'f';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_G))
		{
			return 'g';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_H))
		{
			return 'h';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_I))
		{
			return 'i';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_J))
		{
			return 'j';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_K))
		{
			return 'k';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_L))
		{
			return 'l';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_M))
		{
			return 'm';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_N))
		{
			return 'n';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_O))
		{
			return 'o';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_P))
		{
			return 'p';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_Q))
		{
			return 'q';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_R))
		{
			return 'r';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			return 's';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_T))
		{
			return 't';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_U))
		{
			return 'u';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_V))
		{
			return 'v';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			return 'w';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			return 'x';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_Y))
		{
			return 'y';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_Z))
		{
			return 'z';
		} else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
		{
			return ' ';
		} else {
			return NO_MATCH;
		}
	}
}
