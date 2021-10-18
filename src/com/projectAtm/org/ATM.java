package com.projectAtm.org;

import java.io.IOException;
import java.sql.SQLException;

public class ATM extends OptionMenu
{
	public static void main(String[] args) throws IOException, InterruptedException, SQLException
	{
		OptionMenu optionMenu = new OptionMenu();
		optionMenu.getLogin();
	}
}