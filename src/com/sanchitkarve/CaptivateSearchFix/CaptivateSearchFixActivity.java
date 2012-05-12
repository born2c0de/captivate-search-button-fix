package com.sanchitkarve.CaptivateSearchFix;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class CaptivateSearchFixActivity extends Activity {
	Button btnFix;
	Button btnUnfix;
	RadioButton optSearch;
	RadioButton optBack;
	RadioButton optHome;
	RadioButton optMenu;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnFix = (Button)findViewById(R.id.btnFix);
        btnFix.setOnClickListener(btnFixOnClick);
        
        btnUnfix = (Button)findViewById(R.id.btnUnfix);
        btnUnfix.setOnClickListener(btnUnfixOnClick);
        
        optSearch = (RadioButton)findViewById(R.id.optSearch);
        optBack = (RadioButton)findViewById(R.id.optBack);
        optHome = (RadioButton)findViewById(R.id.optHome);
        optMenu = (RadioButton)findViewById(R.id.optMenu);
    }
    
    View.OnClickListener btnFixOnClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	View.OnClickListener btnUnfixOnClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
}