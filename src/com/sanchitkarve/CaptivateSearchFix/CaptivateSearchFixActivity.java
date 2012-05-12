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
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

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
			String buttonTag = "";
			if(optSearch.isChecked()) buttonTag = "SEARCH";
			else if(optHome.isChecked()) buttonTag = "HOME";
			else if(optMenu.isChecked()) buttonTag = "MENU";
			else if(optBack.isChecked()) buttonTag = "BACK";
			
			try {
			/*
			 * TODO:
			 * 1) Figure out how to tweak timeout and sleeptime values to get the toasts to display.
			 * 2) See the RootTools wiki for instructions of "memory saving" method to do all superuser operations in just
			 *    one RootTools code segment so that
			 *    a) The superuser toast is not shown every time
			 *    b) Hoping it has something to do with the toasts not being displayed below...and using "memory saving" method might fix it.
			 */				
				//get mount point
				List<String> mntRes = RootTools.sendShell("mount | grep /system | sed -e 's/\\(.*\\) \\/system .*/\\1/'", 1000);
				if(mntRes.size() == 2 && mntRes.get(1).equals("0"))
				{
					Toast.makeText(getBaseContext(), "Success. /system = " + mntRes.get(0), Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Failure. Could not find /system. Value = " + mntRes.get(0) + " Error Code = " + mntRes.get(1), Toast.LENGTH_SHORT).show();
					return;
				}			
				//Mount as rewritable
				List<String> remntRes = RootTools.sendShell("mount -o rw,remount -t yaffs2 " + mntRes.get(0) + " /system/", 1000);
				if(remntRes.size() == 1 && remntRes.get(0).equals("0"))
				{
					Toast.makeText(getBaseContext(), "Success: Mounted /system as RW.", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Failure: Could not mount /system as RW.\nError Code = " + remntRes.get(0), Toast.LENGTH_SHORT).show();
					return;
				}
				//String commentCmd = "find /system/usr/keylayout/ -type f \\( -name '*.kl' ! -name 'Generic.kl' ! -name 'Vendor*' ! -name 'AVRCP*' ! -name 'qwerty*' ! -name 'sec_jack*' ! -name 's3c-k*' \\) -print0 | xargs -0 sed -i 's/^key \\([[:digit:]]*\\)\\([[:blank:]]*\\)\\(SEARCH\\)\\([[:blank:]]*\\)\\(VIRTUAL\\)/#key \1\2\3\4\5/'";
				//String commentCmd = "find /sdcard/ -type f \\( -name \'*.kl\' ! -name \'Generic.kl\' ! -name \'Vendor*\' ! -name \'AVRCP*\' ! -name \'qwerty*\' ! -name \'sec_jack*\' ! -name \'s3c-k*\' \\) -print0 | xargs -0 sed -i \'s/^key \\([[:digit:]]*\\)\\([[:blank:]]*\\)\\(SEARCH\\)\\([[:blank:]]*\\)\\(VIRTUAL\\)/# key \1\2\3\4\5/\'";
				String commentCmd = "";
				BufferedReader br = null;
			    try {
			        br = new BufferedReader(new InputStreamReader(getAssets().open("comment.txt"))); //throwing a FileNotFoundException?
			        String word = "";
			        while((word=br.readLine()) != null)
			        	commentCmd = word; //break txt file into different words, add to wordList
			    }
			        catch(IOException e) {
			            e.printStackTrace();
			        }
			        finally {
			            try {
			                br.close(); //stop reading
			            }
			            catch(IOException ex) {
			                ex.printStackTrace();
			            }
			        }
				List<String> uc = RootTools.sendShell(commentCmd, 1000);
				for(int i=0 ; i < uc.size(); i++)
				{
					Toast.makeText(getBaseContext(), i + " = " + uc.get(i), Toast.LENGTH_SHORT).show();
				}
				List<String> resetmntRes = RootTools.sendShell("mount -o ro,remount -t yaffs2 " + mntRes.get(0) + " /system/", 1000);
				if(resetmntRes.size() == 1 && resetmntRes.get(0).equals("0"))
				{
					Toast.makeText(getBaseContext(), "Success: Mounted /system as RO.", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Failure: Could not mount /system as RO.\nError Code = " + resetmntRes.get(0), Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(getBaseContext(), "FIX SUCCESS", Toast.LENGTH_SHORT).show();
				/*for(int i=0 ; i < remntRes.size(); i++)
				{
					Toast.makeText(getBaseContext(), i + " = " + remntRes.get(i), Toast.LENGTH_SHORT).show();
				}*/
			/*File sdCard = Environment.getExternalStorageDirectory();
			File x = new File(sdCard.getAbsolutePath() + "/MyFiles");
			String[] cmds = { "cd /", "tar zxvf /sdcard/MyFiles/com.amazon.venezia.tar.gz"};
			Toast.makeText(getBaseContext(), "before", Toast.LENGTH_SHORT).show();
			List<String> res = RootTools.sendShell(cmds, 50, 500);			
			for(String x1 : res)
			{
				Toast.makeText(getBaseContext(), "tar result = " + x1, Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(getBaseContext(), "result length" + res.size(), Toast.LENGTH_SHORT).show();
			*/
			}
			catch(Exception e){e.printStackTrace();}
			
		}
	};
	
	View.OnClickListener btnUnfixOnClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String buttonTag = "";
			if(optSearch.isChecked()) buttonTag = "SEARCH";
			else if(optHome.isChecked()) buttonTag = "HOME";
			else if(optMenu.isChecked()) buttonTag = "MENU";
			else if(optBack.isChecked()) buttonTag = "BACK";
			
			try {
			/*
			 * TODO:
			 * 1) Figure out how to tweak timeout and sleeptime values to get the toasts to display.
			 * 2) See the RootTools wiki for instructions of "memory saving" method to do all superuser operations in just
			 *    one RootTools code segment so that
			 *    a) The superuser toast is not shown every time
			 *    b) Hoping it has something to do with the toasts not being displayed below...and using "memory saving" method might fix it.
			 */				
				//get mount point
				List<String> mntRes = RootTools.sendShell("mount | grep /system | sed -e 's/\\(.*\\) \\/system .*/\\1/'", 1000);
				if(mntRes.size() == 2 && mntRes.get(1).equals("0"))
				{
					Toast.makeText(getBaseContext(), "Success. /system = " + mntRes.get(0), Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Failure. Could not find /system. Value = " + mntRes.get(0) + " Error Code = " + mntRes.get(1), Toast.LENGTH_SHORT).show();
					return;
				}			
				//Mount as rewritable
				List<String> remntRes = RootTools.sendShell("mount -o rw,remount -t yaffs2 " + mntRes.get(0) + " /system/", 1000);
				if(remntRes.size() == 1 && remntRes.get(0).equals("0"))
				{
					Toast.makeText(getBaseContext(), "Success: Mounted /system as RW.", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Failure: Could not mount /system as RW.\nError Code = " + remntRes.get(0), Toast.LENGTH_SHORT).show();
					return;
				}
				//String unCommentCmd = "find /system/usr/keylayout/ -type f \\( -name '*.kl' ! -name 'Generic.kl' ! -name 'Vendor*' ! -name 'AVRCP*' ! -name 'qwerty*' ! -name 'sec_jack*' ! -name 's3c-k*' \\) -print0 | xargs -0 sed -i 's/^#[[:blank:]]*key \\([[:digit:]]*\\)\\([[:blank:]]*\\)\\(SEARCH\\)\\([[:blank:]]*\\)\\(VIRTUAL\\)/key \1\2\3\4\5/'";
				//String unCommentCmd = "find /sdcard/ -type f \\( -name \'*.kl\' ! -name \'Generic.kl\' ! -name \'Vendor*\' ! -name \'AVRCP*\' ! -name \'qwerty*\' ! -name \'sec_jack*\' ! -name \'s3c-k*\' \\) -print0 | xargs -0 sed -i \'s/^#[[:blank:]]*key \\([[:digit:]]*\\)\\([[:blank:]]*\\)\\(SEARCH\\)\\([[:blank:]]*\\)\\(VIRTUAL\\)/key \1\2\3\4\5/\'";
				String unCommentCmd = "";
				BufferedReader br = null;
			    try {
			        br = new BufferedReader(new InputStreamReader(getAssets().open("uncomment.txt"))); //throwing a FileNotFoundException?
			        String word = "";
			        while((word=br.readLine()) != null)
			        	unCommentCmd = word; //break txt file into different words, add to wordList
			    }
			        catch(IOException e) {
			            e.printStackTrace();
			        }
			        finally {
			            try {
			                br.close(); //stop reading
			            }
			            catch(IOException ex) {
			                ex.printStackTrace();
			            }
			        }
				List<String> uc = RootTools.sendShell(unCommentCmd, 1000);
				for(int i=0 ; i < uc.size(); i++)
				{
					Toast.makeText(getBaseContext(), i + " = " + uc.get(i), Toast.LENGTH_SHORT).show();
				}
				List<String> resetmntRes = RootTools.sendShell("mount -o ro,remount -t yaffs2 " + mntRes.get(0) + " /system/", 1000);
				if(resetmntRes.size() == 1 && resetmntRes.get(0).equals("0"))
				{
					Toast.makeText(getBaseContext(), "Success: Mounted /system as RO.", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(getBaseContext(), "Failure: Could not mount /system as RO.\nError Code = " + resetmntRes.get(0), Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(getBaseContext(), "UNFIX SUCCESS", Toast.LENGTH_SHORT).show();
			}
			catch(Exception e){e.printStackTrace();}
			
		}
	};
}