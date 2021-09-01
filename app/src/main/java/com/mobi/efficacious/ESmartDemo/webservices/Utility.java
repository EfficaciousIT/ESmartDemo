package com.mobi.efficacious.ESmartDemo.webservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.Toast;

public class Utility 
{
	static Utility utilities;
	private Utility(Context context) 
	{
		mcontext=context;
		contextThemeWrapper=new ContextThemeWrapper(mcontext,android.R.style.Theme_Holo_Dialog);
		builder = new AlertDialog.Builder(contextThemeWrapper);
		progress_dialog = new ProgressDialog(contextThemeWrapper);
	}
	
	public static final int DELAY=1000;
	static Context mcontext;
	
	static ContextThemeWrapper contextThemeWrapper;
	static AlertDialog.Builder builder;
	static ProgressDialog progress_dialog;
	
	RatingDialogListener ratingDialogListener;
	DialogListener dialogListener;

	Handler handler=new Handler();
	
	public static Utility getInstance(Context context)
	{		
		if(utilities==null)
				utilities=new Utility(context);
		mcontext=context;
		return utilities;	
	}
	
	public  boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public boolean haveNetworkConnection() 
	{
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager)mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}
	
	public void showYesNoDialog(String message,String yesButtonText,String noButtonText)
	{
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	dialogListener.OnYesButtonClicked();
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		        	dialogListener.OnNoButtonClicked();
		            break;
		        }
		    }
		};

		
		builder.setMessage(""+message)
		.setPositiveButton(""+yesButtonText, dialogClickListener)
		 .setNegativeButton(""+noButtonText, dialogClickListener).show();
	}
	
	public void showToast(final String message)
	{	((Activity)mcontext).runOnUiThread(new Runnable() {
		
		@Override
		public void run() 
		{
			Toast toast= Toast.makeText(mcontext,message, Toast.LENGTH_SHORT);  
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();	
		}
		});
	
	}
	
	public void showProgreessDialog()
    { 		
		 try {
			//dismiss();
			 progress_dialog=new ProgressDialog(mcontext);
			progress_dialog.setMessage("Loading please wait..");
			progress_dialog.setCancelable(true);		
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() 
				{
					 if(!progress_dialog.isShowing())
				        	progress_dialog.show();
					
				}
			}, DELAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       

    }
	
	public void dismiss()
	{
		try {
				handler.postDelayed(new Runnable() 
				{
				
				@Override
				public void run() 
				{
					if(progress_dialog!=null)
					{
						if(progress_dialog.isShowing())
						progress_dialog.dismiss();	
						progress_dialog=null;
					}
					
				}
			}, DELAY);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
    
	public void showProgreessDialog(boolean isCancelable)
    { 	
		
		 try {
			//dismiss();
			progress_dialog.setMessage("Loading please wait..");
			progress_dialog.setCancelable(isCancelable);
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() 
				{
					 if(!progress_dialog.isShowing())
				        	progress_dialog.show();
					
				}
			}, DELAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       

    }
    
	void showProgreessDialog(String message)
    { 	
        try {
			progress_dialog.setMessage(""+message);
			progress_dialog.setCancelable(true);
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() 
				{
					 if(!progress_dialog.isShowing())
				        	progress_dialog.show();
					
				}
			}, DELAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void sendEmail(String to,String subject,String message)
	{
		
		if(to.toString().equalsIgnoreCase(" ")|| to.toString().equalsIgnoreCase("#"))
		{
			utilities.showToast("No email information available.");
		}
		else
		{
			Intent email = new Intent(Intent.ACTION_SEND);
			  email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
			  //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
			  //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
			  email.putExtra(Intent.EXTRA_SUBJECT, subject);
			  email.putExtra(Intent.EXTRA_TEXT, message);

			  //need this to prompts email client only
			  email.setType("message/rfc822");

			  mcontext.startActivity(Intent.createChooser(email, "Choose an Email client :"));
		}
		
	}
	
	public void dialNumber(String number)
	{
		try {
			if(number.toString().equalsIgnoreCase(" ")||number.toString().equalsIgnoreCase("#"))
			{
				utilities.showToast("No contact information available.");
			}
			else
			{
				Intent dialIntent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+number));
				mcontext.startActivity(dialIntent);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
//	public void showRatingDialog(String strTitle,String message,final int Id)
//	{
//		final Dialog rankDialog = new Dialog(contextThemeWrapper);
//	        rankDialog.setContentView(R.layout.rating_dialog_layout);
//	        rankDialog.setTitle(strTitle);	        
//	        rankDialog.setCancelable(true);
//	        final RatingBar   ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
//	        ratingBar.setRating(0);
//	        
//	        //TextView textTitle = (TextView) rankDialog.findViewById(R.id.rank_dialog_textTitle);
//	        //textTitle.setText(strTitle);
//	        
//	        TextView textMessage = (TextView) rankDialog.findViewById(R.id.rank_dialog_Message);
//	        textMessage.setText(message);
//	        
//	        
//	        Button btnOk = (Button) rankDialog.findViewById(R.id.rank_dialog_Ok_button);
//	        btnOk.setOnClickListener(new View.OnClickListener() 
//	        {
//	            @Override
//	            public void onClick(View v) 
//	            {
//	                rankDialog.dismiss();
//	                ratingDialogListener.OnRatingSet(ratingBar.getRating(), Id);
//	            }
//	        });
//	        
//	        Button btnCancel = (Button) rankDialog.findViewById(R.id.rank_dialog_Cancel_button);
//	        btnCancel.setOnClickListener(new View.OnClickListener() {
//	            @Override
//	            public void onClick(View v) 
//	            {
//	                rankDialog.dismiss();
//	                ratingDialogListener.OnRatingCancelled();
//	            }
//	        });
//	        
//	        
//	        //now that the dialog is set up, it's time to show it    
//	        rankDialog.show();     
//	}
//		
	public void setRatingDialogListener(RatingDialogListener ratingDialogListener) 
	{
		this.ratingDialogListener = ratingDialogListener;
	}
	
	
	public void setDialogListener(DialogListener ratingDialogListener) 
	{
		this.dialogListener = ratingDialogListener;
	}
	
	public interface RatingDialogListener
	{
		public abstract void OnRatingSet(float rating, int id);
		public abstract void OnRatingCancelled();
	}

	public interface DialogListener
	{
		public abstract void OnYesButtonClicked();
		public abstract void OnNoButtonClicked();
	}

}
