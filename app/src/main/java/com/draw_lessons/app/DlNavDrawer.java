package com.draw_lessons.app;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import draw_lessons.com.drawlessons.R;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialAccountListener;


public class DlNavDrawer extends MaterialNavigationDrawer implements MaterialAccountListener {

	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dl_nav_drawer);
	}*/
	public void init(Bundle savedInstanceState) {
		//add some accounts
		MaterialAccount account = new MaterialAccount(this.getResources(),"Aleix","aleix.casanova@gmail.com", R.drawable.photo, R.drawable.bamboo);
		this.addAccount(account);

		//set an account listener
		this.setAccountListener(this);

		//create levels in navigation drawer
//		this.addSection(newSection("Dibujar", new activity_draw()));
		this.addSection(newSection("Cursos", new activity_cursos()));
		// create bottom section
		this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));


	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_dl_nav_drawer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onAccountOpening(MaterialAccount account) {

	}

	@Override
	public void onChangeAccount(MaterialAccount newAccount) {

	}
}
