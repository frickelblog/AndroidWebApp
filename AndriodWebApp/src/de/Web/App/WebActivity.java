package de.Web.App;

import de.tA.App.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // WebView zuweisen	
        WebView myWebView = (WebView) findViewById(R.id.webview);

        // JavaScript einschalten
        myWebView.getSettings().setJavaScriptEnabled(true);
        
        // URL in das WebView laden
        myWebView.loadUrl("http://frickelblog.de/app/index.html");
        
        // WebViewClient setzen um BrowserLeiste auszublenden
        myWebView.setWebViewClient(new myWebViewClient());
        
        // WebChromeClient setzen um Browserfunktionen nutzen zu können
        myWebView.setWebChromeClient(new WebChromeClient());
        
        // Um HTML5 Localstorage zu ermöglichen
        myWebView.getSettings().setDomStorageEnabled(true);
    }
    
        
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      
    	// WebView zuweisen	
        WebView myWebView = (WebView) findViewById(R.id.webview);
       
        /* 	Diesen Abschnitt brauchen wir um den BACK-Button für das Webview zu aktivieren.
        	Wenn wir dies nicht einfügen würden, dann würde man immer aus der App fliegen,
        	sobald man auf den BACK-Button getippt hat, weil das Webview als Einzelnes 
        	Activity Angesprochen wird.
        	Mit diesem Abschnitt wird geschait ob die Brwoser-Historie des Webviews noch
        	Seiten beinhaltet - diese werden dann zurück gesprungen bevor die App geschlossen wird.
        */
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	if(myWebView.canGoBack())
	    	{
	    		myWebView.goBack();
	    		return true;
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
    }
    
    
    // Unsere eigene Klasse für den Abgeleiteten WebviewClient
    private class myWebViewClient extends WebViewClient {

    	// Hier überschreiben wir die URL Loading Methode um Links wie mailto: / tel: oder geo: mit den 
    	// dazugehörigen Intents zu laden
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) 
        {
        	if(url.startsWith("mailto:"))
        	{
                MailTo mt = MailTo.parse(url);
                Intent i = newEmailIntent(WebActivity.this, mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                startActivity(i);
                view.reload();
                return true;
            }
        	else if (url.startsWith("tel:") || url.startsWith("geo:"))
        	{ 
        		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)); 
        		startActivity(intent); 
        		return true;
            }
        	else
        	{
        		view.loadUrl(url);
        		return true;
        	}
        }
    }
    
    // Dies hier ist eine kleine Statische Methode um eine E-Mail weg schicken zu können
    public static Intent newEmailIntent(Context context, String address, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { address });
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.setType("message/rfc822");
        return intent;
  }

    
};

