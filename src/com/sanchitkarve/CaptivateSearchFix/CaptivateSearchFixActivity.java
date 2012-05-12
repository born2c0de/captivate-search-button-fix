package com.sanchitkarve.CaptivateSearchFix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.stericson.RootTools.RootTools;

public class CaptivateSearchFixActivity extends Activity {
	Button btnFix;
	Button btnUnfix;
	TextView lblResult;
	RadioButton optSearch;
	/*RadioButton optBack;
	RadioButton optHome;
	RadioButton optMenu;*/
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnFix = (Button)findViewById(R.id.btnFix);
        btnFix.setOnClickListener(btnFixOnClick);
        
        btnUnfix = (Button)findViewById(R.id.btnUnfix);
        btnUnfix.setOnClickListener(btnUnfixOnClick);
        
        lblResult = (TextView)findViewById(R.id.lblResult); 
        
        optSearch = (RadioButton)findViewById(R.id.optSearch);
        /*optBack = (RadioButton)findViewById(R.id.optBack);
        optHome = (RadioButton)findViewById(R.id.optHome);
        optMenu = (RadioButton)findViewById(R.id.optMenu);*/
        try
        {
        	if(!RootTools.isRootAvailable())
        	{
        		btnFix.setEnabled(false);        		
        		btnUnfix.setEnabled(false);        		
        		btnFix.setText("NO ROOT ACCESS");
        		btnUnfix.setText("SUPERUSER NOT FOUND");
        		return;
        	}
        	if(!RootTools.isBusyboxAvailable())
        	{
        		btnFix.setEnabled(false);        		
        		btnUnfix.setEnabled(false);
        		String msg = getString(R.string.BusyBoxFAIL);
        		btnFix.setText(msg);
        		btnUnfix.setText(msg);
        		return;
        	}
        	if(!RootTools.isAccessGiven())
        	{
        		btnFix.setEnabled(false);        		
        		btnUnfix.setEnabled(false);
        		String msg = getString(R.string.RootPermissionFAIL);
        		btnFix.setText(msg);
        		btnUnfix.setText(msg);        		
        	}
        	
        }
        catch(Exception e){e.printStackTrace();}
    }
    
    /**
     * 
     * @param mode 0 - Fix. 1 - Unfix
     */
    private void fixCaptivateSearchButton(int mode)
    {
		String result = getString(R.string.lblResult) + "\n";
		lblResult.setText(result);
		/*
    	String buttonTag = "";
		if(optSearch.isChecked()) buttonTag = getString(R.string.optSearchText);
		else if(optHome.isChecked()) buttonTag = getString(R.string.optHomeText);
		else if(optMenu.isChecked()) buttonTag = getString(R.string.optMenuText);
		else if(optBack.isChecked()) buttonTag = getString(R.string.optBackText);
		*/
		
		try {					
			//get mount point
			List<String> mntRes = RootTools.sendShell("mount | grep /system | sed -e 's/\\(.*\\) \\/system .*/\\1/'", 1000);
			if(mntRes.size() == 2 && mntRes.get(1).equals("0"))
			{
				//Toast.makeText(getBaseContext(), "Success. /system = " + mntRes.get(0), Toast.LENGTH_SHORT).show();
				result += getString(R.string.MountSuccess) + mntRes.get(0) + "\n";
				lblResult.setText(result);
			}
			else
			{
				//Toast.makeText(getBaseContext(), "Failure. Could not find /system. Value = " + mntRes.get(0) + " Error Code = " + mntRes.get(1), Toast.LENGTH_SHORT).show();
				result += getString(R.string.MountFail) + "\n";					
				lblResult.setText(result);
				return;
			}			
			//Mount as rewritable
			List<String> remntRes = RootTools.sendShell("mount -o rw,remount -t yaffs2 " + mntRes.get(0) + " /system/", 1000);
			if(remntRes.size() == 1 && remntRes.get(0).equals("0"))
			{
				//Toast.makeText(getBaseContext(), "Success: Mounted /system as RW.", Toast.LENGTH_SHORT).show();
				result += getString(R.string.MountRWSuccess) + "\n";
				lblResult.setText(result);
			}
			else
			{
				//Toast.makeText(getBaseContext(), "Failure: Could not mount /system as RW.\nError Code = " + remntRes.get(0), Toast.LENGTH_SHORT).show();
				result += getString(R.string.MountRWFail) + "\n";
				lblResult.setText(result);
				return;
			}

			String shellCmd = "";
			BufferedReader br = null;
		    try {
		    	String cmdFile = (mode == 0) ? "comment.txt" : "uncomment.txt";
		        br = new BufferedReader(new InputStreamReader(getAssets().open(cmdFile)));
		        String line = "";
		        while((line = br.readLine()) != null)
		        	shellCmd = line;
		    }
		    catch(IOException e) {
		    	e.printStackTrace();
		    }
		    finally {
		    	try {
		    		br.close();
		        }
		        catch(IOException ex) {
		        	ex.printStackTrace();
		        }
		   }
			List<String> uc = RootTools.sendShell(shellCmd, 1000);
			if(uc.size() == 1 && uc.get(0).equals("0"))				
			{
				//Toast.makeText(getBaseContext(), i + " = " + uc.get(i), Toast.LENGTH_SHORT).show();
				result += getString(R.string.KLModifySuccess) + "\n";
				lblResult.setText(result);
			}
			else
			{
				result += getString(R.string.KLModifyFail) + "\n";
				lblResult.setText(result);
			}
			List<String> resetmntRes = RootTools.sendShell("mount -o ro,remount -t yaffs2 " + mntRes.get(0) + " /system/", 1000);
			if(resetmntRes.size() == 1 && resetmntRes.get(0).equals("0"))
			{
				//Toast.makeText(getBaseContext(), "Success: Mounted /system as RO.", Toast.LENGTH_SHORT).show();
				result += getString(R.string.RestoreMountSuccess) + "\n";
				lblResult.setText(result);
			}
			else
			{
				//Toast.makeText(getBaseContext(), "Failure: Could not mount /system as RO.\nError Code = " + resetmntRes.get(0), Toast.LENGTH_SHORT).show();
				result += getString(R.string.RestoreMountFail) + "\n";
				lblResult.setText(result);
				return;
			}

			result += (mode == 0) ? getString(R.string.FixSuccess) :getString(R.string.UnfixSuccess);
			result += "\n" + getString(R.string.RebootMessage);
			lblResult.setText(result);
		}
		catch(Exception e){e.printStackTrace();}
    }
    
    View.OnClickListener btnFixOnClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			fixCaptivateSearchButton(0); // 0 = Fix
		}
	};
	
	View.OnClickListener btnUnfixOnClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			fixCaptivateSearchButton(1); // 1 = Unfix
		}
	};
}