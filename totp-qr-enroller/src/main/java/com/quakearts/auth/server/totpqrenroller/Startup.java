package com.quakearts.auth.server.totpqrenroller;

import com.quakearts.appbase.Main;
import com.quakearts.auth.server.totpqrenroller.command.GenerateCodeCommand;
import com.quakearts.utilities.CommandMain;

public class Startup {
	public static void main(String[] args) {
		if(args.length == 0) {
			Main.main(new String[] {AppInit.class.getName(),"-dontwaitinmain"});			
		} else {
			String[] commandArgs = new String[args.length+1];
			commandArgs[0] = GenerateCodeCommand.class.getName();
			System.arraycopy(args, 0, commandArgs, 1, args.length);
			CommandMain.main(commandArgs);
		}
	}
}
